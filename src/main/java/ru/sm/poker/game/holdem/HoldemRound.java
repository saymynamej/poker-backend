package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.game.Round;
import ru.sm.poker.game.RoundSettingsManager;
import ru.sm.poker.service.OrderService;
import ru.sm.poker.service.WinnerService;
import ru.sm.poker.service.common.SimpleWinnerService;
import ru.sm.poker.service.holdem.HoldemCombinationService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class HoldemRound implements Round {
    private final List<PlayerDTO> players;
    private final String gameName;
    private final OrderService orderService;
    private final long smallBlindBet;
    private final long bigBlindBet;
    private final WinnerService winnerService = new SimpleWinnerService(new HoldemCombinationService());
    private HoldemRoundSettingsDTO holdemRoundSettings;

    @Override
    public void startRound() {
        log.info("game was started, because found enough persons");

        final RoundSettingsManager roundSettingsManager = HoldemRoundSettingsManagerFactory
                .getRoundSettingsManager(
                        players,
                        gameName,
                        bigBlindBet,
                        smallBlindBet
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
        setAfkForPlayerWhichHaveNotEnoughChips();
    }

    private void setAfkForPlayerWhichHaveNotEnoughChips() {
        getPlayers().forEach(player -> {
            if (player.getChipsCount() == 0) {
                player.setStateType(StateType.AFK);
            }
        });
    }

    @Override
    public List<PlayerDTO> getPlayers() {
        return this.players;
    }

    @Override
    public String getGameName() {
        return this.gameName;
    }

    @Override
    public void reloadRound() {
        this.holdemRoundSettings.getPlayers().forEach(player -> player.setStateType(StateType.AFK));
    }

    @Override
    public synchronized HoldemRoundSettingsDTO getHoldemRoundSettings() {
        return this.holdemRoundSettings;
    }
}
