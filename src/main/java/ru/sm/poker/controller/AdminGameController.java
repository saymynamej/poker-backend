package ru.sm.poker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sm.poker.action.holdem.*;
import ru.sm.poker.dto.AdminActionDTO;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;

import java.security.Principal;


@RequiredArgsConstructor
@RestController
public class AdminGameController {

    private final GameManager gameManager;
    private final ActionService actionService;

    @MessageMapping("/admin/addPlayer")
    public void addUser(Principal principal) {
        gameManager.addPlayer(Player.builder()
                .name(principal.getName())
                .timeBank(60L)
                .chipsCount(5000)
                .build());
    }

    @MessageMapping("/admin/afk")
    public void setUnsetAfk(Principal principal) {
        actionService.setUnSetAfkPlayer(principal.getName());
    }

    @MessageMapping("/admin/raise")
    public void raise(AdminActionDTO actionDTO) {
        actionService.setAction(actionDTO.getName(), new Raise(Long.parseLong(actionDTO.getCount())));
    }

    @MessageMapping("/admin/call")
    public void call(AdminActionDTO actionDTO) {
        actionService.setAction(actionDTO.getName(), new Call(Long.parseLong(actionDTO.getCount())));
    }

    @MessageMapping("/admin/fold")
    public void fold(AdminActionDTO actionDTO) {
        actionService.setAction(actionDTO.getName(), new Fold());
    }

    @MessageMapping("/admin/bet")
    public void bet(AdminActionDTO actionDTO) {
        actionService.setAction(actionDTO.getName(), new Bet(Long.parseLong(actionDTO.getCount())));
    }

    @MessageMapping("/admin/check")
    public void check(AdminActionDTO actionDTO) {
        actionService.setAction(actionDTO.getName(), new Check());
    }

    @MessageMapping("/admin/reload")
    public void reload(Principal principal) {
        gameManager.reload(principal.getName());
    }
}