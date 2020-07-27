package ru.smn.poker.converter;

import ru.smn.poker.dto.Player;
import ru.smn.poker.entities.RoundEntity;
import ru.smn.poker.enums.CardType;
import ru.smn.poker.enums.RoleType;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.entities.ChipsCountEntity;
import ru.smn.poker.entities.GameEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.GameType;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.smn.poker.util.PlayerUtil.*;

public class RoundSettingsConverter {

    public static GameEntity toEntity(RoundSettings roundSettings, List<RoundEntity> prevRounds) {
        final GameEntity gameEntity = GameEntity.builder()
                .id(roundSettings.getGameId())
                .name(roundSettings.getGameName())
                .gameType(GameType.HOLDEM_FULL)
                .build();

        final List<Player> players = roundSettings.getPlayers();

        final List<PlayerEntity> playerEntities = PlayerConverter.toEntities(players, gameEntity);

        gameEntity.setPlayers(playerEntities);

        final List<ChipsCountEntity> chipsCountEntities = playerEntities.stream()
                .map(PlayerEntity::getChipsCount)
                .collect(Collectors.toList());

        gameEntity.setCounts(chipsCountEntities);

        final RoundEntity roundEntity = RoundEntity.builder()
                .button(getPlayerEntityByRole(playerEntities, RoleType.BUTTON))
                .smallBlind(getPlayerEntityByRole(playerEntities, RoleType.SMALL_BLIND))
                .bigBlind(getPlayerEntityByRole(playerEntities, RoleType.BIG_BLIND))
                .isFinished(roundSettings.isFinished())
                .stageType(roundSettings.getStageType())
                .smallBlindBet(roundSettings.getSmallBlindBet())
                .bigBlindBet(roundSettings.getBigBlindBet())
                .lastBet(roundSettings.getLastBet())
                .activePlayer(getActivePlayer(playerEntities))
                .bank(roundSettings.getBank())
                .game(gameEntity)
                .build();

        if (roundSettings.getFlop() != null) {
            final List<CardType> flop = roundSettings.getFlop();
            roundEntity.setF1(flop.get(0));
            roundEntity.setF2(flop.get(1));
            roundEntity.setF3(flop.get(2));
        }
        if (roundSettings.getTern() != null) {
            roundEntity.setTern(roundSettings.getTern());
        }
        if (roundSettings.getRiver() != null) {
            roundEntity.setRiver(roundSettings.getRiver());
        }


        long roundId = getRoundId(prevRounds);
        if (roundId != 0) {
            roundEntity.setId(roundId);
        }

        gameEntity.setRounds(Collections.singletonList(roundEntity));

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
