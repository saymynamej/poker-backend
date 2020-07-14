package ru.smn.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.dto.Player;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.enums.StateType;
import ru.smn.poker.game.Round;
import ru.smn.poker.game.RoundSettingsManager;
import ru.smn.poker.service.OrderService;
import ru.smn.poker.service.WinnerService;
import ru.smn.poker.service.common.SimpleWinnerService;
import ru.smn.poker.service.holdem.HoldemCombinationService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class HoldemRound implements Round {
    private final List<Player> players;
    private final String gameName;
    private final OrderService orderService;
    private final long smallBlindBet;
    private final long bigBlindBet;
    private final WinnerService winnerService = new SimpleWinnerService(new HoldemCombinationService());
    private final long gameId;
    private HoldemRoundSettings holdemRoundSettings;


    @Override
    public void startRound() {
        log.info("game was started, because found enough persons");
        System.out.println(gameId);
        System.out.println(gameName);
        final RoundSettingsManager roundSettingsManager = HoldemRoundSettingsManagerFactory.getRoundSettingsManager(
                players,
                gameName,
                bigBlindBet,
                smallBlindBet,
                gameId
        );

        while (true) {
            holdemRoundSettings = roundSettingsManager.getSettings(
                    holdemRoundSettings
            );
            final boolean skipNext = orderService.start(holdemRoundSettings);

            if (skipNext || holdemRoundSettings.getStageType() == StageType.RIVER) {
                break;
            }
        }
        winnerService.sendPrizes(holdemRoundSettings);
    }

    @Override
    public synchronized List<Player> getPlayers() {
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
    public void reloadRound() {
        this.holdemRoundSettings.getPlayers().forEach(player -> player.setStateType(StateType.AFK));
    }

    @Override
    public synchronized HoldemRoundSettings getHoldemRoundSettings() {
        return this.holdemRoundSettings;
    }
}
