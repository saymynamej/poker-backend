package ru.smn.poker.converter;

import ru.smn.poker.dto.HoldemRoundSettingsDTO;
import ru.smn.poker.entities.GameEntity;
import ru.smn.poker.entities.RoundEntity;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.game.TableSettings;

import java.util.List;

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

    public static GameEntity toEntity(TableSettings tableSettings) {
        final GameEntity gameEntity = GameEntity.builder()
                .id(tableSettings.getGameId())
                .players(tableSettings.getPlayers())
                .name(tableSettings.getGameName())
                .gameType(GameType.HOLDEM_FULL)
                .build();
        return gameEntity;
    }

    private static long getRoundId(List<RoundEntity> prevRounds) {
        long roundId = 0;
        if (prevRounds.size() > 0) {
            final RoundEntity lastRound = prevRounds.get(prevRounds.size() - 1);
            if (!lastRound.isFinished()) {
                roundId = lastRound.getId();
            }
        }
        return roundId;
    }
}
