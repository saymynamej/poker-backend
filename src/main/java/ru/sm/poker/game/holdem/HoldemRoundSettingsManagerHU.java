package ru.sm.poker.game.holdem;

import ru.sm.poker.action.Action;
import ru.sm.poker.action.holdem.Call;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.RoleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HoldemRoundSettingsManagerHU extends HoldemRoundSettingsManager {
    public HoldemRoundSettingsManagerHU(List<PlayerDTO> players, String gameName, long bigBlindBet, long smallBlindBet) {
        super(players, gameName, bigBlindBet, smallBlindBet);
    }

    @Override
    protected void setButton() {
        super.setButton();
        final PlayerDTO button = getPlayerByRole(RoleType.BUTTON).orElseThrow();
        removeChipsFromPlayer(button, getSmallBlindBet());
    }

    @Override
    protected void setBigBlind() {
        clearRole(RoleType.BIG_BLIND);
        final PlayerDTO playerDTO = getPlayers().stream()
                .filter(player -> player.getRoleType() != RoleType.BUTTON)
                .findAny()
                .orElseThrow();
        playerDTO.setRole(RoleType.BIG_BLIND);
        removeChipsFromPlayer(playerDTO, getBigBlindBet());
    }

    @Override
    protected Map<PlayerDTO, List<Action>> setBlindsHistory() {
        final Map<PlayerDTO, List<Action>> history = new HashMap<>();
        final PlayerDTO button = getPlayerByRole(RoleType.BUTTON).orElseThrow();
        final List<Action> forButton = new ArrayList<>();
        forButton.add(new Call(getSmallBlindBet()));
        history.put(button, forButton);
        final PlayerDTO bigBlind = getPlayerByRole(RoleType.BIG_BLIND).orElseThrow();
        final List<Action> forBigBlind = new ArrayList<>();
        forBigBlind.add(new Call(getBigBlindBet()));
        history.put(bigBlind, forBigBlind);
        return history;
    }

    @Override
    protected void setSmallBlind() {}
}
