package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.game.Round;
import ru.sm.poker.game.RoundSettingsManager;
import ru.sm.poker.service.OrderService;

import java.util.List;

import static ru.sm.poker.util.HistoryUtil.unionHistory;

@RequiredArgsConstructor
@Slf4j
public class HoldemRound implements Round {
    private final List<PlayerDTO> players;
    private final String gameName;
    private final OrderService orderService;
    private final long smallBlindBet;
    private final long bigBlindBet;
    private HoldemRoundSettingsDTO holdemRoundSettings;

    @Override
    public void startRound() {
        log.info("game was started, because found enough persons");

        final RoundSettingsManager roundSettingsManager = HoldemRoundSettingsManagerFactory
                .getRoundSettingsManager(players, gameName, bigBlindBet, smallBlindBet);

        this.holdemRoundSettings = roundSettingsManager.getPreflopSettings();
        boolean isSkipNext = orderService.start(holdemRoundSettings);

        if (!isSkipNext) {
            this.holdemRoundSettings = roundSettingsManager.getPostFlopSettings(
                    this.holdemRoundSettings.getBank(),
                    this.holdemRoundSettings.getStageHistory()
            );

            isSkipNext = orderService.start(this.holdemRoundSettings);
            if (!isSkipNext) {
                this.holdemRoundSettings = roundSettingsManager.getPostFlopSettingsWithTern(
                        this.holdemRoundSettings.getBank(),
                        unionHistory(
                                this.holdemRoundSettings.getStageHistory(),
                                this.holdemRoundSettings.getFullHistory()
                        )
                );

                isSkipNext = orderService.start(this.holdemRoundSettings);
                if (!isSkipNext) {
                    this.holdemRoundSettings = roundSettingsManager.getPostFlopSettingsWithRiver(
                            this.holdemRoundSettings.getBank(),
                            unionHistory(
                                    this.holdemRoundSettings.getStageHistory(),
                                    this.holdemRoundSettings.getFullHistory()
                            )
                    );
                    orderService.start(holdemRoundSettings);
                }
            }
        }
        setAfkForPlayerWhichHaveNotEnoughChips();
    }

    private void setAfkForPlayerWhichHaveNotEnoughChips(){
        getPlayers().forEach(player -> {
            if (player.getChipsCount() == 0){
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
