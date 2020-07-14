package ru.smn.poker.game.holdem;

import ru.smn.poker.action.Action;
import ru.smn.poker.action.holdem.Call;
import ru.smn.poker.dto.Player;
import ru.smn.poker.enums.RoleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HoldemRoundSettingsManagerHU extends HoldemRoundSettingsManager {

    public HoldemRoundSettingsManagerHU(List<Player> players, String gameName, long bigBlindBet, long smallBlindBet, long gameId) {
        super(players, gameName, bigBlindBet, smallBlindBet, gameId);
    }

    @Override
    protected void setButton() {
        super.setButton();
        final Player button = getPlayerByRole(RoleType.BUTTON).orElseThrow();
        removeChipsFromPlayer(button, getSmallBlindBet());
    }

    @Override
    protected void setBigBlind() {
        clearRole(RoleType.BIG_BLIND);
        final Player player = getPlayers().stream()
                .filter(pl -> pl.getRoleType() != RoleType.BUTTON)
                .findAny()
                .orElseThrow();
        player.setRole(RoleType.BIG_BLIND);
        removeChipsFromPlayer(player, getBigBlindBet());
    }

    @Override
    protected Map<Player, List<Action>> setBlindsHistory() {
        final Map<Player, List<Action>> history = new HashMap<>();
        final Player button = getPlayerByRole(RoleType.BUTTON).orElseThrow();
        final List<Action> forButton = new ArrayList<>();
        final Player bigBlind = getPlayerByRole(RoleType.BIG_BLIND).orElseThrow();
        final List<Action> forBigBlind = new ArrayList<>();
        forButton.add(new Call(getSmallBlindBet()));
        history.put(button, forButton);
        forBigBlind.add(new Call(getBigBlindBet()));
        history.put(bigBlind, forBigBlind);
        return history;
    }

    @Override
    protected void setSmallBlind() {}
}
