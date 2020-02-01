package ru.sm.poker.service.holdem;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.CombinationDTO;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.CombinationType;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.service.CombinationService;
import ru.sm.poker.service.NotificationService;
import ru.sm.poker.service.WinnerService;
import ru.sm.poker.service.common.SecurityNotificationService;

import java.util.ArrayList;
import java.util.Comparator;
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
        final List<Pair<PlayerDTO, CombinationDTO>> playersAndCombinations = findWinners(
                getPlayersInGame(holdemRoundSettingsDTO.getPlayers()),
                holdemRoundSettingsDTO.getFlop(),
                holdemRoundSettingsDTO.getTern(),
                holdemRoundSettingsDTO.getRiver()
        );
        final CombinationDTO theMostPowerFullCombination = findTheMostPowerFullCombination(playersAndCombinations);

        final List<PlayerDTO> playerDTOWithTheMostPowerFullCombinations = findPlayerWithTheMostPowerFullCombinations(
                playersAndCombinations,
                theMostPowerFullCombination.getCombinationType()
        );
        final long bank = holdemRoundSettingsDTO.getBank();
        playerDTOWithTheMostPowerFullCombinations.forEach(player -> player.addChips(bank / playerDTOWithTheMostPowerFullCombinations.size()));
        securityNotificationService.sendToAllWithSecurityWhoIsNotInTheGame(holdemRoundSettingsDTO);

    }


    private List<PlayerDTO> findPlayerWithTheMostPowerFullCombinations(List<Pair<PlayerDTO, CombinationDTO>> player, CombinationType combinationType) {
        return player.stream()
                .filter(pair -> pair.getValue().getCombinationType().equals(combinationType))
                .map(Pair::getKey)
                .collect(Collectors.toList());
    }


    private CombinationDTO findTheMostPowerFullCombination(List<Pair<PlayerDTO, CombinationDTO>> combinations) {
        return combinations.stream()
                .max(Comparator.comparingInt(
                        playerCombinationDTOPair -> playerCombinationDTOPair.getRight()
                                .getCombinationType()
                                .getPower())
                )
                .get()
                .getRight();
    }

    @Override
    public List<Pair<PlayerDTO, CombinationDTO>> findWinners(List<PlayerDTO> playerDTOS, List<CardType> flop, CardType tern, CardType river) {
        final List<Pair<PlayerDTO, CombinationDTO>> playersComb = new ArrayList<>();
        final List<CardType> allCards = playerDTOS.stream()
                .flatMap(player -> player
                        .getCards()
                        .stream())
                .collect(Collectors.toList());

        allCards.addAll(flop);
        allCards.add(tern);
        allCards.add(river);

        cardsIntersect(allCards);

        playerDTOS.forEach(player -> {
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
