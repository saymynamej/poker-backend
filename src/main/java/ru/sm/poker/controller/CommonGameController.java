package ru.sm.poker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sm.poker.action.holdem.*;
import ru.sm.poker.dto.ActionDTO;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.SeatManager;
import ru.sm.poker.util.PlayerUtil;

import java.security.Principal;


@RequestMapping("/game")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CommonGameController {

    private final GameManager gameManager;
    private final ActionService actionService;
    private final SeatManager seatManager;


    @MessageMapping("/addPlayer")
    public void joinInQueue(Principal principal) {
        seatManager.joinInQueue(
                PlayerUtil.getDefaultPlayerForHoldem(principal.getName())
        );
    }

    @MessageMapping("/addPlayerInGame")
    public void joinInGame(Principal principal, String gameName) {
        seatManager.joinInGame(
                gameName,
                PlayerUtil.getDefaultPlayerForHoldem(principal.getName())
        );
    }

    @MessageMapping("/afk")
    public void setUnsetAfk(Principal principal) {
        actionService.setUnSetAfkPlayer(principal.getName());
    }

    @MessageMapping("/addChips")
    public void addChips(Principal principal) {
        gameManager.addChips(principal.getName());
    }

    @MessageMapping("/raise")
    public void raise(Principal principal, ActionDTO actionDTO) {
        actionService.setAction(principal.getName(), new Raise(Long.parseLong(actionDTO.getCount())));
    }

    @MessageMapping("/call")
    public void call(Principal principal, ActionDTO actionDTO) {
        actionService.setAction(principal.getName(), new Call(Long.parseLong(actionDTO.getCount())));
    }

    @MessageMapping("/fold")
    public void fold(Principal principal) {
        actionService.setAction(principal.getName(), new Fold());
    }

    @MessageMapping("/bet")
    public void bet(Principal principal, ActionDTO actionDTO) {
        actionService.setAction(principal.getName(), new Bet(Long.parseLong(actionDTO.getCount())));
    }

    @MessageMapping("/check")
    public void check(Principal principal) {
        actionService.setAction(principal.getName(), new Check());
    }

    @MessageMapping("/all-in")
    public void allIn(Principal principal, ActionDTO actionDTO) {
        actionService.setAction(principal.getName(), new All(Long.parseLong(actionDTO.getCount())));
    }
}
