package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import ru.sm.poker.dto.CombinationDTO;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.game.Round;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.holdem.ActionServiceHoldem;
import ru.sm.poker.service.holdem.WinnerServiceHoldem;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class HoldemRound implements Round {

    private final List<Player> players;
    private final String gameName;
    private final ActionServiceHoldem actionServiceHoldem;
    private final int smallBlindBet;
    private final int bigBlindBet;
    private RoundSettingsDTO roundSettingsDTO;

    @Override
    public void startRound() {
        log.info("game was started, because found 4 person");

        final RoundSettingsController roundSettingsController =
                new RoundSettingsController(players, gameName, bigBlindBet, smallBlindBet);

        this.roundSettingsDTO = roundSettingsController.getPreflopSettings();

        actionServiceHoldem.setActions(roundSettingsDTO);

        this.roundSettingsDTO = roundSettingsController.getPostFlopSettings();

        actionServiceHoldem.setActions(roundSettingsDTO);

        this.roundSettingsDTO = roundSettingsController.getPostFlopSettingsWithTern();
        actionServiceHoldem.setActions(roundSettingsDTO);

        this.roundSettingsDTO = roundSettingsController.getPostFlopSettingsWithRiver();
        actionServiceHoldem.setActions(roundSettingsDTO);

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
