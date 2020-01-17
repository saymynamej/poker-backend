package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.model.Player;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlayerService {

    public void removeChips(Player player, long chips) {
        player.removeChips(chips);
    }

    public void addChips(Player player, long chips) {
        player.addChips(chips);
    }

    public boolean checkAllAfk(List<Player> players) {
        return players.stream()
                .allMatch(player -> player.getStateType() == StateType.AFK);
    }

    public boolean checkSize(List<Player> players, long size) {
        return players.stream()
                .filter(player -> player.getStateType() == StateType.IN_GAME).count() >= size;
    }
}
