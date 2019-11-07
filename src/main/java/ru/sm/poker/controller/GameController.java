package ru.sm.poker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sm.poker.game.Game;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;
import java.util.List;

@RequestMapping("/game")
@RestController
@RequiredArgsConstructor
public class GameController {
    private final Game game;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ActionService actionService;



    @MessageMapping("/addPlayer")
    @SendTo("/poker/game/ready/players")
    public ResponseEntity<List<Player>> addUser(SimpMessageHeaderAccessor messageHeaderAccessor) {
        if (game.addPlayer(new Player(messageHeaderAccessor.getSessionId(), 5000))) {
            return new ResponseEntity<>(game.getPlayers(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @MessageMapping("/sendAction")
    public void action(SimpMessageHeaderAccessor messageHeaderAccessor, String json) {
        actionService.parseAction(messageHeaderAccessor.getSessionId(), json);
    }
}
