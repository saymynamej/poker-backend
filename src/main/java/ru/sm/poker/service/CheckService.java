package ru.sm.poker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sm.poker.game.Game;
import ru.sm.poker.model.Player;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CheckService {
    private final Game game;


    public boolean isExist(String name){
        return game.playerExistByName(name);
    }

    public List<String> check(Player player){
        final List<String> errors = new ArrayList<>();
        if (!checkActivePlayer(player)){
            errors.add("user is not active");
        }
        return errors;
    }

    private boolean checkActivePlayer(Player player){
        return game.getActivePlayer().equals(player);
    }
}
