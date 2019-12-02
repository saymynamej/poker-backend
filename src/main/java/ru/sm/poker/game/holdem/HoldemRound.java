package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sm.poker.game.Round;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.RoundSettings;
import ru.sm.poker.model.action.*;
import ru.sm.poker.service.BroadCastService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class HoldemRound implements Round {

    private final List<Player> players;
    private final String gameName;
    private final BroadCastService broadCastService;

    private final int smallBlind;
    private final int bigBlind;

    private RoundSettings roundSettings;
    private CountAction lastBet;
    private long bank = 0;

    @Override
    public void startRound() {
        log.info("game was started, because found 4 person");
        final RoundSettingsController roundSettingsController
                = new RoundSettingsController(players, gameName, bigBlind, smallBlind);

        this.roundSettings = roundSettingsController.setPreflopSettings();
//        lastBet = new Bet(2, gameName);

        this.bank = this.roundSettings.getBank();
        setActions(this.roundSettings.getPlayers());

        this.roundSettings = roundSettingsController.setPostflopSettings();
        setActions(this.roundSettings.getPlayers());

        this.roundSettings = roundSettingsController.setPostTernSettings();
        setActions(this.roundSettings.getPlayers());

        this.roundSettings = roundSettingsController.setPostRiverSettings();
        setActions(this.roundSettings.getPlayers());
    }

    private void setActions(List<Player> players) {
        for (Player player : players) {
            setActivePlayer(player);
            System.out.println("wait action");
            broadCastService.sendToAll(getRoundSettings());
            waitPlayerAction(player);
            setActivePlayer(player);
        }
    }

    private void waitPlayerAction(Player player) {
        while (true) {
            if (player.getAction()
                    .getClass() != Wait.class) {
                parseAction(player);
                break;
            }
        }
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
    }

    private void raise(Player player, Raise raise) {
        if (raise.getCount() < lastBet.getCount() * 2) {
            return;
        }
        removeChipsPlayerAndAddToBank(player, raise.getCount());
        setLastBet(raise.getCount());
    }

    private void setLastBet(long count) {
        this.lastBet = new Bet(count, gameName);
    }

    private void fold(Player player, Fold fold) {
        player.setAction(fold);
    }

    private void call(Player player, Call call) {
        if (call.getCount() != lastBet.getCount()) {
            return;
        }
        removeChipsPlayerAndAddToBank(player, call.getCount());
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
        bank += count;
    }

    private void setActivePlayer(Player player) {
        player.setActive(!player.isActive());
    }


    @Override
    public void reloadRound() {

    }

    @Override
    public RoundSettings getRoundSettings() {
        return RoundSettings
                .builder()
                .flop(roundSettings.getFlop())
                .tern(roundSettings.getTern())
                .river(roundSettings.getRiver())
                .activePlayer(getActivePlayer())
                .button(roundSettings.getButton())
                .bank(bank)
                .smallBlind(roundSettings.getSmallBlind())
                .bigBlind(roundSettings.getBigBlind())
                .players(roundSettings.getPlayers())
                .build();
    }
}
