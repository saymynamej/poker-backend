package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.game.Round;
import ru.sm.poker.game.RoundSettingsManager;
import ru.sm.poker.service.OrderService;
import ru.sm.poker.service.WinnerService;
import ru.sm.poker.util.HistoryUtil;
import ru.sm.poker.util.ThreadUtil;

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

        final boolean isSkipNext = orderService.start(holdemRoundSettingsDTO);

        if (!isSkipNext) {
            this.holdemRoundSettingsDTO = roundSettingsManager.getPostFlopSettings(
                    this.holdemRoundSettingsDTO.getBank(),
                    this.holdemRoundSettingsDTO.getStageHistory()
            );

            final boolean isSkipNext2 = orderService.start(this.holdemRoundSettingsDTO);
            if (!isSkipNext2) {
                this.holdemRoundSettingsDTO = roundSettingsManager.getPostFlopSettingsWithTern(
                        this.holdemRoundSettingsDTO.getBank(),
                        HistoryUtil.unionHistory(
                                this.holdemRoundSettingsDTO.getStageHistory(),
                                this.holdemRoundSettingsDTO.getFullHistory()
                        )
                );

                final boolean isSkipNext3 = orderService.start(this.holdemRoundSettingsDTO);
                if (!isSkipNext3) {
                    this.holdemRoundSettingsDTO = roundSettingsManager.getPostFlopSettingsWithRiver(
                            this.holdemRoundSettingsDTO.getBank(),
                            HistoryUtil.unionHistory(
                                    this.holdemRoundSettingsDTO.getStageHistory(),
                                    this.holdemRoundSettingsDTO.getFullHistory()
                            )
                    );
                    orderService.start(holdemRoundSettingsDTO);
                }
            }
        }

        winnerService.sendPrizes(holdemRoundSettingsDTO);

        ThreadUtil.sleep(10);

        playerDTOS.forEach(player -> {
            if (player.getChipsCount() == 0) {
                player.addChips(5000L);
            }
        });

    }

    @Override
    public List<PlayerDTO> getPlayers() {
        return this.playerDTOS;
    }

    @Override
    public String getGameName() {
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
