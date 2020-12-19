package ru.smn.poker.util;

import ru.smn.poker.action.Action;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.stream.PlayerPredicates;

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
        final List<PlayerEntity> playersInGame = PlayerUtil.getPlayersInGame(roundSettings.getPlayers());

        final List<PlayerEntity> players = playersInGame.stream()
                .filter(PlayerPredicates.playerFolded().negate())
                .filter(PlayerPredicates.playerHasChips())
                .collect(Collectors.toList());

        return players.stream()
                .noneMatch(player -> sumStageHistoryBets(roundSettings, player) != roundSettings.getLastBet());
    }


    public static long sumRoundHistoryBets(RoundSettings roundSettings, PlayerEntity player) {
        final Map<PlayerEntity, List<Action>> fullHistory = roundSettings.getFullHistory();
        final List<Action> countActions = fullHistory.get(player);
        return sumBets(countActions);
    }


    public static long sumStageHistoryBets(RoundSettings roundSettings, PlayerEntity player) {
        final Map<PlayerEntity, List<Action>> history = roundSettings.getStageHistory();
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


    public static long sumAllHistoryBetsWithNewAction(RoundSettings roundSettings, PlayerEntity player, CountAction countAction) {
        final Map<PlayerEntity, List<Action>> history = roundSettings.getStageHistory();
        final List<Action> countActions = history.get(player);
        return sumBets(countActions) + countAction.getCount();
    }


    public static void addActionInHistory(RoundSettings roundSettings, PlayerEntity player, Action action) {
        final Map<PlayerEntity, List<Action>> history = roundSettings.getStageHistory();
        final List<Action> newActionsList = history.get(player);
        if (newActionsList != null) {
            newActionsList.add(action);
            return;
        }

        final List<Action> actionsList = new ArrayList<>();
        actionsList.add(action);
        history.put(player, actionsList);
    }

    public static void addActionInHistory(RoundSettings roundSettings, PlayerEntity player) {
        addActionInHistory(roundSettings, player, player.getAction());
    }

    public static Map<PlayerEntity, List<Action>> unionHistory(
            Map<PlayerEntity, List<Action>> firstHistory,
            Map<PlayerEntity, List<Action>> secondHistory
    ) {
        final Map<PlayerEntity, List<Action>> unionActions = new HashMap<>(firstHistory);

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
