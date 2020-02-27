package ru.sm.poker.service.holdem;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.CombinationDTO;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.CombinationType;
import ru.sm.poker.service.CombinationService;
import ru.sm.poker.service.WinnerService;
import ru.sm.poker.service.common.SecurityNotificationService;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.sm.poker.util.HistoryUtil.sumRoundHistoryBets;
import static ru.sm.poker.util.PlayerUtil.getNotFoldedPlayers;
import static ru.sm.poker.util.PlayerUtil.getPlayersInGame;

@Service
@RequiredArgsConstructor
public class HoldemWinnerService implements WinnerService {

    private final CombinationService combinationService;
    private final SecurityNotificationService securityNotificationService;

    @Override
    public void sendPrizes(HoldemRoundSettings holdemRoundSettings, boolean isNotOpen) {
        if (isNotOpen) {
            giveOutPrizeToLastPlayer(holdemRoundSettings);
            return;
        }

        checkCardsIntersect(getAllCards(holdemRoundSettings));

        final List<Pair<Player, CombinationDTO>> playersAndCombos = findCombinations(
                getPlayersInGame(holdemRoundSettings.getPlayers()),
                getDeck(holdemRoundSettings)
        );
        final List<Pair<Player, CombinationDTO>> winners = findWinners(playersAndCombos);

        if (winners.size() == 1) {
            giveOutPrizeToSingleWinner(holdemRoundSettings, winners.get(0).getKey());
            return;
        }

        final List<Pair<Player, CombinationDTO>> moreStrongerCombinations = combinationService.findMoreStrongerCombinations(winners);
        if (moreStrongerCombinations.size() == 1) {
            giveOutPrizeToSingleWinner(holdemRoundSettings, moreStrongerCombinations.get(0).getKey());
            return;
        }
        processBets(moreStrongerCombinations, holdemRoundSettings);

    }


    private void giveOutPrizeToSingleWinner(HoldemRoundSettings holdemRoundSettings, Player player) {
        processBets(holdemRoundSettings, player);
        securityNotificationService.sendToAllWithSecurityWhoIsNotInTheGame(holdemRoundSettings);
    }

    private void giveOutPrizeToLastPlayer(HoldemRoundSettings holdemRoundSettings) {
        final List<Player> players = holdemRoundSettings.getPlayers();

        final List<Player> lastPlayer = getNotFoldedPlayers(players);

        if (lastPlayer.size() > 1) {
            throw new RuntimeException();
        }

        final Player player = lastPlayer.get(0);
        player.addChips(holdemRoundSettings.getBank());
        securityNotificationService.sendToAllWithSecurity(holdemRoundSettings);
    }


    private void processBets(List<Pair<Player, CombinationDTO>> winners, HoldemRoundSettings holdemRoundSettings) {
        long bank = holdemRoundSettings.getBank();
        final Map<Player, Long> playersBets = getPlayersBets(holdemRoundSettings);
        bank = getBankWithoutPlayersBets(winners, bank, playersBets);
        processBets(winners, bank);
        securityNotificationService.sendToAllWithSecurityWhoIsNotInTheGame(holdemRoundSettings);
    }

    private void processBets(List<Pair<Player, CombinationDTO>> winners, long bank) {
        if (bank > 0) {
            for (Pair<Player, CombinationDTO> winner : winners) {
                final Player player = winner.getKey();
                player.addChips(bank / winners.size());
            }
        }
    }

    private long getBankWithoutPlayersBets(
            List<Pair<Player, CombinationDTO>> winners,
            long bank, Map<Player,
            Long> playersBets
    ) {
        for (Pair<Player, CombinationDTO> winner : winners) {
            final Player player = winner.getLeft();
            final Long bets = playersBets.get(player);
            player.addChips(bets);
            bank -= bets;
        }
        return bank;
    }

    private void processBets(HoldemRoundSettings holdemRoundSettings, Player winner) {
        long calculate = 0;
        final Map<Player, Long> playersBets = getPlayersBets(holdemRoundSettings);
        final Long winnerBets = playersBets.get(winner);
        calculate += winnerBets;
        for (Map.Entry<Player, Long> otherPlayerBets : playersBets.entrySet()) {
            if (otherPlayerBets.getKey().equals(winner)) {
                continue;
            }
            final Long bets = otherPlayerBets.getValue();
            final Player player = otherPlayerBets.getKey();

            if (bets > winnerBets) {
                long returnChips = bets - winnerBets;
                player.addChips(returnChips);
                calculate += winnerBets;
                continue;
            }
            calculate += bets;
        }
        winner.addChips(calculate);
    }

    private Map<Player, Long> getPlayersBets(HoldemRoundSettings holdemRoundSettings) {
        final Map<Player, Long> bets = new HashMap<>();

        final List<Player> players = holdemRoundSettings.getPlayers();

        players.forEach(player -> {
            final long allBets = sumRoundHistoryBets(holdemRoundSettings, player);
            bets.put(player, allBets);
        });

        return bets;

    }

    @Override
    public List<Pair<Player, CombinationDTO>> findCombinations(List<Player> players, List<CardType> deck) {
        final List<Pair<Player, CombinationDTO>> playersComb = new ArrayList<>();
        for (Player player : players) {
            final List<CardType> allCards = player.getCards();
            allCards.addAll(deck);
            final Pair<CombinationType, List<CardType>> combination = combinationService.findCombination(allCards);
            final CombinationDTO combinationDTO = new CombinationDTO(combination.getKey(), combination.getValue());
            playersComb.add(Pair.of(player, combinationDTO));
        }
        return playersComb;
    }


    private List<CardType> getAllCards(HoldemRoundSettings holdemRoundSettings) {
        final List<CardType> cards = new ArrayList<>(getDeck(holdemRoundSettings));
        cards.addAll(holdemRoundSettings.getPlayers()
                .stream()
                .flatMap(playerDTO -> playerDTO.getCards().stream())
                .collect(Collectors.toList()));
        return cards;

    }

    private List<CardType> getDeck(HoldemRoundSettings holdemRoundSettings) {
        final List<CardType> deck = new ArrayList<>(holdemRoundSettings.getFlop());
        deck.add(holdemRoundSettings.getTern());
        deck.add(holdemRoundSettings.getRiver());
        return deck;
    }


    private List<Pair<Player, CombinationDTO>> findWinners(
            List<Pair<Player, CombinationDTO>> playersAndCombinations
    ) {
        final List<CombinationDTO> allCombinations = getCombinations(playersAndCombinations);
        final CombinationDTO theStrongestCombination = findTheStrongestCombination(allCombinations);
        return findPlayersWithTheStrongestCombination(playersAndCombinations, theStrongestCombination);
    }


    private List<Pair<Player, CombinationDTO>> findPlayersWithTheStrongestCombination(
            List<Pair<Player, CombinationDTO>> playersAndCombinations,
            CombinationDTO strongestCombo
    ) {
        return playersAndCombinations.stream()
                .filter(playerAndCombination -> playerAndCombination.getRight().equals(strongestCombo))
                .collect(Collectors.toList());
    }

    private List<CombinationDTO> getCombinations(List<Pair<Player, CombinationDTO>> combinations) {
        return combinations.stream()
                .map(Pair::getValue)
                .collect(Collectors.toList());
    }


    private CombinationDTO findTheStrongestCombination(List<CombinationDTO> combinations) {
        return combinations.stream()
                .max(Comparator.comparingInt(combination -> combination.getCombinationType().getPower()))
                .orElseThrow(() -> new RuntimeException("cannot find the strongest combinations"));
    }

    private void checkCardsIntersect(List<CardType> cards) {
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
