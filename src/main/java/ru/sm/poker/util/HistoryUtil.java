package ru.sm.poker.util;

import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HistoryUtil {


    public static long allActionsWithNewActionEqualsLastBet(RoundSettingsDTO roundSettingsDTO, Player player, CountAction countAction) {
        return sumAllHistoryBetsWithBlinds(roundSettingsDTO, player) + countAction.getCount();
    }


    public static boolean allActionsEqualsLastBetPreflop(RoundSettingsDTO roundSettingsDTO, Player player) {
        return sumAllHistoryBetsWithBlinds(roundSettingsDTO, player) == roundSettingsDTO.getLastBet();
    }

    public static boolean allActionsEqualsLastBetPostFlop(RoundSettingsDTO roundSettingsDTO, Player player) {
        return sumAllHistoryBetsWithoutBlinds(roundSettingsDTO, player) == roundSettingsDTO.getLastBet();
    }

    public static long sumAllHistoryBetsWithoutBlinds(RoundSettingsDTO roundSettingsDTO, Player player) {
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


    public static long sumAllHistoryBetsWithoutBlinds(RoundSettingsDTO roundSettingsDTO, Player player, CountAction countAction) {
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

    private static long sumAllHistoryBetsWithBlinds(RoundSettingsDTO roundSettingsDTO, Player player) {
        long summ = sumAllHistoryBetsWithoutBlinds(roundSettingsDTO, player);
        if (player.isBigBlind()) {
            summ += roundSettingsDTO.getBigBlindBet();
        }
        if (player.isSmallBlind()) {
            summ += roundSettingsDTO.getSmallBlindBet();
        }
        return summ;
    }


    public static Optional<CountAction> getLastAction(RoundSettingsDTO roundSettingsDTO, Player player) {
        final Map<Player, List<CountAction>> history = roundSettingsDTO.getHistory();
        final List<CountAction> countActions = history.get(player);
        if (countActions == null) {
            return Optional.empty();
        }
        return Optional.of(countActions.get(countActions.size() - 1));
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
