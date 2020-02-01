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
                .noneMatch(player -> sumAllHistoryBets(holdemRoundSettingsDTO, player) != holdemRoundSettingsDTO.getLastBet());
    }


    public static long sumAllHistoryBets(HoldemRoundSettingsDTO holdemRoundSettingsDTO, PlayerDTO playerDTO) {
        final Map<PlayerDTO, List<CountAction>> history = holdemRoundSettingsDTO.getHistory();
        final List<CountAction> countActions = history.get(playerDTO);
        long sum = 0;
        if (countActions != null) {
            for (CountAction ca : countActions) {
                sum += ca.getCount();
            }
        }
        return sum;
    }


    public static long sumAllHistoryBetsWithNewAction(HoldemRoundSettingsDTO holdemRoundSettingsDTO, PlayerDTO playerDTO, CountAction countAction) {
        final Map<PlayerDTO, List<CountAction>> history = holdemRoundSettingsDTO.getHistory();
        final List<CountAction> countActions = history.get(playerDTO);
        long summ = countAction.getCount();

        if (countActions != null) {
            for (CountAction ca : countActions) {
                summ += ca.getCount();
            }
        }
        return summ;
    }


    public static void addActionInHistory(HoldemRoundSettingsDTO holdemRoundSettingsDTO, PlayerDTO playerDTO, CountAction action) {
        final Map<PlayerDTO, List<CountAction>> history = holdemRoundSettingsDTO.getHistory();

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
        if (playerDTO.getAction() instanceof CountAction) {
            addActionInHistory(holdemRoundSettingsDTO, playerDTO, (CountAction) playerDTO.getAction());
        }
    }

}
