package ru.sm.poker.util;

import ru.sm.poker.action.Action;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.game.holdem.HoldemRound;

import java.util.*;
import java.util.stream.Collectors;

import static ru.sm.poker.util.PlayerUtil.getPlayersInGame;
import static ru.sm.poker.util.StreamUtil.*;

public class HistoryUtil {


    public static boolean canMoveNextAndStageRiver(HoldemRoundSettingsDTO holdemRoundSettingsDTO){
        return canMoveNext(holdemRoundSettingsDTO) && holdemRoundSettingsDTO.getStageType() == StageType.RIVER;
    }

    public static boolean canMoveNext(HoldemRoundSettingsDTO holdemRoundSettings) {
        return allPlayersInGameHaveSameCountOfBet(holdemRoundSettings) && holdemRoundSettings.lastBetIsNotZero() || holdemRoundSettings.allPlayersCheck();
    }

    public static boolean allPlayersInGameHaveSameCountOfBet(HoldemRoundSettingsDTO holdemRoundSettings) {
        final List<PlayerDTO> playersInGame = getPlayersInGame(holdemRoundSettings.getPlayers());

        final List<PlayerDTO> players = playersInGame.stream()
                .filter(playerFolded().negate())
                .filter(playerHasChips())
                .collect(Collectors.toList());

        return players.stream()
                .noneMatch(player -> sumStageHistoryBets(holdemRoundSettings, player) != holdemRoundSettings.getLastBet());
    }


    public static long sumRoundHistoryBets(HoldemRoundSettingsDTO holdemRoundSettings, PlayerDTO player) {
        final Map<PlayerDTO, List<Action>> fullHistory = holdemRoundSettings.getFullHistory();
        final List<Action> countActions = fullHistory.get(player);
        return sumBets(countActions);
    }


    public static long sumStageHistoryBets(HoldemRoundSettingsDTO holdemRoundSettings, PlayerDTO player) {
        final Map<PlayerDTO, List<Action>> history = holdemRoundSettings.getStageHistory();
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


    public static long sumAllHistoryBetsWithNewAction(HoldemRoundSettingsDTO holdemRoundSettings, PlayerDTO player, CountAction countAction) {
        final Map<PlayerDTO, List<Action>> history = holdemRoundSettings.getStageHistory();
        final List<Action> countActions = history.get(player);
        return sumBets(countActions) + countAction.getCount();
    }


    public static void addActionInHistory(HoldemRoundSettingsDTO holdemRoundSettings, PlayerDTO player, Action action) {
        final Map<PlayerDTO, List<Action>> history = holdemRoundSettings.getStageHistory();
        final List<Action> newActionsList = history.get(player);
        if (newActionsList != null) {
            newActionsList.add(action);
            return;
        }

        final List<Action> actionsList = new ArrayList<>();
        actionsList.add(action);
        history.put(player, actionsList);
    }

    public static void addActionInHistory(HoldemRoundSettingsDTO holdemRoundSettings, PlayerDTO player) {
        addActionInHistory(holdemRoundSettings, player, player.getAction());
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
