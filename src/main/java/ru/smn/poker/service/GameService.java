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

import static ru.smn.poker.util.HistoryUtil.addActionInHistory;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final GameRepository gameRepository;
    private final ActionLogService actionLogService;
    private final HandRepository handRepository;

    public List<GameEntity> findAll() {
        return gameRepository.findAll();
    }

    @Transactional
    public GameEntity saveGame(GameEntity gameEntity) {
        return gameRepository.save(gameEntity);
    }

    @Transactional
    public void update(TableSettings tableSettings) {

    }

    public void doAction(
            PlayerEntity player,
            TableSettings tableSettings,
            long removeChips,
            long lastBet,
            Action action
    ) {
        player.removeChips(removeChips);
        tableSettings.setBank(tableSettings.getBank() + removeChips);
        setLastBet(tableSettings, lastBet);
        addActionInHistory(tableSettings, player);
        actionLogService.log(player, action, tableSettings);
    }

    public void setActivePlayer(TableSettings tableSettings, PlayerEntity player) {
        player.setActive(true);
        tableSettings.setActivePlayer(player);
        update(tableSettings);
    }

    public void setInActivePlayer(TableSettings tableSettings, PlayerEntity player) {
        player.setActive(false);
        tableSettings.setActivePlayer(null);
        update(tableSettings);
    }

    public void setLastBet(TableSettings tableSettings, long count) {
        tableSettings.setLastBet(count);
    }

}
