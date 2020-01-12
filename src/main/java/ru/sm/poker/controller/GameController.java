package ru.sm.poker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sm.poker.action.holdem.*;
import ru.sm.poker.dto.ActionDTO;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;

import java.security.Principal;

@RequestMapping("/game")
@RestController
@RequiredArgsConstructor
@Slf4j
public class GameController {

    private final GameManager gameManager;
    private final ActionService actionService;

    @MessageMapping("/addPlayer")
    public void addUser(Principal principal) {
        gameManager.addPlayer(Player.builder()
                .name(principal.getName())
                .timeBank(60L)
                .chipsCount(5000)
                .build());
    }

    @MessageMapping
    public void setUnsetAfk(Principal principal) {
        actionService.setUnSetAfkPlayer(principal.getName());
    }

    @MessageMapping("/addPlayerInGame")
    public void addUserInGame(Principal principal, String gameName) {
        gameManager.addPlayer(Player
                .builder()
                .name(principal.getName())
                .chipsCount(5000L)
                .build(), gameName);
    }

    @MessageMapping("/raise")
    public void raise(Principal principal, ActionDTO actionDTO) {
        actionService.setAction(actionDTO.getName(), new Raise(Long.parseLong(actionDTO.getCount())));
    }

    @MessageMapping("/call")
    public void call(Principal principal, ActionDTO actionDTO) {
        actionService.setAction(actionDTO.getName(), new Call(Long.parseLong(actionDTO.getCount())));
    }

    @MessageMapping("/fold")
    public void fold(Principal principal, ActionDTO actionDTO) {
        actionService.setAction(actionDTO.getName(), new Fold());
    }

    @MessageMapping("/bet")
    public void bet(Principal principal, ActionDTO actionDTO) {
        actionService.setAction(principal.getName(), new Bet(Long.parseLong(actionDTO.getCount())));
    }

    @MessageMapping("/check")
    public void check(Principal principal, ActionDTO actionDTO) {
        actionService.setAction(actionDTO.getName(), new Check());
    }

    @MessageMapping("/reload")
    public void reload(Principal principal) {
        gameManager.reload(principal.getName());
    }
}
