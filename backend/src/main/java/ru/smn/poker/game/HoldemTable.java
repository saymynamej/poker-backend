package ru.smn.poker.game;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.StateType;
import ru.smn.poker.service.common.HandService;
import ru.smn.poker.service.OrderActionService;
import ru.smn.poker.service.PrizeService;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Slf4j
public class HoldemTable implements Table {
    private final List<PlayerEntity> players;
    private final OrderActionService orderActionService;
    private final PrizeService prizeService;
    private final GameSettings gameSettings;
    private final HandService handService;
    private TableSettings settings;

    @Override
    public void start() {
        log.info("table with name:" + getGameName() + " started");
        setInGame();
        while (isEnable()) {
            final TableSettingsManager tableSettingsManager = getTableSettingsManager();
            while (true) {
                this.settings = tableSettingsManager.getSettings();
                if (orderActionService.start(settings)) {
                    break;
                }
                tableSettingsManager.commit(settings);
            }
            prizeService.sendPrizes(settings);
        }
    }

    private void setInGame() {
        players.forEach(playerEntity -> playerEntity.getTableSettings().setStateType(StateType.IN_GAME));
    }

    private TableSettingsManager getTableSettingsManager() {
        return HoldemRoundSettingsManagerFactory.getManager(
                players,
                gameSettings,
                handService
        );
    }

    public boolean isEnable(){
        return true;
    }

    @Override
    public synchronized List<PlayerEntity> getPlayers() {
        return this.players;
    }

    @Override
    public String getGameName() {
        return this.gameSettings.getGameName();
    }

    @Override
    public synchronized TableSettings getTableSettings() {
        return this.settings;
    }

    @Override
    public void addPlayer(PlayerEntity playerEntity) {
        players.add(playerEntity);
    }
}
