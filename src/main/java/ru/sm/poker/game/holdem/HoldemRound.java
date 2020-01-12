package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.game.Round;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.OrderService;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class HoldemRound implements Round {

    private final List<Player> players;
    private final String gameName;
    private final OrderService orderService;
    private final int smallBlindBet;
    private final int bigBlindBet;
    private RoundSettingsDTO roundSettingsDTO;

    @Override
    public void startRound() {
        log.info("game was started, because found 4 person");

        final RoundSettingsController roundSettingsController =
                new RoundSettingsController(players, gameName, bigBlindBet, smallBlindBet);

        this.roundSettingsDTO = roundSettingsController.getPreflopSettings();

        orderService.start(roundSettingsDTO, Collections.emptyList());

        this.roundSettingsDTO = roundSettingsController.getPostFlopSettings(this.roundSettingsDTO.getBank());

        orderService.start(roundSettingsDTO, Collections.emptyList());

        this.roundSettingsDTO = roundSettingsController.getPostFlopSettingsWithTern(this.roundSettingsDTO.getBank());
        orderService.start(roundSettingsDTO, Collections.emptyList());

        this.roundSettingsDTO = roundSettingsController.getPostFlopSettingsWithRiver(this.roundSettingsDTO.getBank());
        orderService.start(roundSettingsDTO, Collections.emptyList());

    }

    @Override
    public void reloadRound() {
        this.roundSettingsDTO.setNeedReload(true);
    }

    @Override
    public RoundSettingsDTO getRoundSettingsDTO() {
        return this.roundSettingsDTO;
    }
}
