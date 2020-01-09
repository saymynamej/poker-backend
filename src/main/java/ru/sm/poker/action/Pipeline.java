package ru.sm.poker.action;

import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;

import java.util.List;

public interface Pipeline {
    void start(RoundSettingsDTO roundSettingsDTO, List<Player> filterPlayers);
}
