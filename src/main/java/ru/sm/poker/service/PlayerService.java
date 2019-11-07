package ru.sm.poker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sm.poker.game.Game;
import ru.sm.poker.model.Player;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final Game game;
    private final CheckService checkService;
    private final BroadCastService broadCastService;


    public void addPlayer(String name) {
        if (!checkService.isExist(name)) {
            game.addPlayer(new Player(name, 5000));
            broadCastService.sendToAllPlayerReadyMessage(game.getPlayers());
        } else {
            broadCastService.sendErrorToUser(name, "You have one registration yet");
        }
    }
}
