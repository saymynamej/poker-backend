package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.game.Round;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.RoundSettings;
import ru.sm.poker.model.action.*;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.BroadCastService;
import ru.sm.poker.service.CheckCombinationService;
import ru.sm.poker.service.CheckCombinationServiceHoldem;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class HoldemRound implements Round {

    private final List<Player> players;
    private final String gameName;
    private final BroadCastService broadCastService;
    private final ActionService actionService;
    private final int smalBlindBet;
    private final int bigBlindBet;
    private final CheckCombinationService checkCombinationService = new CheckCombinationServiceHoldem();
    private RoundSettings roundSettings;

    @Override
    public void startRound() {
        log.info("game was started, because found 4 person");

        setAllActivePlayers();

        final RoundSettingsController roundSettingsController =
                new RoundSettingsController(players, gameName, bigBlindBet, smalBlindBet);

        this.roundSettings = roundSettingsController.getPreflopSettings();
        setActions(this.roundSettings.getPlayers());

        this.roundSettings = roundSettingsController.getPostFlopSettings();
        setActions(this.roundSettings.getPlayers());

        this.roundSettings = roundSettingsController.setPostTernSettings();
        setActions(this.roundSettings.getPlayers());

        this.roundSettings = roundSettingsController.setPostRiverSettings();
        setActions(this.roundSettings.getPlayers());
    }



    private void setAllActivePlayers() {
        this.players.forEach(player -> player.setStateType(StateType.IN_GAME));
    }

    private void setActions(List<Player> players) {
        players.forEach(player -> {
            if (player.getStateType() == StateType.IN_GAME) {
                setActivePlayer(player);
                broadCastService.sendToAll(getRoundSettings());
                waitPlayerAction(player);
                setInActivePlayer(player);
            }
        });
    }

    private void waitPlayerAction(Player player) {
        while (true) {
            if (checkAllAfk()) {
                break;
            }
            if (player.getAction().getClass() != Wait.class) {
                parseAction(player);
                break;
            }
        }
    }


    private boolean checkAllCall() {
        return players.stream()
                .allMatch(player -> player.getAction() instanceof Bet);
    }

    private boolean checkAllAfk() {
        return players.stream()
                .allMatch(player -> player.getStateType() == StateType.AFK);
    }

    private void parseAction(Player player) {
        final Action action = player.getAction();

        if (action instanceof Call) {
            call(player, (Call) action);
        } else if (action instanceof Fold) {
            fold(player, (Fold) action);
        } else if (action instanceof Raise) {
            raise(player, (Raise) action);
        }

        player.setAction(new Wait(this.gameName));
    }

    private void raise(Player player, Raise raise) {
        if (raise.getCount() < roundSettings.getLastBet() * 2) {
            player.setAction(new Wait(gameName));
            waitPlayerAction(player);
        }
        removeChipsPlayerAndAddToBank(player, raise.getCount());
        setLastBet(raise.getCount());
    }

    private void setLastBet(long count) {
        this.roundSettings.setLastBet(count);
    }

    private void fold(Player player, Fold fold) {
        player.setAction(fold);
        player.setStateType(StateType.WAIT);
    }

    private void call(Player player, Call call) {
        if (call.getCount() != roundSettings.getLastBet()) {
            player.setAction(new Wait(gameName));
            waitPlayerAction(player);
            return;
        }
        removeChipsPlayerAndAddToBank(player, call.getCount());
        this.roundSettings.setLastBet(call.getCount());
    }

    private void removeChipsPlayerAndAddToBank(Player player, long chips) {
        removeChips(player, chips);
        addBank(chips);
    }

    private Player getActivePlayer() {
        return this.players
                .stream()
                .filter(Player::isActive)
                .findFirst()
                .orElse(null);
    }

    private void removeChips(Player player, long chips) {
        player.removeChips(chips);
    }

    private void addChips(Player player, long chips) {
        player.addChips(chips);
    }

    private void addBank(long count) {
        roundSettings.setBank(roundSettings.getBank() + count);
    }

    private void setActivePlayer(Player player) {
        player.setActive(true);
        this.roundSettings.setActivePlayer(player);
    }

    private void setInActivePlayer(Player player) {
        player.setActive(false);
        this.roundSettings.setActivePlayer(null);
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
