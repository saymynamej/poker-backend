package ru.sm.poker.game.holdem;

import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.Round;
import ru.sm.poker.model.Player;
import ru.sm.poker.util.ThreadUtil;

import java.util.List;
import java.util.Optional;

public class HoldemGame extends Game {

    private final static long DELAY_IN_SECONDS = 5L;

    public HoldemGame(String gameName, int maxPlayersSize, List<Player> players, Round round) {
        super(gameName, maxPlayersSize, players, round);
    }

    @Override
    public void start() {
        while (true) {
            ThreadUtil.sleep(DELAY_IN_SECONDS);
            if (isReady()) {
                getRound().startRound();
            }
        }
    }

    @Override
    public void stop() {
        isStarted = false;
    }

    @Override
    public void reload() {
        getRound().reloadRound();
    }

    @Override
    public RoundSettingsDTO getRoundSettings() {
//        return getRound().getRoundSettingsDTO() == null ? Optional.empty() : Optional.of(getRound().getRoundSettingsDTO());
        return getRound().getRoundSettingsDTO();
    }

    private boolean isReady() {
        final List<Player> players = getPlayers();
        return players.size() >= 4 && players.stream()
                .filter(player -> player.getStateType() == StateType.IN_GAME)
                .count() >= 4;
    }
}
