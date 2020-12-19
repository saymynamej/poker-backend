package ru.smn.poker.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.service.GameService;
import ru.smn.poker.service.OrderService;
import ru.smn.poker.service.WinnerService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class HoldemRound implements Round {
    private final List<PlayerEntity> players;
    private final String gameName;
    private final OrderService orderService;
    private final WinnerService winnerService;
    private final GameService gameService;
    private final long smallBlindBet;
    private final long bigBlindBet;
    private final long gameId;
    private RoundSettings roundSettings;


    public void restore(RoundSettings roundSettings) {
        this.roundSettings = roundSettings;
        while (true) {
            gameService.update(roundSettings);

            final boolean skipNext = orderService.start(roundSettings);

            if (skipNext || roundSettings.getStageType() == StageType.RIVER) {
                break;
            }
            else {
                this.roundSettings = getRoundSettingsManager().getSettings(this.roundSettings);
            }
        }
        roundSettings.setFinished(true);
        winnerService.sendPrizes(roundSettings);
        gameService.update(roundSettings);
    }

    @Override
    public void startRound() {
        final RoundSettingsManager roundSettingsManager = getRoundSettingsManager();

        while (true) {
            this.roundSettings = roundSettingsManager.getSettings(this.roundSettings);

            gameService.update(roundSettings);

            final boolean skipNext = orderService.start(roundSettings);

            if (skipNext || roundSettings.getStageType() == StageType.RIVER) {
                break;
            }
        }
        roundSettings.setFinished(true);
        winnerService.sendPrizes(roundSettings);
        gameService.update(roundSettings);
    }


    private RoundSettingsManager getRoundSettingsManager() {
        log.info("game was started, because found enough persons");
        return HoldemRoundSettingsManagerFactory.getRoundSettingsManager(
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
    public void setGameId(Long gameId) {

    }

    @Override
    public synchronized RoundSettings getRoundSettings() {
        return this.roundSettings;
    }
}
