package ru.smn.poker.game;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.service.GameService;
import ru.smn.poker.service.OrderService;
import ru.smn.poker.service.PrizeService;
import ru.smn.poker.service.WinnerService;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Slf4j
public class HoldemRound implements Round {
    private final List<PlayerEntity> players;
    private final String gameName;
    private final OrderService orderService;
    private final PrizeService prizeService;
    private final GameService gameService;
    private final long smallBlindBet;
    private final long bigBlindBet;
    private final long gameId;
    private RoundSettings roundSettings;


    @Override
    public void start() {
        final RoundSettingsManager roundSettingsManager = getRoundSettingsManager();

        while (true) {
            this.roundSettings = roundSettingsManager.getSettings(this.roundSettings);
            final boolean skipNext = orderService.start(roundSettings);
            if (skipNext || roundSettings.getStageType() == StageType.RIVER) {
                break;
            }
        }
        roundSettings.setFinished(true);
        prizeService.sendPrizes(roundSettings);
        gameService.update(roundSettings);
    }


    private RoundSettingsManager getRoundSettingsManager() {
        return HoldemRoundSettingsManagerFactory.getManager(
                players,
                gameName,
                bigBlindBet,
                smallBlindBet,
                gameId
        );
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
    public synchronized RoundSettings getRoundSettings() {
        return this.roundSettings;
    }
}
