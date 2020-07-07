package ru.sm.poker.util;

import ru.sm.poker.action.Action;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;
import ru.sm.poker.enums.StageType;

import java.util.*;
import java.util.stream.Collectors;

import static ru.sm.poker.util.PlayerUtil.getPlayersInGame;
import static ru.sm.poker.util.StreamUtil.playerFolded;
import static ru.sm.poker.util.StreamUtil.playerHasChips;

public class HistoryUtil {


    public static boolean canMoveNextAndStageRiver(HoldemRoundSettings holdemRoundSettings){
        return canMoveNext(holdemRoundSettings) && holdemRoundSettings.getStageType() == StageType.RIVER;
    }

    public static boolean canMoveNext(HoldemRoundSettings holdemRoundSettings) {
        return allPlayersInGameHaveSameCountOfBet(holdemRoundSettings) && holdemRoundSettings.lastBetIsNotZero() || holdemRoundSettings.allPlayersCheck();
    }

    public static boolean allPlayersInGameHaveSameCountOfBet(HoldemRoundSettings holdemRoundSettings) {
        final List<Player> playersInGame = getPlayersInGame(holdemRoundSettings.getPlayers());

        final List<Player> players = playersInGame.stream()
                .filter(playerFolded().negate())
                .filter(playerHasChips())
                .collect(Collectors.toList());

        return players.stream()
                .noneMatch(player -> sumStageHistoryBets(holdemRoundSettings, player) != holdemRoundSettings.getLastBet());
    }


    public static long sumRoundHistoryBets(HoldemRoundSettings holdemRoundSettings, Player player) {
        final Map<Player, List<Action>> fullHistory = holdemRoundSettings.getFullHistory();
        final List<Action> countActions = fullHistory.get(player);
        return sumBets(countActions);
    }


    public static long sumStageHistoryBets(HoldemRoundSettings holdemRoundSettings, Player player) {
        final Map<Player, List<Action>> history = holdemRoundSettings.getStageHistory();
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


    public static long sumAllHistoryBetsWithNewAction(HoldemRoundSettings holdemRoundSettings, Player player, CountAction countAction) {
        final Map<Player, List<Action>> history = holdemRoundSettings.getStageHistory();
        final List<Action> countActions = history.get(player);
        return sumBets(countActions) + countAction.getCount();
    }


    public static void addActionInHistory(HoldemRoundSettings holdemRoundSettings, Player player, Action action) {
        final Map<Player, List<Action>> history = holdemRoundSettings.getStageHistory();
        final List<Action> newActionsList = history.get(player);
        if (newActionsList != null) {
            newActionsList.add(action);
            return;
        }

        final List<Action> actionsList = new ArrayList<>();
        actionsList.add(action);
        history.put(player, actionsList);
    }

    public static void addActionInHistory(HoldemRoundSettings holdemRoundSettings, Player player) {
        addActionInHistory(holdemRoundSettings, player, player.getAction());
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
