package ru.smn.poker.converter;

import ru.smn.poker.dto.TableSettingsDTO;
import ru.smn.poker.entities.TableEntity;
import ru.smn.poker.enums.GameType;
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
                .gameId(tableSettings.getTableId())
                .gameName(tableSettings.getTableName())
                .isFinished(tableSettings.isFinished())
                .lastBet(tableSettings.getLastBet())
                .players(PlayerConverter.toDTOs(tableSettings.getPlayers()))
                .river(tableSettings.getRiver())
                .stageType(tableSettings.getStageType())
                .tern(tableSettings.getTern())
                .smallBlind(PlayerConverter.toDTO(tableSettings.getSmallBlind()))
                .build();

    }

    public static TableEntity toEntity(TableSettings tableSettings){
        return TableEntity.builder()
                .players(tableSettings.getPlayers())
                .gameType(GameType.HOLDEM_HU)
                .name(tableSettings.getTableName())
                .id(tableSettings.getTableId())
                .build();
    }
}
