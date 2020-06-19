package ru.sm.poker.game.holdem;

import ru.sm.poker.config.game.GameSettings;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.Round;
import ru.sm.poker.game.common.CommonGameManager;
import ru.sm.poker.util.ThreadUtil;

import java.util.List;
import java.util.Map;

public class HoldemGame extends Game {

    private final static long DELAY_IN_SECONDS = 2L;

    public HoldemGame(GameSettings gameSettings, Round round) {
        super(gameSettings, round);
        setAllPlayersActive();
    }

    @Override
    public void start() {
        enable();
        while (isEnable()) {
            ThreadUtil.sleep(DELAY_IN_SECONDS);
            addChips();
            if (isReady()) {
                getRound().startRound();
            }
        }
    }

    @Override
    public void enable() {
        isEnable = true;
    }

    @Override
    public void disable() {
        isEnable = false;
    }

    @Override
    public void reload() {
        getRound().reloadRound();
    }

    @Override
    public HoldemRoundSettings getRoundSettings() {
        return getRound().getHoldemRoundSettings();
    }

    private void setAllPlayersActive() {
        getPlayers().forEach(player -> player.setStateType(StateType.IN_GAME));
    }

    private void addChips(){
        final Map<Player, Long> chipsMap = CommonGameManager.getChipsMap();

        if (chipsMap.isEmpty()){
            return;
        }

        final List<Player> players = getPlayers();

        players.forEach(player -> {
            final Long chipsCount = chipsMap.get(player);
            if (chipsCount != null && chipsCount > 0){
                player.addChips(chipsCount);
                player.setStateType(StateType.IN_GAME);
            }
            chipsMap.remove(player);
        });

    }

    private boolean isReady() {
        final List<Player> players = getPlayers();
        return players.size() >= getGameSettings().getMinPlayersForStart() && players.stream()
                .filter(player -> player.getStateType() == StateType.IN_GAME)
                .count() >= getGameSettings().getMinActivePlayers();
    }
}
