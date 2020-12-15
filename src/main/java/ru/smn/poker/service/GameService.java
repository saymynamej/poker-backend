package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smn.poker.entities.*;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.smn.poker.util.HistoryUtil.addActionInHistory;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final RoundRepository roundRepository;
    private final ChipsCountRepository chipsCountRepository;
    private final CardRepository cardRepository;
    private final PlayerSettingsRepository playerSettingsRepository;

    public List<GameEntity> findAll() {
        return gameRepository.findAll();
    }

    @Transactional
    public GameEntity saveGame(GameEntity gameEntity) {
        return gameRepository.save(gameEntity);
    }

    @Transactional
    public void update(RoundSettings roundSettings) {
        final Optional<GameEntity> optionalGameEntity = gameRepository.findById(roundSettings.getGameId());
        saveChipsCount(roundSettings);
        saveCards(roundSettings, optionalGameEntity.orElseThrow());
        savePlayerSettings(roundSettings);
        saveRound(roundSettings);
    }

    private void saveRound(RoundSettings roundSettings) {
        final RoundEntity roundEntity = getRoundEntity(
                roundSettings,
                gameRepository.findById(roundSettings.getGameId()).orElse(null)
        );
        if (roundSettings.getRoundId() == null) {
            final Long idSavedRound = roundRepository.save(roundEntity).getId();
            roundSettings.setRoundId(idSavedRound);
        }

        roundRepository.save(roundEntity);
    }

    private void savePlayerSettings(RoundSettings roundSettings) {
        final List<PlayerSettingsEntity> playerSettingsEntities = roundSettings.getPlayers().stream()
                .map(PlayerEntity::getSettings)
                .collect(Collectors.toList());

        playerSettingsRepository.saveAll(playerSettingsEntities);
    }

    private void saveCards(RoundSettings roundSettings, GameEntity gameEntity) {

        final List<CardEntity> cardEntities = roundSettings.getPlayers().stream()
                .flatMap(players -> players.getCards().stream())
                .collect(Collectors.toList());

        cardEntities.forEach(cardEntity -> cardEntity.setGame(gameEntity));

        cardRepository.saveAll(cardEntities);

    }

    private void saveChipsCount(RoundSettings roundSettings) {
        final List<ChipsCountEntity> chipsCountEntities = roundSettings.getPlayers().stream()
                .map(PlayerEntity::getChipsCount)
                .collect(Collectors.toList());

        chipsCountRepository.saveAll(chipsCountEntities);
    }


    private RoundEntity getRoundEntity(RoundSettings roundSettings, GameEntity gameEntity) {
        final RoundEntity roundEntity = RoundEntity.builder()
                .bank(roundSettings.getBank())
                .bigBlindBet(roundSettings.getBigBlindBet())
                .id(roundSettings.getRoundId())
                .smallBlindBet(roundSettings.getSmallBlindBet())
                .game(gameEntity)
                .stageType(roundSettings.getStageType())
                .isFinished(roundSettings.isFinished())
                .lastBet(roundSettings.getLastBet())
                .build();

        if (roundSettings.getActivePlayer() != null) {
            roundEntity.setActivePlayer(roundSettings.getActivePlayer());
        }
        if (roundSettings.getButton() != null) {
            roundEntity.setButton(roundSettings.getButton());
        }
        if (roundSettings.getSmallBlind() != null) {
            roundEntity.setSmallBlind(roundSettings.getSmallBlind());
        }
        if (roundSettings.getBigBlind() != null) {
            roundEntity.setBigBlind(roundSettings.getBigBlind());
        }

        if (roundSettings.getFlop() != null && !roundSettings.getFlop().isEmpty()) {
            roundEntity.setF1(roundSettings.getFlop().get(0));
            roundEntity.setF2(roundSettings.getFlop().get(1));
            roundEntity.setF3(roundSettings.getFlop().get(2));
        }
        if (roundSettings.getTern() != null) {
            roundEntity.setTern(roundSettings.getTern());
        }
        if (roundSettings.getRiver() != null) {
            roundEntity.setRiver(roundSettings.getRiver());
        }
        return roundEntity;
    }

    public long getNextGameId() {
        final GameEntity gameEntityWithMaxId = gameRepository.findGameEntityWithMaxId();
        if (gameEntityWithMaxId == null) {
            return 1;
        }
        Long id = gameEntityWithMaxId.getId();
        return ++id;
    }

    public void doAction(PlayerEntity player, RoundSettings roundSettings, long removeChips, long lastBet) {
        player.removeChips(removeChips);
        addBank(roundSettings, removeChips);
        setLastBet(roundSettings, lastBet);
        addActionInHistory(roundSettings, player);
    }

    public void addBank(RoundSettings roundSettings, long count) {
        roundSettings.setBank(roundSettings.getBank() + count);
    }

    public void setActivePlayer(RoundSettings roundSettings, PlayerEntity player) {
        player.setActive(true);
        roundSettings.setActivePlayer(player);
    }

    public void setInActivePlayer(RoundSettings roundSettings, PlayerEntity player) {
        player.setActive(false);
        roundSettings.setActivePlayer(null);
    }

    public void setLastBet(RoundSettings roundSettings, long count) {
        roundSettings.setLastBet(count);
    }

}
