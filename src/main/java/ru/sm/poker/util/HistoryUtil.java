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
        final List<PlayerDTO> playersInGame = getPlayersInGame(holdemRoundSettingsDTO.getPlayerDTOS());

        final List<PlayerDTO> playerDTOS = playersInGame.stream()
                .filter(player -> player.getChipsCount() > 0)
                .collect(Collectors.toList());

        return playerDTOS.stream()
                .noneMatch(player -> sumAllHistoryBets(holdemRoundSettingsDTO, player) != holdemRoundSettingsDTO.getLastBet());
    }


    public static long sumAllHistoryBets(HoldemRoundSettingsDTO holdemRoundSettingsDTO, PlayerDTO playerDTO) {
        final Map<PlayerDTO, List<CountAction>> history = holdemRoundSettingsDTO.getHistory();
        final List<CountAction> countActions = history.get(playerDTO);
        long summ = 0;
        if (countActions != null) {
            for (CountAction ca : countActions) {
                summ += ca.getCount();
            }
        }
        System.out.println(playerDTO.getName() + " : " + summ);
        return summ;
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

    public static void addActionInHistory(HoldemRoundSettingsDTO holdemRoundSettingsDTO, PlayerDTO playerDTO) {
        final Map<PlayerDTO, List<CountAction>> history = holdemRoundSettingsDTO.getHistory();

        final List<CountAction> newActionsList = history.get(playerDTO);
        if (newActionsList != null) {
            if (playerDTO.getAction() instanceof CountAction) {
                newActionsList.add((CountAction) playerDTO.getAction());
            }
            return;
        }

        final List<CountAction> actionsList = new ArrayList<>();
        if (playerDTO.getAction() instanceof CountAction) {
            actionsList.add((CountAction) playerDTO.getAction());
        }
        history.put(playerDTO, actionsList);
    }

}
