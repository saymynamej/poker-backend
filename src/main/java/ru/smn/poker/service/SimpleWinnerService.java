package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.action.Action;
import ru.smn.poker.dto.Card;
import ru.smn.poker.dto.Player;
import ru.smn.poker.dto.PlayerCombination;
import ru.smn.poker.entities.CardEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.CardType;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.service.CombinationService;
import ru.smn.poker.service.WinnerService;
import ru.smn.poker.util.PlayerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.smn.poker.util.HistoryUtil.sumBets;

@Service
@RequiredArgsConstructor
public class SimpleWinnerService implements WinnerService {
    private final CombinationService combinationService;

    @Override
    public void sendPrizes(RoundSettings roundSettings) {
        final List<PlayerEntity> playersInGame = PlayerUtil.getPlayersInGame(roundSettings.getPlayers());
        if (roundSettings.isOnePlayerLeft()) {
            playersInGame.get(0).addChips(roundSettings.getBank());
        } else {
            final List<PlayerCombination> winners = findWinners(roundSettings);
            calculate(winners, roundSettings);
        }
    }


    private void calculate(List<PlayerCombination> winners, RoundSettings roundSettings) {
        if (!checkIfNeedReturnToAll(winners, roundSettings)) {
            for (PlayerCombination winnerFromLoop : winners) {
                long winnerBets = sumBets(roundSettings.getFullHistory().get(winners.get(0).getPlayer()));
                long generatedBank = winnerBets;

                final List<PlayerEntity> playersInGame = PlayerUtil.getPlayersInGame(roundSettings.getPlayers()).stream()
                        .filter(playerDTO -> !playerDTO.equals(winnerFromLoop.getPlayer()))
                        .collect(Collectors.toList());


                for (PlayerEntity player : playersInGame) {
                    final List<Action> otherPlayerActions = roundSettings.getFullHistory().get(player);
                    long otherPlayer = sumBets(otherPlayerActions);
                    if (winnerBets > otherPlayer) {
                        generatedBank += otherPlayer;
                    } else {
                        generatedBank += winnerBets;
                        long returnChips = Math.abs(otherPlayer - winnerBets);
                        player.addChips(returnChips);
                    }
                }
                winnerFromLoop.getPlayer().addChips(generatedBank);
            }
        }
    }

    private boolean checkIfNeedReturnToAll(List<PlayerCombination> winners, RoundSettings roundSettings) {
        final List<Action> allWinnersActions = winners.stream()
                .flatMap(player -> roundSettings.getFullHistory().get(player.getPlayer()).stream())
                .collect(Collectors.toList());

        final long allWinnersBets = sumBets(allWinnersActions);
        if (allWinnersBets == roundSettings.getBank()) {
            winners.forEach(playerCombination -> returnChips(roundSettings, playerCombination));
            return true;
        }
        return false;
    }


    private void returnChips(RoundSettings roundSettings, PlayerCombination playerCombination) {
        playerCombination.getPlayer().addChips(
                sumBets(roundSettings.getFullHistory().get(playerCombination.getPlayer()))
        );
    }

    private List<PlayerCombination> findWinners(RoundSettings roundSettings) {
        final List<CardType> flop = roundSettings.getFlop();
        final CardType tern = roundSettings.getTern();
        final CardType river = roundSettings.getRiver();
        final List<PlayerCombination> combinations = new ArrayList<>();


        PlayerUtil.getPlayersInGame(roundSettings.getPlayers()).forEach(player -> {
            final List<CardType> playerCards = player.getCards().stream()
                    .map(CardEntity::getCardType)
                    .collect(Collectors.toList());

            final List<CardType> cards = new ArrayList<>(playerCards);
            cards.addAll(flop);
            cards.add(tern);
            cards.add(river);
            combinations.add(PlayerCombination.builder()
                    .combination(combinationService.findCombination(cards))
                    .player(player)
                    .build());
        });

        return combinationService.findWinners(combinations);

    }

}
