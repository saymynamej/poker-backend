package ru.sm.poker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sm.poker.action.holdem.*;
import ru.sm.poker.base.PlayerSettings;
import ru.sm.poker.dto.AdminActionDTO;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.SeatManager;

import java.security.Principal;

import static ru.sm.poker.util.LongUtil.parseLong;


@RequiredArgsConstructor
@RestController
@Slf4j
public class AdminGameController {

    private final GameManager gameManager;
    private final ActionService actionService;
    private final SeatManager seatManager;

    @MessageMapping("/admin/addPlayer")
    public void joinInQueue(Principal principal) {
        seatManager.joinInQueue(PlayerSettings.getDefaultPlayerForHoldem(principal.getName()));
    }


    @MessageMapping("/admin/addChips")
    public void addChips(String name) {
        gameManager.addChips(name);
    }


    @MessageMapping("/admin/afk")
    public void setUnsetAfk(String name) {
        actionService.setUnSetAfkPlayer(name);
    }

    @MessageMapping("/admin/raise")
    public void raise(AdminActionDTO actionDTO) {
        try {
            actionService.setAction(actionDTO.getName(), new Raise(parseLong(actionDTO.getCount())));
        } catch (RuntimeException e) {
            log.info("cannot parse raise: " + actionDTO);
        }
    }

    @MessageMapping("/admin/call")
    public void call(AdminActionDTO actionDTO) {
        try {
            actionService.setAction(actionDTO.getName(), new Call(parseLong(actionDTO.getCount())));
        } catch (RuntimeException e) {
            log.info("cannot parse call: " + actionDTO);
        }
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
