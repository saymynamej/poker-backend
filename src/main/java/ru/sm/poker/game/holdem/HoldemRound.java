package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.game.Round;
import ru.sm.poker.game.RoundSettingsManager;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.service.OrderService;
import ru.sm.poker.service.WinnerService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class HoldemRound implements Round {

    private final List<PlayerDTO> playerDTOS;
    private final String gameName;
    private final OrderService orderService;
    private final WinnerService winnerService;
    private final long smallBlindBet;
    private final long bigBlindBet;
    private HoldemRoundSettingsDTO holdemRoundSettingsDTO;

    @Override
    public void startRound() {
        log.info("game was started, because found enough persons");

        final RoundSettingsManager roundSettingsManager =
                new HoldemRoundSettingsManager(playerDTOS, gameName, bigBlindBet, smallBlindBet);

        this.holdemRoundSettingsDTO = roundSettingsManager.getPreflopSettings();

        orderService.start(holdemRoundSettingsDTO);

        this.holdemRoundSettingsDTO = roundSettingsManager.getPostFlopSettings(this.holdemRoundSettingsDTO.getBank());
        orderService.start(holdemRoundSettingsDTO);

        this.holdemRoundSettingsDTO = roundSettingsManager.getPostFlopSettingsWithTern(this.holdemRoundSettingsDTO.getBank());
        orderService.start(holdemRoundSettingsDTO);

        this.holdemRoundSettingsDTO = roundSettingsManager.getPostFlopSettingsWithRiver(this.holdemRoundSettingsDTO.getBank());
        orderService.start(holdemRoundSettingsDTO);


        winnerService.sendPrizes(holdemRoundSettingsDTO);

//        //TODO, TRANSFER IT TO THE OTHER SERVICE
        playerDTOS.forEach(player -> {
            if (player.getChipsCount() ==  0){
                player.setStateType(StateType.AFK);
            }
        });

    }

    @Override
    public List<PlayerDTO> getPlayers() {
        return this.playerDTOS;
    }

    @Override
    public String getGameName(){
        return this.gameName;
    }

    @Override
    public void reloadRound() {
        this.holdemRoundSettingsDTO.getPlayers().forEach(player -> player.setStateType(StateType.AFK));
    }

    @Override
    public synchronized HoldemRoundSettingsDTO getHoldemRoundSettingsDTO() {
        return this.holdemRoundSettingsDTO;
    }
}
