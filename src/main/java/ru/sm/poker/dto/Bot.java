package ru.sm.poker.dto;

import lombok.experimental.SuperBuilder;
import ru.sm.poker.enums.PlayerType;
@SuperBuilder
public class Bot extends PlayerDTO {

    public Bot(String name, long chipsCount) {
        super(name, chipsCount);
    }

    @Override
    public PlayerType getPlayerType() {
        return PlayerType.BOT;
    }
}
