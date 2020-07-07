package ru.sm.poker.util;

import ru.sm.poker.dto.Bot;
import ru.sm.poker.dto.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.sm.poker.util.StreamUtil.playerFolded;
import static ru.sm.poker.util.StreamUtil.playerInGame;

public class PlayerUtil {

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
                .filter(playerFolded().negate())
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
                .filter(playerInGame())
                .filter(playerFolded().negate())
                .collect(Collectors.toList());
    }

}
