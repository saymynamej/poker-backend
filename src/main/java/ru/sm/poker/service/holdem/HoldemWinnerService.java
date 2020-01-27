package ru.sm.poker.service.holdem;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.CombinationDTO;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.CombinationType;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.CombinationService;
import ru.sm.poker.service.NotificationService;
import ru.sm.poker.service.WinnerService;
import ru.sm.poker.service.common.SecurityNotificationService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.sm.poker.util.PlayerUtil.getPlayersInGame;

@Service
@RequiredArgsConstructor
public class HoldemWinnerService implements WinnerService {

    private final CombinationService combinationService;
    private final SecurityNotificationService securityNotificationService;
    private final NotificationService notificationService;

    @Override
    public void sendPrizes(HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        final List<Pair<Player, CombinationDTO>> winners = findWinners(
                getPlayersInGame(holdemRoundSettingsDTO.getPlayers()),
                holdemRoundSettingsDTO.getFlop(),
                holdemRoundSettingsDTO.getTern(),
                holdemRoundSettingsDTO.getRiver()
        );
        final long bank = holdemRoundSettingsDTO.getBank();

        final Pair<Player, CombinationDTO> theMostPowerFullCombination = winners.get(0);

        final CombinationDTO powerFullCombination = theMostPowerFullCombination.getValue();

        final List<Pair<Player, CombinationDTO>> allPowerFullCombinations = winners.stream()
                .filter(pair -> pair.getRight().equals(powerFullCombination))
                .collect(Collectors.toList());

        final int size = allPowerFullCombinations.size();

        allPowerFullCombinations.forEach(allPowerFullCombination -> {
            final long result = bank / size;
            final Player player = allPowerFullCombination.getKey();
            player.addChips(result);
        });

        securityNotificationService.sendToAllWithSecurityWhoIsNotInTheGame(holdemRoundSettingsDTO);
        notificationService.sendGameInformationToAll(winners);
    }

    @Override
    public List<Pair<Player, CombinationDTO>> findWinners(List<Player> players, List<CardType> flop, CardType tern, CardType river) {
        final List<Pair<Player, CombinationDTO>> playersComb = new ArrayList<>();
        final List<CardType> allCards = players
                .stream()
                .flatMap(player -> player
                        .getCards()
                        .stream())
                .collect(Collectors.toList());

        allCards.addAll(flop);
        allCards.add(tern);
        allCards.add(river);

        cardsIntersect(allCards);

        players.forEach(player -> {
            final List<CardType> playersCards = new ArrayList<>(player.getCards());
            playersCards.addAll(flop);
            playersCards.add(tern);
            playersCards.add(river);

            final Pair<CombinationType, List<CardType>> combination =
                    combinationService.findCombination(playersCards);

            final CombinationDTO comboDTO = new CombinationDTO(
                    combination.getLeft(),
                    combination.getRight()
            );

            playersComb.add(Pair.of(player, comboDTO));
        });

        playersComb.sort((o1, o2) -> o2.getValue().getCombinationType().getPower() - o1.getValue().getCombinationType().getPower());

        return playersComb;
    }


    private void cardsIntersect(List<CardType> cards) {
        final List<CardType> distinct = cards.stream()
                .distinct()
                .collect(Collectors.toList());

        if (distinct.size() != cards.size()) {
            throw new RuntimeException(format("cards are intersect: %s", findDuplicates(cards, distinct)));
        }
    }

    private List<CardType> findDuplicates(List<CardType> cards, List<CardType> distinct) {
        final List<CardType> copyList = new ArrayList<>(cards);
        for (CardType cardType : distinct) {
            copyList.remove(cardType);
        }
        return copyList;
    }

}
