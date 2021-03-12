package ru.smn.poker.converter;

import ru.smn.poker.dto.HoldemRoundSettingsDTO;
import ru.smn.poker.game.TableSettings;

public class RoundSettingsConverter {

    public static HoldemRoundSettingsDTO toDTO(TableSettings tableSettings) {
        return HoldemRoundSettingsDTO.builder()
                .activePlayer(PlayerConverter.toDTO(tableSettings.getActivePlayer()))
                .bank(tableSettings.getBank())
                .bigBlind(PlayerConverter.toDTO(tableSettings.getBigBlind()))
                .bigBlindBet(tableSettings.getBigBlindBet())
                .smallBlindBet(tableSettings.getSmallBlindBet())
                .button(PlayerConverter.toDTO(tableSettings.getButton()))
                .flop(tableSettings.getFlop())
                .gameId(tableSettings.getGameId())
                .gameName(tableSettings.getGameName())
                .isFinished(tableSettings.isFinished())
                .lastBet(tableSettings.getLastBet())
                .players(PlayerConverter.toDTOs(tableSettings.getPlayers()))
                .river(tableSettings.getRiver())
                .stageType(tableSettings.getStageType())
                .tern(tableSettings.getTern())
                .smallBlind(PlayerConverter.toDTO(tableSettings.getSmallBlind()))
                .build();

    }
}
