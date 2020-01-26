package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.game.Round;
import ru.sm.poker.game.RoundSettingsManager;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.OrderService;
import ru.sm.poker.service.WinnerService;

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

//        //TODO, TRANSFER IT TO THE OTHER SERVICE
        players.forEach(player -> {
            if (player.getChipsCount() ==  0){
                player.setStateType(StateType.AFK);
            }
        });


    }

    @Override
    public void reloadRound() {
        this.roundSettingsDTO.getPlayers().forEach(player -> player.setStateType(StateType.AFK));
    }

    @Override
    public synchronized RoundSettingsDTO getRoundSettingsDTO() {
        return this.roundSettingsDTO;
    }
}
