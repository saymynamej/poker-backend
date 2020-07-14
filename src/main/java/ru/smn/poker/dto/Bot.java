package ru.smn.poker.dto;

import lombok.experimental.SuperBuilder;
import ru.smn.poker.enums.PlayerType;
@SuperBuilder
public class Bot extends Player {

    public Bot(String name, long chipsCount) {
        super(name, chipsCount);
    }

    @Override
    public PlayerType getPlayerType() {
        return PlayerType.BOT;
    }
}
