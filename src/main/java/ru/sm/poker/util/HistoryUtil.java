package ru.sm.poker.util;

import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;

import java.util.ArrayList;
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
        return sumBets(getCountActions(playerDTO, holdemRoundSettingsDTO.getFullHistory()));
    }


    public static long sumStageHistoryBets(HoldemRoundSettingsDTO holdemRoundSettingsDTO, PlayerDTO playerDTO) {
        return sumBets(getCountActions(playerDTO, holdemRoundSettingsDTO.getStageHistory()));
    }

    private static List<CountAction> getCountActions(PlayerDTO playerDTO, Map<PlayerDTO, List<CountAction>> stageHistory) {
        if (stageHistory == null) {
            throw new RuntimeException("cannot find history for player " + playerDTO.getName());
        }
        final List<CountAction> countActions = stageHistory.get(playerDTO);

        if (countActions == null) {
            throw new RuntimeException("cannot find actions for player:" + playerDTO.getName());
        }
        return countActions;
    }

    private static long sumBets(List<CountAction> actions) {
        long sum = 0;
        for (CountAction ca : actions) {
            sum += ca.getCount();
        }
        return sum;
    }


    public static long sumAllHistoryBetsWithNewAction(HoldemRoundSettingsDTO holdemRoundSettingsDTO, PlayerDTO playerDTO, CountAction countAction) {
        final Map<PlayerDTO, List<CountAction>> history = holdemRoundSettingsDTO.getStageHistory();
        if (history == null) {
            throw new RuntimeException("cannot find history for player:" + playerDTO.getName());
        }
        final List<CountAction> countActions = history.get(playerDTO);
        if (countAction == null) {
            throw new RuntimeException("cannot find actions for player:" + playerDTO.getName());
        }
        long sum = countAction.getCount();

        if (countActions != null) {
            for (CountAction ca : countActions) {
                sum += ca.getCount();
            }
        }
        return sum;
    }


    public static void addActionInHistory(HoldemRoundSettingsDTO holdemRoundSettingsDTO, PlayerDTO playerDTO, CountAction action) {
        final Map<PlayerDTO, List<CountAction>> history = holdemRoundSettingsDTO.getStageHistory();
        if (history == null) {
            throw new RuntimeException("cannot find history");
        }

        final List<CountAction> newActionsList = history.get(playerDTO);
        if (newActionsList != null) {
            newActionsList.add(action);
            return;
        }

        final List<CountAction> actionsList = new ArrayList<>();
        actionsList.add(action);
        history.put(playerDTO, actionsList);
    }

    public static void addActionInHistory(HoldemRoundSettingsDTO holdemRoundSettingsDTO, PlayerDTO playerDTO) {
        if (playerDTO.getAction() != null && playerDTO.getAction() instanceof CountAction) {
            addActionInHistory(holdemRoundSettingsDTO, playerDTO, (CountAction) playerDTO.getAction());
        }
    }

    public static void unionHistory(Map<PlayerDTO, List<CountAction>> firstHistory, Map<PlayerDTO, List<CountAction>> secondHistory) {
        if (firstHistory == null || secondHistory == null) {
            throw new RuntimeException("histories cannot be null");
        }
        firstHistory.forEach((key, value) -> {
            final List<CountAction> countActions = secondHistory.get(key);
            if (countActions != null) {
                countActions.addAll(value);
            }
        });
    }
}
