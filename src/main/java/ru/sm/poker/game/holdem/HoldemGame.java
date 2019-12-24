package ru.sm.poker.game.holdem;

import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.Round;
import ru.sm.poker.model.Player;

import java.util.List;

public class HoldemGame extends Game {

    public HoldemGame(
            String gameName, int maxPlayersSize,
            List<Player> players,
            Round round
    ) {
        super(gameName, maxPlayersSize, players, round);
    }

    @Override
    public void start() {
        isStarted = true;
        while (getPlayers().size() >= 4 && isStarted) {
            getRound().startRound();
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
        return getRound().getRoundSettingsDTO();
    }
}
