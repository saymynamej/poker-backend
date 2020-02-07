package ru.sm.poker.util;

import org.apache.commons.lang3.tuple.ImmutablePair;
import ru.sm.poker.action.holdem.Wait;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.game.Game;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RoundSettingsUtil {

    public static HoldemRoundSettingsDTO copyWithNewPlayersAndReloadAction(HoldemRoundSettingsDTO holdemRoundSettingsDTO, List<PlayerDTO> playerDTOS) {
        final String gameName = playerDTOS.get(0)
                .getGameName();

        playerDTOS.forEach(player -> player.setAction(new Wait()));

        return copy(holdemRoundSettingsDTO, playerDTOS);
    }

    public static HoldemRoundSettingsDTO copyWithSecureCard(HoldemRoundSettingsDTO holdemRoundSettingsDTO, List<String> filters) {
        final List<PlayerDTO> playersWithSecureCards = PlayerUtil.copies(holdemRoundSettingsDTO.getPlayers());
        playersWithSecureCards.forEach(player -> {
            if (!filters.contains(player.getName())) {
                player.addCards(Collections.emptyList());
            }
        });

        return copy(holdemRoundSettingsDTO, playersWithSecureCards);
    }

    public static HoldemRoundSettingsDTO copy(HoldemRoundSettingsDTO holdemRoundSettingsDTO, List<PlayerDTO> playerDTOS) {
        return HoldemRoundSettingsDTO.builder()
                .gameName(holdemRoundSettingsDTO.getGameName())
                .bank(holdemRoundSettingsDTO.getBank())
                .smallBlindBet(holdemRoundSettingsDTO.getSmallBlindBet())
                .bigBlindBet(holdemRoundSettingsDTO.getBigBlindBet())
                .bigBlind(holdemRoundSettingsDTO.getBigBlind())
                .smallBlind(holdemRoundSettingsDTO.getSmallBlind())
                .button(holdemRoundSettingsDTO.getButton())
                .players(playerDTOS)
                .stageType(holdemRoundSettingsDTO.getStageType())
                .lastBet(holdemRoundSettingsDTO.getLastBet())
                .activePlayerDTO(holdemRoundSettingsDTO.getActivePlayerDTO())
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
