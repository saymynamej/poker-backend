package ru.sm.poker.service.holdem;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.CombinationDTO;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.CombinationType;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.service.CombinationService;
import ru.sm.poker.service.WinnerService;
import ru.sm.poker.service.common.SecurityNotificationService;
import ru.sm.poker.util.HistoryUtil;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.sm.poker.util.PlayerUtil.getPlayersInGame;

@Service
@RequiredArgsConstructor
public class HoldemWinnerService implements WinnerService {

    private final CombinationService combinationService;
    private final SecurityNotificationService securityNotificationService;

    @Override
    public void sendPrizes(HoldemRoundSettingsDTO holdemRoundSettingsDTO) {

        final List<PlayerDTO> players = holdemRoundSettingsDTO.getPlayers();
        final List<PlayerDTO> playersInGame = getPlayersInGame(players);
        final List<CardType> deckCards = new ArrayList<>();

        if (playersInGame.size() == 1) {
            final PlayerDTO lastPlayer = playersInGame.get(0);
            lastPlayer.addChips(holdemRoundSettingsDTO.getBank());
            securityNotificationService.sendToAllWithSecurity(holdemRoundSettingsDTO);
            return;
        }

        if (holdemRoundSettingsDTO.getStageType() == StageType.RIVER) {
            if (holdemRoundSettingsDTO.getFlop() != null) {
                deckCards.addAll(holdemRoundSettingsDTO.getFlop());
            }
            if (holdemRoundSettingsDTO.getTern() != null) {
                deckCards.add(holdemRoundSettingsDTO.getTern());
            }
            if (holdemRoundSettingsDTO.getRiver() != null) {
                deckCards.add(holdemRoundSettingsDTO.getRiver());
            }
        }

        if (deckCards.size() != HoldemCombinationService.getCombinationSize()) {
            throw new RuntimeException("global error, cards deck size muste be " + HoldemCombinationService.getCombinationSize());
        }

        final long fullBank = holdemRoundSettingsDTO.getBank();

        final Map<PlayerDTO, Long> banks = new HashMap<>();

        for (PlayerDTO playerDTO : playersInGame) {
            final long allBets = HistoryUtil.sumRoundHistoryBets(holdemRoundSettingsDTO, playerDTO);
            banks.put(playerDTO, allBets * playersInGame.size());
        }








//        final List<Pair<PlayerDTO, CombinationDTO>> combinations = findWinners(playersInGame, deckCards);
//        final CombinationDTO theMostPowerFullCombination = findTheMostPowerFullCombination(combinations);
//        final List<Pair<PlayerDTO, CombinationDTO>> playersWithTheMostPowerFullCombinations = findPlayersWithTheMostPowerFullCombinations(
//                combinations,
//                theMostPowerFullCombination.getCombinationType()
//        );
//
//        if (playersWithTheMostPowerFullCombinations.size() > 1) {
//            final List<Pair<PlayerDTO, CombinationDTO>> winner = findWinnerFromPlayersWhoHasTheSameCombinations(playersWithTheMostPowerFullCombinations);
//            for (Pair<PlayerDTO, CombinationDTO> playerDTOCombinationDTOPair : winner) {
//                final PlayerDTO playerDTO = playerDTOCombinationDTOPair.getKey();
//                playerDTO.addChips(holdemRoundSettingsDTO.getBank() / winner.size());
//                securityNotificationService.sendToAllWithSecurity(holdemRoundSettingsDTO);
//            }
//            return;
//        }
//        final PlayerDTO playerDTO = playersWithTheMostPowerFullCombinations.get(0).getKey();
//        playerDTO.addChips(holdemRoundSettingsDTO.getBank());
//        securityNotificationService.sendToAllWithSecurity(holdemRoundSettingsDTO);
}


    private List<Pair<PlayerDTO, CombinationDTO>> findWinnerFromPlayersWhoHasTheSameCombinations(List<Pair<PlayerDTO, CombinationDTO>> players) {
        return null;
    }


    private List<Pair<PlayerDTO, CombinationDTO>> findPlayersWithTheMostPowerFullCombinations(List<Pair<PlayerDTO, CombinationDTO>> player, CombinationType combinationType) {
        return player.stream()
                .filter(pair -> pair.getValue().getCombinationType().equals(combinationType))
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
    public List<Pair<PlayerDTO, CombinationDTO>> findWinners(List<PlayerDTO> players, List<CardType> deck) {
        final List<Pair<PlayerDTO, CombinationDTO>> playersComb = new ArrayList<>();
        for (PlayerDTO player : players) {
            final List<CardType> allCards = player.getCards();
            allCards.addAll(deck);
            final Pair<CombinationType, List<CardType>> combination = combinationService.findCombination(allCards);
            final CombinationDTO combinationDTO = new CombinationDTO(combination.getKey(), combination.getValue());
            playersComb.add(Pair.of(player, combinationDTO));
        }
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
