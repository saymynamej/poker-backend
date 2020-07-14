package ru.smn.poker.game.holdem;

import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.dto.Player;
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
            removeInActivePlayers();
            ThreadUtil.sleep(DELAY_IN_SECONDS);
            if (isReady()) {
                getRound().startRound();
            }
        }
    }

    private void removeInActivePlayers() {
        getPlayers().removeAll(getPlayers().stream()
                .filter(player -> player.getChipsCount() == 0 || player.getStateType() != StateType.IN_GAME)
                .collect(Collectors.toList()));
    }

    @Override
    public void stop() {
        isEnable = false;
    }

    @Override
    public HoldemRoundSettings getRoundSettings() {
        return getRound().getHoldemRoundSettings();
    }

    @Override
    protected boolean isReady() {
        final List<Player> players = getPlayers();
        return players.size() >= getGameSettings().getMinPlayersForStart() && players.stream()
                .filter(player -> player.getStateType() == StateType.IN_GAME)
                .count() >= getGameSettings().getMinActivePlayers();
    }

    private void setInGame() {
        getPlayers().forEach(player -> player.setStateType(StateType.IN_GAME));
    }
}
