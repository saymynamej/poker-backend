package ru.smn.poker.controller.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.smn.poker.action.Action;
import ru.smn.poker.dto.ActionInfo;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.SeatManager;

import java.security.Principal;

import static ru.smn.poker.util.PlayerUtil.getDefaultPlayerForHoldem;


@RequestMapping("/game")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CommonActionController {
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

    @MessageMapping("/doAction")
    public void doAction(Principal principal, ActionInfo actionInfo) {
        final Action action = ActionType.getActionByType(actionInfo.getActionType(), Long.parseLong(actionInfo.getCount()));
        actionService.setAction(principal.getName(), action);
    }

}
