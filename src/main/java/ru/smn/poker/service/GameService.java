package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smn.poker.action.Action;
import ru.smn.poker.entities.*;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.repository.*;

import java.util.List;
import java.util.stream.Collectors;

import static ru.smn.poker.util.HistoryUtil.addActionInHistory;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final GameRepository gameRepository;
    private final ActionLogService actionLogService;
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
    public void update(TableSettings tableSettings) {
        saveChipsCount(tableSettings);
        saveCards(
                tableSettings,
                gameRepository.findById(tableSettings.getGameId()).orElseThrow()
        );
        savePlayerSettings(tableSettings);
        saveRound(tableSettings);
    }

    @Transactional
    public void updateBlinds(TableSettings tableSettings) {
        actionLogService.logBlinds(tableSettings);
    }


    private void saveRound(TableSettings tableSettings) {
        final RoundEntity roundEntity = getRoundEntity(
                tableSettings,
                gameRepository.findById(tableSettings.getGameId()).orElse(null)
        );
        if (tableSettings.getRoundId() == null) {
            final Long idSavedRound = roundRepository.save(roundEntity).getId();
            tableSettings.setRoundId(idSavedRound);
        }

        roundRepository.save(roundEntity);
    }

    private void savePlayerSettings(TableSettings tableSettings) {
        final List<PlayerSettingsEntity> playerSettingsEntities = tableSettings.getPlayers().stream()
                .map(PlayerEntity::getSettings)
                .collect(Collectors.toList());

        playerSettingsRepository.saveAll(playerSettingsEntities);
    }

    private void saveCards(TableSettings tableSettings, GameEntity gameEntity) {
        final List<CardEntity> cardEntities = tableSettings.getPlayers().stream()
                .flatMap(players -> players.getCards().stream())
                .collect(Collectors.toList());

        cardEntities.forEach(cardEntity -> {
            cardEntity.setGame(gameEntity);
            cardEntity.setSettings(cardEntity.getPlayer().getSettings());
        });

        cardRepository.saveAll(cardEntities);

    }

    private void saveChipsCount(TableSettings tableSettings) {
        final List<ChipsCountEntity> chipsCountEntities = tableSettings.getPlayers().stream()
                .map(PlayerEntity::getChipsCount)
                .collect(Collectors.toList());

        chipsCountRepository.saveAll(chipsCountEntities);
    }


    private RoundEntity getRoundEntity(TableSettings tableSettings, GameEntity gameEntity) {
        final RoundEntity roundEntity = RoundEntity.builder()
                .bank(tableSettings.getBank())
                .bigBlindBet(tableSettings.getBigBlindBet())
                .id(tableSettings.getRoundId())
                .smallBlindBet(tableSettings.getSmallBlindBet())
                .game(gameEntity)
                .stageType(tableSettings.getStageType())
                .isFinished(tableSettings.isFinished())
                .lastBet(tableSettings.getLastBet())
                .build();

        if (tableSettings.getActivePlayer() != null) {
            roundEntity.setActivePlayer(tableSettings.getActivePlayer());
        }
        if (tableSettings.getButton() != null) {
            roundEntity.setButton(tableSettings.getButton());
        }
        if (tableSettings.getSmallBlind() != null) {
            roundEntity.setSmallBlind(tableSettings.getSmallBlind());
        }
        if (tableSettings.getBigBlind() != null) {
            roundEntity.setBigBlind(tableSettings.getBigBlind());
        }

        if (tableSettings.getFlop() != null && !tableSettings.getFlop().isEmpty()) {
            roundEntity.setF1(tableSettings.getFlop().get(0));
            roundEntity.setF2(tableSettings.getFlop().get(1));
            roundEntity.setF3(tableSettings.getFlop().get(2));
        }
        if (tableSettings.getTern() != null) {
            roundEntity.setTern(tableSettings.getTern());
        }
        if (tableSettings.getRiver() != null) {
            roundEntity.setRiver(tableSettings.getRiver());
        }
        return roundEntity;
    }

    public void doAction(
            PlayerEntity player,
            TableSettings tableSettings,
            long removeChips,
            long lastBet
    ) {
        player.removeChips(removeChips);
        addBank(tableSettings, removeChips);
        setLastBet(tableSettings, lastBet);
        addActionInHistory(tableSettings, player);
    }

    public void log(PlayerEntity player, TableSettings tableSettings, Action action) {
        actionLogService.log(player, action, tableSettings);
    }

    public void addBank(TableSettings tableSettings, long count) {
        tableSettings.setBank(tableSettings.getBank() + count);
    }

    public void setActivePlayer(TableSettings tableSettings, PlayerEntity player) {
        player.setActive(true);
        tableSettings.setActivePlayer(player);
    }

    public void setInActivePlayer(TableSettings tableSettings, PlayerEntity player) {
        player.setActive(false);
        tableSettings.setActivePlayer(null);
    }

    public void setLastBet(TableSettings tableSettings, long count) {
        tableSettings.setLastBet(count);
    }

}
