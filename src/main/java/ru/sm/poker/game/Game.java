package ru.sm.poker.game;

import lombok.extern.slf4j.Slf4j;
import ru.sm.poker.config.game.GameSettings;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.StateType;

import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class Game {

    private final GameSettings gameSettings;
    private final Round round;
    protected boolean isEnable = true;

    public Game(GameSettings gameSettings, Round round) {
        this.gameSettings = gameSettings;
        this.round = round;
    }

    protected Round getRound() {
        return this.round;
    }

    public List<PlayerDTO> getPlayers() {
        return this.round.getPlayers();
    }

    public boolean isEnable() {
        return isEnable;
    }

    public String getGameName() {
        return this.round.getGameName();
    }

    public int getMaxPlayersSize() {
        return this.gameSettings.getMaxPlayerSize();
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public void addPlayer(PlayerDTO playerDTO) {
        if (!this.getPlayers().contains(playerDTO)) {
            playerDTO.setStateType(StateType.IN_GAME);
            this.getPlayers().add(playerDTO);
        }
    }

    public boolean removePlayer(PlayerDTO playerDTO) {
        return this.getPlayers().remove(playerDTO);
    }

    public boolean removePlayer(String name) {
        final Optional<PlayerDTO> optionalPlayer = getPlayers().stream()
                .filter(player -> player.getName().equals(name))
                .findAny();
        if (optionalPlayer.isPresent()) {
            final PlayerDTO playerDTO = optionalPlayer.get();
            playerDTO.setStateType(StateType.LEAVE);
            return this.removePlayer(playerDTO);
        }
        return false;
    }

    public abstract void start();

    public abstract void enable();

    public abstract void disable();

    public abstract void reload();

    public abstract HoldemRoundSettingsDTO getRoundSettings();

}
