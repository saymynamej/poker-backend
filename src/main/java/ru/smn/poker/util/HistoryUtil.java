package ru.smn.poker.util;

import ru.smn.poker.action.Action;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.dto.Player;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.enums.StageType;

import java.util.*;
import java.util.stream.Collectors;

public class HistoryUtil {


    public static boolean canMoveNextAndStageRiver(RoundSettings roundSettings){
        return canMoveNext(roundSettings) && roundSettings.getStageType() == StageType.RIVER;
    }

    public static boolean canMoveNext(RoundSettings roundSettings) {
        return allPlayersInGameHaveSameCountOfBet(roundSettings) && roundSettings.lastBetIsNotZero() || roundSettings.allPlayersCheck();
    }

    public static boolean allPlayersInGameHaveSameCountOfBet(RoundSettings roundSettings) {
        final List<Player> playersInGame = PlayerUtil.getPlayersInGame(roundSettings.getPlayers());

        final List<Player> players = playersInGame.stream()
                .filter(StreamUtil.playerFolded().negate())
                .filter(StreamUtil.playerHasChips())
                .collect(Collectors.toList());

        return players.stream()
                .noneMatch(player -> sumStageHistoryBets(roundSettings, player) != roundSettings.getLastBet());
    }


    public static long sumRoundHistoryBets(RoundSettings roundSettings, Player player) {
        final Map<Player, List<Action>> fullHistory = roundSettings.getFullHistory();
        final List<Action> countActions = fullHistory.get(player);
        return sumBets(countActions);
    }


    public static long sumStageHistoryBets(RoundSettings roundSettings, Player player) {
        final Map<Player, List<Action>> history = roundSettings.getStageHistory();
        final List<Action> countActions = history.get(player);
        return sumBets(countActions);
    }

    public static long sumBets(List<Action> actions) {
        if (actions == null) {
            return 0;
        }

        final Optional<Long> reduced = actions.stream()
                .filter(CountAction.class::isInstance)
                .map(action -> ((CountAction) action).getCount())
                .reduce(Long::sum);

        if (reduced.isPresent()) {
            return reduced.get();
        }
        return 0;
    }


    public static long sumAllHistoryBetsWithNewAction(RoundSettings roundSettings, Player player, CountAction countAction) {
        final Map<Player, List<Action>> history = roundSettings.getStageHistory();
        final List<Action> countActions = history.get(player);
        return sumBets(countActions) + countAction.getCount();
    }


    public static void addActionInHistory(RoundSettings roundSettings, Player player, Action action) {
        final Map<Player, List<Action>> history = roundSettings.getStageHistory();
        final List<Action> newActionsList = history.get(player);
        if (newActionsList != null) {
            newActionsList.add(action);
            return;
        }

        final List<Action> actionsList = new ArrayList<>();
        actionsList.add(action);
        history.put(player, actionsList);
    }

    public static void addActionInHistory(RoundSettings roundSettings, Player player) {
        addActionInHistory(roundSettings, player, player.getAction());
    }

    public static Map<Player, List<Action>> unionHistory(Map<Player, List<Action>> firstHistory, Map<Player, List<Action>> secondHistory) {
        final Map<Player, List<Action>> unionActions = new HashMap<>(firstHistory);

        secondHistory.forEach((key, value) -> {
            final List<Action> countActions = unionActions.get(key);
            if (countActions != null) {
                countActions.addAll(value);
            } else {
                unionActions.put(key, value);
            }
        });

        return unionActions;
    }
}
