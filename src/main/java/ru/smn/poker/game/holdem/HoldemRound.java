package ru.smn.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.smn.poker.dto.Player;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.enums.StateType;
import ru.smn.poker.game.Round;
import ru.smn.poker.game.RoundSettingsManager;
import ru.smn.poker.service.OrderService;
import ru.smn.poker.service.WinnerService;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class HoldemRound implements Round {
    private final List<Player> players;
    private final String gameName;
    private final OrderService orderService;
    private final WinnerService winnerService;
    private final long smallBlindBet;
    private final long bigBlindBet;
    private final long gameId;
    private RoundSettings roundSettings;


    @Override
    public void startRound() {
        log.info("game was started, because found enough persons");
        final RoundSettingsManager roundSettingsManager = HoldemRoundSettingsManagerFactory.getRoundSettingsManager(
                players,
                gameName,
                bigBlindBet,
                smallBlindBet,
                gameId
        );


        while (true) {
            roundSettings = roundSettingsManager.getSettings(
                    roundSettings
            );
            final boolean skipNext = orderService.start(roundSettings);

            if (skipNext || roundSettings.getStageType() == StageType.RIVER) {
                break;
            }
        }
        winnerService.sendPrizes(roundSettings);
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
        this.roundSettings.getPlayers().forEach(player -> player.setStateType(StateType.AFK));
    }

    @Override
    public synchronized RoundSettings getRoundSettings() {
        return this.roundSettings;
    }
}
