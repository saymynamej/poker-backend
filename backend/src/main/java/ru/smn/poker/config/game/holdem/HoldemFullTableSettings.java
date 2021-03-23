package ru.smn.poker.config.game.holdem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.smn.poker.config.game.GameSettings;
import ru.smn.poker.enums.GameType;

@RequiredArgsConstructor
@Component
@Getter
@Setter
public class HoldemFullTableSettings implements GameSettings {

    private final static int MAX_PLAYERS_SIZE = 9;

    private final static int MIN_ACTIVE_PLAYERS = 3;

    private final static int MIN_PLAYERS_FOR_START = 3;

    private final static int MIN_SMALL_BLIND_BET = 1;

    private final static int MIN_BIG_BLIND_BET = 2;

    private final GameType gameType = GameType.HOLDEM_FULL;

    private String gameName;

    private Long tableId;

    @Override
    public int getMaxPlayerSize() {
        return MAX_PLAYERS_SIZE;
    }

    @Override
    public int getMinActivePlayers() {
        return MIN_ACTIVE_PLAYERS;
    }

    @Override
    public int getMinPlayersForStart() {
        return MIN_PLAYERS_FOR_START;
    }

    @Override
    public long getStartSmallBlindBet() {
        return MIN_SMALL_BLIND_BET;
    }


    @Override
    public long getStartBigBlindBet() {
        return MIN_BIG_BLIND_BET;
    }

    @Override
    public GameType getGameType() {
        return gameType;
    }
}
