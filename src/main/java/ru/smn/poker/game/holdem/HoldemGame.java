package ru.smn.poker.game.holdem;

import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.enums.StateType;
import ru.smn.poker.game.Game;
import ru.smn.poker.game.Round;
import ru.smn.poker.util.ThreadUtil;

import java.util.List;
import java.util.stream.Collectors;

public class HoldemGame extends Game {
    private final static long DELAY_IN_SECONDS = 1L;

    public HoldemGame(GameSettings gameSettings, Round round) {
        super(gameSettings, round);
    }

    @Override
    public void start() {
        isEnable = true;
        setInGame();
        while (isEnable()) {
            addChipsToPlayers();
            removeInActivePlayers();
            ThreadUtil.sleep(DELAY_IN_SECONDS);
            if (isReady()) {
                getRound().startRound();
            }
        }
    }

    private void addChipsToPlayers() {
        final List<PlayerEntity> players = getPlayers();
        for (PlayerEntity player : players) {
            if (player.getChipsCount().getCount() == 0){
                player.addChips(5000);
            }
        }
    }

    private void removeInActivePlayers() {
        getPlayers().removeAll(getPlayers().stream()
                .filter(player -> player.getChipsCount().getCount() == 0 || player.getStateType() != StateType.IN_GAME)
                .collect(Collectors.toList()));
    }

    @Override
    public void stop() {
        isEnable = false;
    }

    @Override
    public RoundSettings getRoundSettings() {
        return getRound().getRoundSettings();
    }

    @Override
    protected boolean isReady() {
        final List<PlayerEntity> players = getPlayers();
        return players.size() >= getGameSettings().getMinPlayersForStart() && players.stream()
                .filter(player -> player.getStateType() == StateType.IN_GAME)
                .count() >= getGameSettings().getMinActivePlayers();
    }

    private void setInGame() {
        getPlayers().forEach(player -> {
            player.setStateType(StateType.IN_GAME);
        });
    }
}
