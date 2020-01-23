package ru.sm.poker.util;

import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.sm.poker.util.PlayerUtil.getPlayersInGame;

public class HistoryUtil {


    public static long allPreviousBetsEqualsLastBetWithNewAction(RoundSettingsDTO roundSettingsDTO, Player player, CountAction countAction) {
        return sumAllHistoryBets(roundSettingsDTO, player) + countAction.getCount();
    }

    public static boolean allPreviousBetsEqualsLastBet(RoundSettingsDTO roundSettingsDTO, Player player) {
        return sumAllHistoryBets(roundSettingsDTO, player) == roundSettingsDTO.getLastBet();
    }

    public static boolean allPlayersInGameHaveSameCountOfBet(RoundSettingsDTO roundSettingsDTO) {
        return getPlayersInGame(roundSettingsDTO.getPlayers()).stream()
                .noneMatch(player -> sumAllHistoryBets(roundSettingsDTO, player) != roundSettingsDTO.getLastBet());
    }


    public static long sumAllHistoryBets(RoundSettingsDTO roundSettingsDTO, Player player) {
        final Map<Player, List<CountAction>> history = roundSettingsDTO.getHistory();
        final List<CountAction> countActions = history.get(player);
        long summ = 0;
        if (countActions != null) {
            for (CountAction ca : countActions) {
                summ += ca.getCount();
            }
        }
        return summ;
    }


    public static long sumAllHistoryBetsWithNewAction(RoundSettingsDTO roundSettingsDTO, Player player, CountAction countAction) {
        final Map<Player, List<CountAction>> history = roundSettingsDTO.getHistory();
        final List<CountAction> countActions = history.get(player);
        long summ = countAction.getCount();

        if (countActions != null) {
            for (CountAction ca : countActions) {
                summ += ca.getCount();
            }
        }
        return summ;
    }

    public static void addActionInHistory(RoundSettingsDTO roundSettingsDTO, Player player) {
        final Map<Player, List<CountAction>> history = roundSettingsDTO.getHistory();

        final List<CountAction> newActionsList = history.get(player);
        if (newActionsList != null) {
            if (player.getAction() instanceof CountAction) {
                newActionsList.add((CountAction) player.getAction());
            }
            return;
        }

        final List<CountAction> actionsList = new ArrayList<>();
        if (player.getAction() instanceof CountAction) {
            actionsList.add((CountAction) player.getAction());
        }
        history.put(player, actionsList);
    }

}
