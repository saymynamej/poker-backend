package ru.sm.poker.util;

import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.sm.poker.util.PlayerUtil.getPlayersInGame;

public class HistoryUtil {


    public static boolean allPlayersInGameHaveSameCountOfBet(HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        final List<Player> playersInGame = getPlayersInGame(holdemRoundSettingsDTO.getPlayers());

        final List<Player> players = playersInGame.stream()
                .filter(player -> player.getChipsCount() > 0)
                .collect(Collectors.toList());

        return players.stream()
                .noneMatch(player -> sumAllHistoryBets(holdemRoundSettingsDTO, player) != holdemRoundSettingsDTO.getLastBet());
    }


    public static long sumAllHistoryBets(HoldemRoundSettingsDTO holdemRoundSettingsDTO, Player player) {
        final Map<Player, List<CountAction>> history = holdemRoundSettingsDTO.getHistory();
        final List<CountAction> countActions = history.get(player);
        long summ = 0;
        if (countActions != null) {
            for (CountAction ca : countActions) {
                summ += ca.getCount();
            }
        }
        System.out.println(player.getName() + " : " + summ);
        return summ;
    }


    public static long sumAllHistoryBetsWithNewAction(HoldemRoundSettingsDTO holdemRoundSettingsDTO, Player player, CountAction countAction) {
        final Map<Player, List<CountAction>> history = holdemRoundSettingsDTO.getHistory();
        final List<CountAction> countActions = history.get(player);
        long summ = countAction.getCount();

        if (countActions != null) {
            for (CountAction ca : countActions) {
                summ += ca.getCount();
            }
        }
        return summ;
    }

    public static void addActionInHistory(HoldemRoundSettingsDTO holdemRoundSettingsDTO, Player player) {
        final Map<Player, List<CountAction>> history = holdemRoundSettingsDTO.getHistory();

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
