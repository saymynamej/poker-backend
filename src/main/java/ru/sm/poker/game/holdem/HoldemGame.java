package ru.sm.poker.game.holdem;

import ru.sm.poker.config.game.GameSettings;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.Round;
import ru.sm.poker.util.ThreadUtil;

import java.util.List;

public class HoldemGame extends Game {
    private final static long DELAY_IN_SECONDS = 2L;

    public HoldemGame(GameSettings gameSettings, Round round) {
        super(gameSettings, round);
    }

    @Override
    public void start() {
        isEnable = true;
        setAllPlayersActive();
        while (isEnable()) {
            ThreadUtil.sleep(DELAY_IN_SECONDS);
            if (isReady()) {
                getRound().startRound();
            }
        }
    }

    @Override
    public void stop() {
        isEnable = false;
    }

    @Override
    public HoldemRoundSettingsDTO getRoundSettings() {
        return getRound().getHoldemRoundSettings();
    }

    @Override
    protected boolean isReady() {
        final List<PlayerDTO> players = getPlayers();
        return players.size() >= getGameSettings().getMinPlayersForStart() && players.stream()
                .filter(player -> player.getStateType() == StateType.IN_GAME)
                .count() >= getGameSettings().getMinActivePlayers();
    }

    private void setAllPlayersActive() {
        getPlayers().forEach(player -> player.setStateType(StateType.IN_GAME));
    }
}
