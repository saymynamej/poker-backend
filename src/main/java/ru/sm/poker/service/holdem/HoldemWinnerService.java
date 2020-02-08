package ru.sm.poker.service.holdem;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.CombinationDTO;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.CombinationType;
import ru.sm.poker.service.CombinationService;
import ru.sm.poker.service.WinnerService;
import ru.sm.poker.service.common.SecurityNotificationService;
import ru.sm.poker.util.PlayerUtil;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.sm.poker.util.HistoryUtil.sumRoundHistoryBets;

@Service
@RequiredArgsConstructor
public class HoldemWinnerService implements WinnerService {

    private final CombinationService combinationService;
    private final SecurityNotificationService securityNotificationService;

    @Override
    public void sendPrizes(HoldemRoundSettingsDTO holdemRoundSettingsDTO) {

        checkCardsIntersect(getAllCards(holdemRoundSettingsDTO));

        final List<PlayerDTO> players = PlayerUtil.getPlayersInGame(holdemRoundSettingsDTO.getPlayers());

        final List<CardType> deck = getDeck(holdemRoundSettingsDTO);

        final List<Pair<PlayerDTO, CombinationDTO>> playersAndCombos = findCombinations(players, deck);

        final long bank = holdemRoundSettingsDTO.getBank();

        final List<Pair<PlayerDTO, CombinationDTO>> winners = findWinners(playersAndCombos);


        if (winners.size() == 1) {
            final Pair<PlayerDTO, CombinationDTO> playerAndCombination = winners.get(0);

            final PlayerDTO player = playerAndCombination.getKey();

            processBets(holdemRoundSettingsDTO, player);

            securityNotificationService.sendToAllWithSecurityWhoIsNotInTheGame(holdemRoundSettingsDTO);
        }

    }

    private void processBets(HoldemRoundSettingsDTO holdemRoundSettingsDTO, PlayerDTO winner) {

        long calculate = 0;

        final Map<PlayerDTO, Long> playersBets = getPlayersBets(holdemRoundSettingsDTO);

        final Long winnerBets = playersBets.get(winner);

        calculate += winnerBets;

        for (Map.Entry<PlayerDTO, Long> otherPlayerBets : playersBets.entrySet()) {
            if (otherPlayerBets.getKey().equals(winner)) {
                continue;
            }
            final Long bets = otherPlayerBets.getValue();
            final PlayerDTO player = otherPlayerBets.getKey();

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

    private Map<PlayerDTO, Long> getPlayersBets(HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        final Map<PlayerDTO, Long> bets = new HashMap<>();

        final List<PlayerDTO> players = holdemRoundSettingsDTO.getPlayers();

        players.forEach(player -> {
            final long allBets = sumRoundHistoryBets(holdemRoundSettingsDTO, player);
            bets.put(player, allBets);
        });

        return bets;

    }

    private Map<PlayerDTO, Long> getBanksForPlayers(HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        final Map<PlayerDTO, Long> banks = new HashMap<>();

        final List<PlayerDTO> players = holdemRoundSettingsDTO.getPlayers();

        players.forEach(player -> {
            final long playerBets = sumRoundHistoryBets(holdemRoundSettingsDTO, player);
            banks.put(player, playerBets * players.size());
        });

        return banks;
    }

    @Override
    public List<Pair<PlayerDTO, CombinationDTO>> findCombinations(List<PlayerDTO> players, List<CardType> deck) {
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


    private List<CardType> getAllCards(HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        final List<CardType> cards = new ArrayList<>(getDeck(holdemRoundSettingsDTO));
        cards.addAll(holdemRoundSettingsDTO.getPlayers()
                .stream()
                .flatMap(playerDTO -> playerDTO.getCards().stream())
                .collect(Collectors.toList()));
        return cards;

    }

    private List<CardType> getDeck(HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        final List<CardType> deck = new ArrayList<>(holdemRoundSettingsDTO.getFlop());
        deck.add(holdemRoundSettingsDTO.getTern());
        deck.add(holdemRoundSettingsDTO.getRiver());
        return deck;
    }


    private List<Pair<PlayerDTO, CombinationDTO>> findWinners(List<Pair<PlayerDTO, CombinationDTO>> playersAndCombinations) {

        final List<CombinationDTO> allCombinations = getCombinations(playersAndCombinations);

        final CombinationDTO theStrongestCombination = findTheStrongestCombination(allCombinations);

        return findPlayersWithTheStrongestCombination(playersAndCombinations, theStrongestCombination);

    }


    private final List<Pair<PlayerDTO, CombinationDTO>> findPlayersWithTheStrongestCombination(
            List<Pair<PlayerDTO, CombinationDTO>> playersAndCombinations,
            CombinationDTO strongestCombo
    ) {
        return playersAndCombinations.stream()
                .filter(playerAndCombination -> playerAndCombination.getRight().equals(strongestCombo))
                .collect(Collectors.toList());
    }

    private List<CombinationDTO> getCombinations(List<Pair<PlayerDTO, CombinationDTO>> combinations) {
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
