package ru.smn.poker.converter;

import ru.smn.poker.dto.HoldemRoundSettingsDTO;
import ru.smn.poker.entities.GameEntity;
import ru.smn.poker.entities.RoundEntity;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.game.RoundSettings;

import java.util.List;

public class RoundSettingsConverter {


    public static HoldemRoundSettingsDTO toDTO(RoundSettings holdemRoundSettings) {
        return HoldemRoundSettingsDTO.builder()
                .activePlayer(PlayerConverter.toDTO(holdemRoundSettings.getActivePlayer()))
                .bank(holdemRoundSettings.getBank())
                .bigBlind(PlayerConverter.toDTO(holdemRoundSettings.getBigBlind()))
                .bigBlindBet(holdemRoundSettings.getBigBlindBet())
                .smallBlindBet(holdemRoundSettings.getSmallBlindBet())
                .button(PlayerConverter.toDTO(holdemRoundSettings.getButton()))
                .flop(holdemRoundSettings.getFlop())
                .gameId(holdemRoundSettings.getGameId())
                .gameName(holdemRoundSettings.getGameName())
                .isFinished(holdemRoundSettings.isFinished())
                .lastBet(holdemRoundSettings.getLastBet())
                .players(PlayerConverter.toDTOs(holdemRoundSettings.getPlayers()))
                .river(holdemRoundSettings.getRiver())
                .stageType(holdemRoundSettings.getStageType())
                .tern(holdemRoundSettings.getTern())
                .build();

    }

    public static GameEntity toEntity(RoundSettings roundSettings) {
        final GameEntity gameEntity = GameEntity.builder()
                .id(roundSettings.getGameId())
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
