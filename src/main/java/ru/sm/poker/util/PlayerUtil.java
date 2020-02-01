package ru.sm.poker.util;

import ru.sm.poker.action.CountAction;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.enums.StateType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerUtil {


    public static List<PlayerDTO> copies(List<PlayerDTO> playerDTOS) {
        final List<PlayerDTO> copyPlayerDTOS = new ArrayList<>();
        playerDTOS.forEach(player -> copyPlayerDTOS.add(player.copy()));
        return copyPlayerDTOS;
    }

    public static PlayerDTO getDefaultPlayerForHoldem(String playerName) {
        return PlayerDTO.builder()
                .name(playerName)
                .timeBank(200L)
                .chipsCount(5000L)
                .build();
    }

    public static List<PlayerDTO> getPlayersInGame(List<PlayerDTO> playerDTOS) {
        return playerDTOS.stream()
                .filter(player -> player.getAction() != null &&
                        player.getAction().getActionType() != ActionType.FOLD &&
                        player.getRoleType() != null &&
                        player.getStateType() != StateType.AFK &&
                        player.getStateType() != StateType.LEAVE &&
                        player.getCards() != null
                )
                .collect(Collectors.toList());
    }

    public static boolean checkPlayerHasEnoughChips(PlayerDTO playerDTO, CountAction countAction) {
        return playerDTO.getChipsCount() >= countAction.getCount();
    }

}
