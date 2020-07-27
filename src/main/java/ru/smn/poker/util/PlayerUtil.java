package ru.smn.poker.util;

import ru.smn.poker.dto.Bot;
import ru.smn.poker.dto.Player;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.RoleType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerUtil {



    public static PlayerEntity getPlayerEntityByRole(List<PlayerEntity> playerEntities, RoleType roleType){
        return playerEntities.stream()
                .filter(playerEntity -> playerEntity.getRoleType() == roleType)
                .findFirst()
                .orElse(null);
    }

    public static PlayerEntity getActivePlayer(List<PlayerEntity> playerEntities){
        return playerEntities.stream()
                .filter(PlayerEntity::isActive)
                .findFirst()
                .orElse(null);
    }

    public static List<Player> copies(List<Player> players) {
        final List<Player> copyPlayers = new ArrayList<>();
        players.forEach(player -> copyPlayers.add(player.copy()));
        return copyPlayers;
    }

    public static Bot getDefaultBotForHoldem(String playerName){
        return Bot.builder()
                .name(playerName)
                .chipsCount(5000L)
                .build();
    }

    public static Player getDefaultPlayerForHoldem(String playerName) {
        return getDefaultPlayerForHoldem(playerName, 5000L);
    }

    public static Player getDefaultPlayerForHoldem(String playerName, long chipsCount) {
        return Player.builder()
                .name(playerName)
                .timeBank(60L)
                .chipsCount(chipsCount)
                .build();
    }

    public static List<String> getNamesOfPlayersInGame(List<Player> players){
        return getPlayersInGame(players)
                .stream()
                .filter(StreamUtil.playerFolded().negate())
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    public static List<Player> getPlayerWhichMayPlay(List<Player> players) {
        return players.stream()
                .filter(StreamUtil.playerInGame())
                .filter(StreamUtil.playerHasChips())
                .collect(Collectors.toList());
    }

    public static List<Player> getNotFoldedPlayers(List<Player> players) {
        return players.stream()
                .filter(Player::isNotFolded)
                .collect(Collectors.toList());
    }

    public static List<Player> getPlayersInGame(List<Player> players) {
        return players.stream()
                .filter(StreamUtil.playerInGame())
                .filter(StreamUtil.playerFolded().negate())
                .collect(Collectors.toList());
    }

}
