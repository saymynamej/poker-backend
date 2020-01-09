package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sm.poker.action.HoldemPipeline;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.game.Round;
import ru.sm.poker.model.Player;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class HoldemRound implements Round {

    private final List<Player> players;
    private final String gameName;
    private final HoldemPipeline holdemPipeline;
    private final int smallBlindBet;
    private final int bigBlindBet;
    private RoundSettingsDTO roundSettingsDTO;

    @Override
    public void startRound() {
        log.info("game was started, because found 4 person");

        final RoundSettingsController roundSettingsController =
                new RoundSettingsController(players, gameName, bigBlindBet, smallBlindBet);

        this.roundSettingsDTO = roundSettingsController.getPreflopSettings();

        log.info("flop start");
        holdemPipeline.start(roundSettingsDTO, Collections.emptyList());

        this.roundSettingsDTO = roundSettingsController.getPostFlopSettings();
        log.info("postflop start");
        holdemPipeline.start(roundSettingsDTO, Collections.emptyList());

        this.roundSettingsDTO = roundSettingsController.getPostFlopSettingsWithTern();
        holdemPipeline.start(roundSettingsDTO, Collections.emptyList());

        this.roundSettingsDTO = roundSettingsController.getPostFlopSettingsWithRiver();
        holdemPipeline.start(roundSettingsDTO, Collections.emptyList());

    }

    @Override
    public void reloadRound() {
        this.players.forEach(player -> player.setStateType(StateType.AFK));
    }

    @Override
    public RoundSettingsDTO getRoundSettingsDTO() {
        return this.roundSettingsDTO;
    }
}
