package ru.smn.poker.game;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.enums.StateType;
import ru.smn.poker.service.GameService;
import ru.smn.poker.service.OrderService;
import ru.smn.poker.service.PrizeService;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Slf4j
public class HoldemTable implements Table {
    private final List<PlayerEntity> players;
    private final String gameName;
    private final OrderService orderService;
    private final PrizeService prizeService;
    private final GameService gameService;
    private final long smallBlindBet;
    private final long bigBlindBet;
    private final long gameId;
    private TableSettings settings;

    @Override
    public void start() {
        setInGame();
        while (isEnable()) {
            final TableSettingsManager tableSettingsManager = getTableSettingsManager();
            while (true) {
                this.settings = tableSettingsManager.getSettings();
                if (orderService.start(settings)) {
                    break;
                }
                tableSettingsManager.commit(settings);
            }
            prizeService.sendPrizes(settings);
//            gameService.update(settings);
        }
    }

    private void setInGame() {
        players.forEach(playerEntity -> playerEntity.setStateType(StateType.IN_GAME));
    }

    private TableSettingsManager getTableSettingsManager() {
        return HoldemRoundSettingsManagerFactory.getManager(
                players,
                gameName,
                bigBlindBet,
                smallBlindBet,
                gameId
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
        return this.gameName;
    }

    @Override
    public synchronized TableSettings getTableSettings() {
        return this.settings;
    }
}
