package ru.sm.poker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sm.poker.game.Game;
import ru.sm.poker.model.Player;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SecurityService {
    private final Game game;

    public boolean isExist(String name){
        return game.playerExistByName(name);
    }

    public List<String> check(Player player){
        final List<String> errors = new ArrayList<>();
        if (!checkActivePlayer(player)){
            errors.add(String.format("user is not active, request:%s, active:%s", player.getName(), game.getActivePlayer().getName()));
        }
        return errors;
    }

    private boolean checkActivePlayer(Player player){
        return game
                .getActivePlayer()
                .equals(player);
    }
}
