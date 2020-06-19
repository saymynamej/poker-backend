package ru.sm.poker.util;

import ru.sm.poker.dto.Bot;
import ru.sm.poker.dto.PlayerDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.sm.poker.util.StreamUtil.*;

public class PlayerUtil {

    public static List<PlayerDTO> copies(List<PlayerDTO> players) {
        final List<PlayerDTO> copyPlayers = new ArrayList<>();
        players.forEach(player -> copyPlayers.add(player.copy()));
        return copyPlayers;
    }

    public static Bot getDefaultBotForHoldem(String playerName){
        return Bot.builder().name(playerName)
                .chipsCount(5000L)
                .build();
    }

    public static PlayerDTO getDefaultPlayerForHoldem(String playerName) {
        return getDefaultPlayerForHoldem(playerName, 5000L);
    }

    public static PlayerDTO getDefaultPlayerForHoldem(String playerName, long chipsCount) {
        return PlayerDTO.builder()
                .name(playerName)
                .timeBank(60L)
                .chipsCount(chipsCount)
                .build();
    }

    public static List<String> getNamesOfPlayersInGame(List<PlayerDTO> players){
        return getPlayersInGame(players)
                .stream()
                .filter(playerFolded().negate())
                .map(PlayerDTO::getName)
                .collect(Collectors.toList());
    }

    public static List<PlayerDTO> getNotFoldedPlayers(List<PlayerDTO> players) {
        return players.stream()
                .filter(PlayerDTO::isNotFolded)
                .collect(Collectors.toList());
    }

    public static List<PlayerDTO> getPlayersInGame(List<PlayerDTO> players) {
        return players.stream()
                .filter(playerInGame())
                .filter(playerFolded().negate())
                .collect(Collectors.toList());
    }

}
