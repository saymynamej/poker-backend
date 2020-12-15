package ru.smn.poker.converter;

import ru.smn.poker.dto.HoldemRoundSettingsDTO;
import ru.smn.poker.entities.GameEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.entities.RoundEntity;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.game.RoundSettings;

import java.util.List;

public class RoundSettingsConverter {


    public static HoldemRoundSettingsDTO toDTO(RoundSettings roundSettings) {

        return HoldemRoundSettingsDTO.builder()
                .activePlayer(PlayerConverter.toDTO(roundSettings.getActivePlayer()))
                .bank(roundSettings.getBank())
                .bigBlind(PlayerConverter.toDTO(roundSettings.getBigBlind()))
                .bigBlindBet(roundSettings.getBigBlindBet())
                .smallBlindBet(roundSettings.getSmallBlindBet())
                .button(PlayerConverter.toDTO(roundSettings.getButton()))
                .flop(roundSettings.getFlop())
                .gameId(roundSettings.getGameId())
                .gameName(roundSettings.getGameName())
                .isFinished(roundSettings.isFinished())
                .lastBet(roundSettings.getLastBet())
                .players(PlayerConverter.toDTOs(roundSettings.getPlayers()))
                .river(roundSettings.getRiver())
                .stageType(roundSettings.getStageType())
                .tern(roundSettings.getTern())
                .smallBlind(PlayerConverter.toDTO(roundSettings.getSmallBlind()))
                .build();

    }

    public static GameEntity toEntity(RoundSettings roundSettings) {
        final GameEntity gameEntity = GameEntity.builder()
                .id(roundSettings.getGameId())
                .players(roundSettings.getPlayers())
                .name(roundSettings.getGameName())
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
