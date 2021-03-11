package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.action.Action;
import ru.smn.poker.dto.PlayerCombination;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.util.PlayerUtil;

import java.util.List;
import java.util.stream.Collectors;

import static ru.smn.poker.util.HistoryUtil.sumBets;

@RequiredArgsConstructor
@Service
public class SimplerPrizeService implements PrizeService {
    private final WinnerService winnerService;

    @Override
    public void sendPrizes(TableSettings tableSettings) {
        final List<PlayerEntity> playersInGame = PlayerUtil.getPlayersInAction(tableSettings.getPlayers());
        if (tableSettings.isOnePlayerLeft()) {
            playersInGame.get(0).addChips(tableSettings.getBank());
            return;
        }
        final List<PlayerCombination> winners = winnerService.findWinners(tableSettings);
        calculate(winners, tableSettings);
    }

    private void calculate(List<PlayerCombination> winners, TableSettings tableSettings) {
        if (!checkIfNeedReturnToAll(winners, tableSettings)) {
            for (PlayerCombination winnerFromLoop : winners) {
                long winnerBets = sumBets(tableSettings.getFullHistory().get(winners.get(0).getPlayer()));
                long generatedBank = winnerBets;

                final List<PlayerEntity> playersInGame = PlayerUtil.getPlayersInAction(tableSettings.getPlayers()).stream()
                        .filter(playerDTO -> !playerDTO.equals(winnerFromLoop.getPlayer()))
                        .collect(Collectors.toList());

                for (PlayerEntity player : playersInGame) {
                    final List<Action> otherPlayerActions = tableSettings.getFullHistory().get(player);
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

    private boolean checkIfNeedReturnToAll(List<PlayerCombination> winners, TableSettings tableSettings) {
        final List<Action> allWinnersActions = winners.stream()
                .flatMap(player -> tableSettings.getFullHistory().get(player.getPlayer()).stream())
                .collect(Collectors.toList());

        final long allWinnersBets = sumBets(allWinnersActions);
        if (allWinnersBets == tableSettings.getBank()) {
            winners.forEach(playerCombination -> returnChips(tableSettings, playerCombination));
            return true;
        }
        return false;
    }

    private void returnChips(TableSettings tableSettings, PlayerCombination playerCombination) {
        playerCombination.getPlayer().addChips(
                sumBets(tableSettings.getFullHistory().get(playerCombination.getPlayer()))
        );
    }

}
