package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.game.Round;
import ru.sm.poker.game.RoundSettingsManager;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.OrderService;
import ru.sm.poker.service.WinnerService;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class HoldemRound implements Round {

    private final List<Player> players;
    private final String gameName;
    private final OrderService orderService;
    private final WinnerService winnerService;
    private final int smallBlindBet;
    private final int bigBlindBet;
    private RoundSettingsDTO roundSettingsDTO;

    @Override
    public void startRound() {
        log.info("game was started, because found 4 person");

        final RoundSettingsManager roundSettingsManager =
                new HoldemRoundSettingsManager(players, gameName, bigBlindBet, smallBlindBet);
        this.roundSettingsDTO = roundSettingsManager.getPreflopSettings();
        orderService.start(roundSettingsDTO);
        this.roundSettingsDTO = roundSettingsManager.getPostFlopSettings(this.roundSettingsDTO.getBank());
        orderService.start(roundSettingsDTO);
        this.roundSettingsDTO = roundSettingsManager.getPostFlopSettingsWithTern(this.roundSettingsDTO.getBank());
        orderService.start(roundSettingsDTO);
        this.roundSettingsDTO = roundSettingsManager.getPostFlopSettingsWithRiver(this.roundSettingsDTO.getBank());
        orderService.start(roundSettingsDTO);
        winnerService.sendPrizes(roundSettingsDTO);

    }

    private boolean isAfk() {
        return roundSettingsDTO.isAfk();
    }

    @Override
    public void reloadRound() {
        this.roundSettingsDTO.setAfk(true);
    }

    @Override
    public RoundSettingsDTO getRoundSettingsDTO() {
        return this.roundSettingsDTO;
    }
}
