package ru.smn.poker.game;

import ru.smn.poker.action.Action;
import ru.smn.poker.action.holdem.Call;
import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.RoleType;
import ru.smn.poker.service.common.HandService;

import java.util.*;

public class HoldemTableSettingsManagerHU extends HoldemTableSettingsManager {

    public HoldemTableSettingsManagerHU(
            Random random,
            List<PlayerEntity> players,
            GameSettings gameSettings,
            HandService handService
    ) {
        super(random, players, gameSettings, handService);
    }

    @Override
    protected void setButton() {
        super.setButton();
        final PlayerEntity button = getPlayerByRole(RoleType.BUTTON).orElseThrow();
        removeChipsFromPlayer(button, getSmallBlindBet());
    }

    @Override
    protected void setBigBlind() {
        clearRole(RoleType.BIG_BLIND);
        final PlayerEntity player = getPlayers().stream()
                .filter(pl -> pl.getTableSettings().getRoleType() != RoleType.BUTTON)
                .findAny()
                .orElseThrow();
        player.getTableSettings().setRole(RoleType.BIG_BLIND);
        removeChipsFromPlayer(player, getBigBlindBet());
    }

    @Override
    protected Map<PlayerEntity, List<Action>> getBlindsHistory() {
        final Map<PlayerEntity, List<Action>> history = new HashMap<>();
        final PlayerEntity button = getPlayerByRole(RoleType.BUTTON).orElseThrow();
        final List<Action> forButton = new ArrayList<>();
        final PlayerEntity bigBlind = getPlayerByRole(RoleType.BIG_BLIND).orElseThrow();
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
