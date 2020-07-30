package ru.smn.poker.controller.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.smn.poker.action.holdem.*;
import ru.smn.poker.dto.Action;
import ru.smn.poker.service.GameDataService;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.SeatManager;

import java.security.Principal;

import static ru.smn.poker.util.PlayerUtil.getDefaultPlayerForHoldem;


@RequestMapping("/game")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CommonActionController {
    private final GameDataService gameDataService;
    private final ActionService actionService;
    private final SeatManager seatManager;


    @MessageMapping("/addPlayer")
    public void joinInQueue(Principal principal) {
        seatManager.joinInQueue(
                getDefaultPlayerForHoldem(principal.getName())
        );
    }

    @MessageMapping("/addPlayerInGame")
    public void joinInGame(Principal principal, String gameName) {
        seatManager.joinInGame(
                gameName,
                getDefaultPlayerForHoldem(principal.getName(), 50L)
        );
    }

    @MessageMapping("/leaveGame")
    public void leaveGame(Principal principal, String gameName) {
        seatManager.leaveGame(
                principal.getName(),
                gameName
        );
    }

    @MessageMapping("/changeStateType")
    public void changeStateType(Principal principal) {
        actionService.changeStateType(principal.getName());
    }

    @MessageMapping("/addChips")
    public void addChips(Principal principal) {
    }

    @MessageMapping("/doRaise")
    public void doRaise(Principal principal, Action action) {
        actionService.setAction(principal.getName(), new Raise(Long.parseLong(action.getCount())));
    }

    @MessageMapping("/doCall")
    public void doCall(Principal principal, Action action) {
        actionService.setAction(principal.getName(), new Call(Long.parseLong(action.getCount())));
    }

    @MessageMapping("/doFold")
    public void doFold(Principal principal) {
        actionService.setAction(principal.getName(), new Fold());
    }

    @MessageMapping("/doBet")
    public void doBet(Principal principal, Action action) {
        actionService.setAction(principal.getName(), new Bet(Long.parseLong(action.getCount())));
    }

    @MessageMapping("/doCheck")
    public void doCheck(Principal principal) {
        actionService.setAction(principal.getName(), new Check());
    }

    @MessageMapping("/doAllIn")
    public void doAllIn(Principal principal, Action action) {
        actionService.setAction(principal.getName(), new AllIn(Long.parseLong(action.getCount())));
    }
}
