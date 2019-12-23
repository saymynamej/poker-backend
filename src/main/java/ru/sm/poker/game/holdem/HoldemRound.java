package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.game.Round;
import ru.sm.poker.model.Combination;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.RoundSettings;
import ru.sm.poker.model.action.*;
import ru.sm.poker.service.holdem.WinnerServiceHoldem;
import ru.sm.poker.service.holdem.ActionServiceHoldem;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class HoldemRound implements Round {

    private final List<Player> players;
    private final String gameName;
    private final WinnerServiceHoldem checkWinnerServiceHoldem;
    private final ActionServiceHoldem actionServiceHoldem;
    private final int smallBlindBet;
    private final int bigBlindBet;
    private RoundSettings roundSettings;

    @Override
    public void startRound() {
        log.info("game was started, because found 4 person");

        setAllActivePlayers();

        final RoundSettingsController roundSettingsController =
                new RoundSettingsController(players, gameName, bigBlindBet, smallBlindBet);

        this.roundSettings = roundSettingsController.getPreflopSettings();

        actionServiceHoldem.setActions(roundSettings);

        this.roundSettings = roundSettingsController.getPostFlopSettings();

        actionServiceHoldem.setActions(roundSettings);

        this.roundSettings = roundSettingsController.setPostTernSettings();
        actionServiceHoldem.setActions(roundSettings);

        this.roundSettings = roundSettingsController.setPostRiverSettings();
        actionServiceHoldem.setActions(roundSettings);

        final List<Pair<Player, Combination>> winners = checkWinner();

    }


    private List<Pair<Player, Combination>> checkWinner() {

        return checkWinnerServiceHoldem.findWinners(
                roundSettings.getPlayers(),
                roundSettings.getFlop(),
                roundSettings.getTern(),
                roundSettings.getRiver()
        );

    }

    private void setAllActivePlayers() {
        this.players.forEach(player -> player.setAction(new Wait(gameName)));
        this.players.forEach(player -> player.setStateType(StateType.IN_GAME));
    }

    @Override
    public void reloadRound() {
        this.players.forEach(player -> player.setStateType(StateType.AFK));
    }

    @Override
    public RoundSettings getRoundSettings() {
        return this.roundSettings;
    }
}
