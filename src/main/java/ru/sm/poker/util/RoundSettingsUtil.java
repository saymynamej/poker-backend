package ru.sm.poker.util;

import org.apache.commons.lang3.tuple.ImmutablePair;
import ru.sm.poker.action.holdem.Wait;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.game.Game;
import ru.sm.poker.model.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RoundSettingsUtil {

    public static RoundSettingsDTO copyWithNewPlayersAndReloadAction(RoundSettingsDTO roundSettingsDTO, List<Player> players) {
        final String gameName = players.get(0)
                .getGameName();

        players.forEach(player -> player.setAction(new Wait()));

        return copy(roundSettingsDTO, players);
    }


    public static RoundSettingsDTO copyWithSecureCard(RoundSettingsDTO roundSettingsDTO, List<String> filters) {
        final List<Player> playersWithSecureCards = PlayerUtil.copies(roundSettingsDTO.getPlayers());
        playersWithSecureCards.forEach(player -> {
            if (!filters.contains(player.getName())) {
                player.addCards(Collections.emptyList());
            }
        });

        return copy(roundSettingsDTO, playersWithSecureCards);
    }


    public static RoundSettingsDTO copy(RoundSettingsDTO roundSettingsDTO, List<Player> players) {
        return RoundSettingsDTO.builder()
                .gameName(roundSettingsDTO.getGameName())
                .bank(roundSettingsDTO.getBank())
                .smallBlindBet(roundSettingsDTO.getSmallBlindBet())
                .bigBlindBet(roundSettingsDTO.getBigBlindBet())
                .bigBlind(roundSettingsDTO.getBigBlind())
                .smallBlind(roundSettingsDTO.getSmallBlind())
                .button(roundSettingsDTO.getButton())
                .players(players)
                .stageType(roundSettingsDTO.getStageType())
                .lastBet(roundSettingsDTO.getLastBet())
                .activePlayer(roundSettingsDTO.getActivePlayer())
                .flop(roundSettingsDTO.getFlop())
                .river(roundSettingsDTO.getRiver())
                .tern(roundSettingsDTO.getTern())
                .build();
    }


    public static ImmutablePair<Game, RoundSettingsDTO> getGameAndRoundSettings(Game game, Optional<RoundSettingsDTO> roundSettingsDTO) {
        if (game == null || roundSettingsDTO.isEmpty()) {
            throw new RuntimeException("cannot find game or roundsettings");
        }
        return ImmutablePair.of(game, roundSettingsDTO.get());
    }
}
