package ru.sm.poker.util;

import ru.sm.poker.action.Action;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.Player;

import java.util.*;
import java.util.stream.Collectors;

import static ru.sm.poker.util.PlayerUtil.getPlayersInGame;

public class HistoryUtil {

    public static boolean allPlayersInGameHaveSameCountOfBet(HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        final List<Player> playersInGame = getPlayersInGame(holdemRoundSettingsDTO.getPlayers());

        final List<Player> players = playersInGame.stream()
                .filter(StreamUtil.playerHasChips())
                .collect(Collectors.toList());

        return players.stream()
                .noneMatch(player -> sumStageHistoryBets(holdemRoundSettingsDTO, player) != holdemRoundSettingsDTO.getLastBet());
    }


    public static long sumRoundHistoryBets(HoldemRoundSettingsDTO holdemRoundSettingsDTO, Player player) {
        final Map<Player, List<Action>> fullHistory = holdemRoundSettingsDTO.getFullHistory();
        final List<Action> countActions = fullHistory.get(player);
        return sumBets(countActions);
    }


    public static long sumStageHistoryBets(HoldemRoundSettingsDTO holdemRoundSettingsDTO, Player player) {
        final Map<Player, List<Action>> history = holdemRoundSettingsDTO.getStageHistory();
        final List<Action> countActions = history.get(player);
        return sumBets(countActions);
    }

    private static long sumBets(List<Action> actions) {
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


    public static long sumAllHistoryBetsWithNewAction(HoldemRoundSettingsDTO holdemRoundSettingsDTO, Player player, CountAction countAction) {
        final Map<Player, List<Action>> history = holdemRoundSettingsDTO.getStageHistory();
        final List<Action> countActions = history.get(player);
        return sumBets(countActions) + countAction.getCount();
    }


    public static void addActionInHistory(HoldemRoundSettingsDTO holdemRoundSettingsDTO, Player player, Action action) {
        final Map<Player, List<Action>> history = holdemRoundSettingsDTO.getStageHistory();
        final List<Action> newActionsList = history.get(player);
        if (newActionsList != null) {
            newActionsList.add(action);
            return;
        }

        final List<Action> actionsList = new ArrayList<>();
        actionsList.add(action);
        history.put(player, actionsList);
    }

    public static void addActionInHistory(HoldemRoundSettingsDTO holdemRoundSettingsDTO, Player player) {
        addActionInHistory(holdemRoundSettingsDTO, player, player.getAction());
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
