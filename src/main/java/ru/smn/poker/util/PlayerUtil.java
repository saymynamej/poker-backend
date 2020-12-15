package ru.smn.poker.util;

import ru.smn.poker.dto.Bot;
import ru.smn.poker.entities.ChipsCountEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.entities.PlayerSettingsEntity;
import ru.smn.poker.enums.RoleType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerUtil {



    public static PlayerEntity getPlayerEntityByRole(List<PlayerEntity> playerEntities, RoleType roleType){
        return playerEntities.stream()
                .filter(playerEntity -> playerEntity.getSettings().getRoleType() == roleType)
                .findFirst()
                .orElse(null);
    }

    public static PlayerEntity getActivePlayer(List<PlayerEntity> playerEntities){
        return playerEntities.stream()
                .filter(playerEntity -> playerEntity.getSettings().isActive())
                .findFirst()
                .orElse(null);
    }

    public static List<PlayerEntity> copies(List<PlayerEntity> players) {
        final List<PlayerEntity> copyPlayers = new ArrayList<>();
        players.forEach(player -> copyPlayers.add(player.copy()));
        return copyPlayers;
    }

    public static Bot getDefaultBotForHoldem(String playerName){
        return Bot.builder()
                .name(playerName)
                .chipsCount(5000L)
                .build();
    }

    public static PlayerEntity getDefaultPlayerForHoldem(String playerName) {
        return getDefaultPlayerForHoldem(playerName, 5000L);
    }

    public static PlayerEntity getDefaultPlayerForHoldem(String playerName, long chipsCount) {
        return PlayerEntity.builder()
                .name(playerName)
                .settings(PlayerSettingsEntity.builder()
                        .timeBank(60L)
                        .chipsCount(ChipsCountEntity.builder()
                                .count(5000L)
                                .build())
                        .build())
                .build();
    }

    public static List<String> getNamesOfPlayersInGame(List<PlayerEntity> players){
        return getPlayersInGame(players)
                .stream()
                .filter(StreamUtil.playerFolded().negate())
                .map(PlayerEntity::getName)
                .collect(Collectors.toList());
    }

    public static List<PlayerEntity> getPlayerWhichMayPlay(List<PlayerEntity> players) {
        return players.stream()
                .filter(StreamUtil.playerInGame())
                .filter(StreamUtil.playerHasChips())
                .collect(Collectors.toList());
    }

    public static List<PlayerEntity> getNotFoldedPlayers(List<PlayerEntity> players) {
        return players.stream()
                .filter(PlayerEntity::isNotFolded)
                .collect(Collectors.toList());
    }

    public static List<PlayerEntity> getPlayersInGame(List<PlayerEntity> players) {
        return players.stream()
                .filter(StreamUtil.playerInGame())
                .filter(StreamUtil.playerFolded().negate())
                .collect(Collectors.toList());
    }

}
