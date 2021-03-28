package ru.smn.poker.util;

import ru.smn.poker.action.Action;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.stream.PlayerPredicates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HistoryUtil {

    public static boolean canMoveNext(TableSettings tableSettings) {
        return allPlayersInGameHaveSameCountOfBet(tableSettings) && tableSettings.isNotFirstMoveOnBigBlind() && (tableSettings.lastBetIsNotZero() || tableSettings.allPlayersCheck());
    }

    public static boolean allPlayersInGameHaveSameCountOfBet(TableSettings tableSettings) {
        final List<PlayerEntity> playersInGame = PlayerUtil.getPlayersInAction(tableSettings.getPlayers());

        final List<PlayerEntity> players = playersInGame.stream()
                .filter(PlayerPredicates.playerFolded().negate())
                .filter(PlayerPredicates.playerHasChips())
                .collect(Collectors.toList());

        return players.stream()
                .noneMatch(player -> sumStageHistoryBets(tableSettings, player) != tableSettings.getLastBet());
    }

    public static long sumStageHistoryBets(TableSettings tableSettings, PlayerEntity player) {
        final Map<PlayerEntity, List<Action>> history = tableSettings.getStageHistory();
        final List<Action> countActions = history.get(player);
        return sumBets(countActions);
    }

    public static long sumBets(List<Action> actions) {
        if (actions == null) {
            return 0;
        }

        final Optional<Long> reduced = actions.stream()
                .map(Action::getCount)
                .reduce(Long::sum);

        if (reduced.isPresent()) {
            return reduced.get();
        }
        return 0;
    }


    public static long sumAllHistoryBetsWithNewAction(TableSettings tableSettings, PlayerEntity player, Action action) {
        final Map<PlayerEntity, List<Action>> history = tableSettings.getStageHistory();
        final List<Action> countActions = history.get(player);
        return sumBets(countActions) + action.getCount();
    }


    public static void addActionInHistory(Map<PlayerEntity, List<Action>> history, PlayerEntity player) {
        final Action action = player.getAction();
        final List<Action> newActionsList = history.get(player);
        if (newActionsList != null) {
            newActionsList.add(action);
            return;
        }
        final List<Action> actionsList = new ArrayList<>();
        actionsList.add(action);
        history.put(player, actionsList);
    }

    public static void addActionInStageHistory(TableSettings tableSettings, PlayerEntity player) {
        addActionInHistory(tableSettings.getStageHistory(), player);
    }

    public static void addActionInFullHistory(TableSettings tableSettings, PlayerEntity player) {
        addActionInHistory(tableSettings.getFullHistory(), player);
    }

}
