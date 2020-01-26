package ru.sm.poker.util;

import org.apache.commons.lang3.tuple.ImmutablePair;
import ru.sm.poker.action.holdem.Wait;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.game.Game;
import ru.sm.poker.model.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RoundSettingsUtil {

    public static HoldemRoundSettingsDTO copyWithNewPlayersAndReloadAction(HoldemRoundSettingsDTO holdemRoundSettingsDTO, List<Player> players) {
        final String gameName = players.get(0)
                .getGameName();

        players.forEach(player -> player.setAction(new Wait()));

        return copy(holdemRoundSettingsDTO, players);
    }


    public static HoldemRoundSettingsDTO copyWithSecureCard(HoldemRoundSettingsDTO holdemRoundSettingsDTO, List<String> filters) {
        final List<Player> playersWithSecureCards = PlayerUtil.copies(holdemRoundSettingsDTO.getPlayers());
        playersWithSecureCards.forEach(player -> {
            if (!filters.contains(player.getName())) {
                player.addCards(Collections.emptyList());
            }
        });

        return copy(holdemRoundSettingsDTO, playersWithSecureCards);
    }


    public static HoldemRoundSettingsDTO copy(HoldemRoundSettingsDTO holdemRoundSettingsDTO, List<Player> players) {
        return HoldemRoundSettingsDTO.builder()
                .gameName(holdemRoundSettingsDTO.getGameName())
                .bank(holdemRoundSettingsDTO.getBank())
                .smallBlindBet(holdemRoundSettingsDTO.getSmallBlindBet())
                .bigBlindBet(holdemRoundSettingsDTO.getBigBlindBet())
                .bigBlind(holdemRoundSettingsDTO.getBigBlind())
                .smallBlind(holdemRoundSettingsDTO.getSmallBlind())
                .button(holdemRoundSettingsDTO.getButton())
                .players(players)
                .stageType(holdemRoundSettingsDTO.getStageType())
                .lastBet(holdemRoundSettingsDTO.getLastBet())
                .activePlayer(holdemRoundSettingsDTO.getActivePlayer())
                .flop(holdemRoundSettingsDTO.getFlop())
                .river(holdemRoundSettingsDTO.getRiver())
                .tern(holdemRoundSettingsDTO.getTern())
                .build();
    }


    public static ImmutablePair<Game, HoldemRoundSettingsDTO> getGameAndRoundSettings(Game game, Optional<HoldemRoundSettingsDTO> roundSettingsDTO) {
        if (game == null || roundSettingsDTO.isEmpty()) {
            throw new RuntimeException("cannot find game or roundsettings");
        }
        return ImmutablePair.of(game, roundSettingsDTO.get());
    }
}
