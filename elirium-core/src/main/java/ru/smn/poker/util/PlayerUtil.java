package ru.smn.poker.util;

import ru.smn.poker.entities.ChipsCountEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.entities.PlayerSettingsEntity;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.enums.StateType;
import ru.smn.poker.stream.PlayerPredicates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerUtil {

    public static List<PlayerEntity> copies(List<PlayerEntity> players) {
        final List<PlayerEntity> copyPlayers = new ArrayList<>();
        players.forEach(player -> copyPlayers.add(player.copy()));
        return copyPlayers;
    }

    public static PlayerEntity getDefaultPlayerForHoldem(String playerName) {
        return getDefaultPlayerForHoldem(playerName, 5000L);
    }

    public static PlayerEntity getDefaultPlayerForHoldem(String playerName, long chipsCount) {
        return PlayerEntity.builder()
                .name(playerName)
                .settings(Collections.singletonList(PlayerSettingsEntity.builder()
                        .timeBank(600000L)
                        .chipsCount(ChipsCountEntity.builder()
                                .count(5000L)
                                .build())
                        .build()))
                .build();
    }

    public static List<String> getNamesOfPlayersInGame(List<PlayerEntity> players){
        return getPlayersInAction(players)
                .stream()
                .filter(PlayerPredicates.playerFolded().negate())
                .map(PlayerEntity::getName)
                .collect(Collectors.toList());
    }

    public static List<PlayerEntity> getPlayerWhichMayPlay(List<PlayerEntity> players) {
        return players.stream()
                .filter(PlayerPredicates.playerInGame())
                .filter(PlayerPredicates.playerHasChips())
                .collect(Collectors.toList());
    }

    public static List<PlayerEntity> getPlayersInAction(List<PlayerEntity> players) {
        return players.stream()
                .filter(playerEntity -> playerEntity.getTableSettings().getStateType() == StateType.IN_GAME && playerEntity.getAction().getActionType() != ActionType.FOLD)
                .collect(Collectors.toList());
    }

}
