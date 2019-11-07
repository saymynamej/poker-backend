package ru.sm.poker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sm.poker.model.action.Bet;
import ru.sm.poker.model.action.Call;
import ru.sm.poker.model.action.Fold;
import ru.sm.poker.model.action.Raise;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.PlayerService;

import java.security.Principal;

@RequestMapping("/game")
@RestController
@RequiredArgsConstructor
@Slf4j
public class GameController {
    private final ActionService actionService;
    private final PlayerService playerService;

    @MessageMapping("/addPlayer")
    public void addUser(Principal principal) {
        playerService.addPlayer(principal.getName());
    }

    @MessageMapping("/bet")
    public void bet(Principal principal, Bet bet) {
        actionService.setAction(principal.getName(), bet);
    }

    @MessageMapping("/raise")
    public void raise(Principal principal, Raise raise) {
        actionService.setAction(principal.getName(), raise);
    }

    @MessageMapping("/raise")
    public void call(Principal principal, Call call) {
        actionService.setAction(principal.getName(), call);
    }

    @MessageMapping("/raise")
    public void fold(Principal principal, Fold fold) {
        actionService.setAction(principal.getName(), fold);
    }
}
