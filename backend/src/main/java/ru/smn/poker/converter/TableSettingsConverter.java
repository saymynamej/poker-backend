package ru.smn.poker.converter;

import ru.smn.poker.dto.TableSettingsDTO;
import ru.smn.poker.game.TableSettings;

public class TableSettingsConverter {

    public static TableSettingsDTO toDTO(TableSettings tableSettings) {
        return TableSettingsDTO.builder()
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
