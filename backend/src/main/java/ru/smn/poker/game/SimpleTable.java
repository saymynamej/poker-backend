package ru.smn.poker.game;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.service.HandService;
import ru.smn.poker.service.OrderActionService;
import ru.smn.poker.service.PrizeService;
import ru.smn.poker.util.PlayerUtil;
import ru.smn.poker.util.ThreadUtil;

import java.util.List;


@RequiredArgsConstructor
@AllArgsConstructor
@Slf4j
public class SimpleTable implements Table {
    private final OrderActionService orderActionService;
    private final PrizeService prizeService;
    private final HandService handService;
    private final List<PlayerEntity> players;
    private final GameSettings gameSettings;
    private TableSettings settings;

    @Override
    public void start() {
        log.info("table with name:" + getGameName() + " started");
        while (isEnable()) {
            if (waitPlayerForStart()) continue;

            final TableSettingsManager tableSettingsManager = getTableSettingsManager();

            while (true) {
                this.settings = tableSettingsManager.getSettings();
                if (orderActionService.start(this.settings)) {
                    break;
                }
                if (this.settings.getStageType() == StageType.RIVER) {
                    break;
                }
                ThreadUtil.sleep(1);
            }

            this.settings.setFinished(true);
            prizeService.sendPrizes(settings);
        }
    }

    private boolean waitPlayerForStart() {
        if (PlayerUtil.getPlayerWhichMayPlay(players).size() < gameSettings.getMinActivePlayers()){
            ThreadUtil.sleep(3);
            log.info("wait players for table: "  + gameSettings.getTableName());
            return false;
        }
        return true;
    }

    private TableSettingsManager getTableSettingsManager() {
        return HoldemRoundSettingsManagerFactory.getManagerHoldemHU(
                players,
                gameSettings,
                handService
        );
    }

    public boolean isEnable() {
        return true;
    }

    @Override
    public List<PlayerEntity> getPlayers() {
        return this.players;
    }

    @Override
    public String getGameName() {
        return this.gameSettings.getTableName();
    }

    @Override
    public TableSettings getTableSettings() {
        return this.settings;
    }

    @Override
    public void addPlayer(PlayerEntity playerEntity) {
        players.add(playerEntity);
    }
}
