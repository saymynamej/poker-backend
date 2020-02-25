package ru.sm.poker.util;

import ru.sm.poker.action.Action;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.sm.poker.util.PlayerUtil.getPlayersInGame;

public class HistoryUtil {


    public static boolean allPlayersInGameHaveSameCountOfBet(HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        final List<PlayerDTO> playersInGame = getPlayersInGame(holdemRoundSettingsDTO.getPlayers());

        final List<PlayerDTO> playerDTOS = playersInGame.stream()
                .filter(player -> player.getChipsCount() > 0)
                .collect(Collectors.toList());

        return playerDTOS.stream()
                .noneMatch(player -> sumStageHistoryBets(holdemRoundSettingsDTO, player) != holdemRoundSettingsDTO.getLastBet());
    }


    public static long sumRoundHistoryBets(HoldemRoundSettingsDTO holdemRoundSettingsDTO, PlayerDTO playerDTO) {
        final Map<PlayerDTO, List<Action>> fullHistory = holdemRoundSettingsDTO.getFullHistory();
        final List<Action> countActions = fullHistory.get(playerDTO);
        long sum = 0;
        if (countActions != null) {
            sum = sumBets(countActions);
        }
        return sum;
    }


    public static long sumStageHistoryBets(HoldemRoundSettingsDTO holdemRoundSettingsDTO, PlayerDTO playerDTO) {
        final Map<PlayerDTO, List<Action>> history = holdemRoundSettingsDTO.getStageHistory();
        final List<Action> countActions = history.get(playerDTO);
        long sum = 0;
        if (countActions != null) {
            sum = sumBets(countActions);
        }
        return sum;
    }

    private static long sumBets(List<Action> actions) {
        long sum = 0;
        for (Action action : actions) {
            if (action instanceof CountAction) {
                sum += ((CountAction) action).getCount();
            }
        }
        return sum;
    }


    public static long sumAllHistoryBetsWithNewAction(HoldemRoundSettingsDTO holdemRoundSettingsDTO, PlayerDTO playerDTO, CountAction countAction) {
        final Map<PlayerDTO, List<Action>> history = holdemRoundSettingsDTO.getStageHistory();
        final List<Action> actions = history.get(playerDTO);
        return countAction.getCount() + sumBets(actions);
    }


    public static void addActionInHistory(HoldemRoundSettingsDTO holdemRoundSettingsDTO, PlayerDTO playerDTO, CountAction action) {
        final Map<PlayerDTO, List<Action>> history = holdemRoundSettingsDTO.getStageHistory();

        final List<Action> newActionsList = history.get(playerDTO);
        if (newActionsList != null) {
            newActionsList.add(action);
            return;
        }

        final List<Action> actionsList = new ArrayList<>();
        actionsList.add(action);
        history.put(playerDTO, actionsList);
    }

    public static void addActionInHistory(HoldemRoundSettingsDTO holdemRoundSettingsDTO, PlayerDTO playerDTO) {
        if (playerDTO.getAction() instanceof CountAction) {
            addActionInHistory(holdemRoundSettingsDTO, playerDTO, (CountAction) playerDTO.getAction());
        }
    }

    public static Map<PlayerDTO, List<Action>> unionHistory(Map<PlayerDTO, List<Action>> firstHistory, Map<PlayerDTO, List<Action>> secondHistory) {
        final Map<PlayerDTO, List<Action>> unionActions = new HashMap<>(firstHistory);

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
