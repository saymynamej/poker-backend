package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.action.Action;
import ru.smn.poker.dto.PlayerCombination;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.util.PlayerUtil;

import java.util.List;
import java.util.stream.Collectors;

import static ru.smn.poker.util.HistoryUtil.sumBets;

@RequiredArgsConstructor
@Service
public class SimplerPrizeService implements PrizeService {
    private final WinnerService winnerService;

    @Override
    public void sendPrizes(RoundSettings roundSettings) {
        final List<PlayerEntity> playersInGame = PlayerUtil.getPlayersInAction(roundSettings.getPlayers());
        if (roundSettings.isOnePlayerLeft()) {
            playersInGame.get(0).addChips(roundSettings.getBank());
            return;
        }
        final List<PlayerCombination> winners = winnerService.findWinners(roundSettings);
        calculate(winners, roundSettings);
    }

    private void calculate(List<PlayerCombination> winners, RoundSettings roundSettings) {
        if (!checkIfNeedReturnToAll(winners, roundSettings)) {
            for (PlayerCombination winnerFromLoop : winners) {
                long winnerBets = sumBets(roundSettings.getFullHistory().get(winners.get(0).getPlayer()));
                long generatedBank = winnerBets;

                final List<PlayerEntity> playersInGame = PlayerUtil.getPlayersInAction(roundSettings.getPlayers()).stream()
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

}
