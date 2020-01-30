package ru.sm.poker.controller;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sm.poker.action.holdem.*;
import ru.sm.poker.dto.AdminActionDTO;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.SeatManager;
import ru.sm.poker.util.PlayerUtil;

import java.security.Principal;
import java.util.Optional;

import static ru.sm.poker.util.LongUtil.parseLong;


@RequiredArgsConstructor
@RestController
@Slf4j
public class AdminGameController {

    private final GameManager gameManager;
    private final ActionService actionService;
    private final SeatManager seatManager;

    @MessageMapping("/admin/addPlayerInQueue")
    public void joinInQueue(Principal principal) {
        seatManager.joinInQueue(PlayerUtil.getDefaultPlayerForHoldem(principal.getName()));
    }

    @MessageMapping("/admin/addPlayerInGame")
    public void joinInGame(AdminActionDTO actionDTO) {
        seatManager.joinInGame(actionDTO.getName(), PlayerUtil.getDefaultPlayerForHoldem(new Faker().name().name()));
    }


    @MessageMapping("/admin/addChips")
    public void addChips(String name) {
        gameManager.addChips(name);
    }


    @MessageMapping("/admin/afk")
    public void changeStateType(String name) {
        actionService.changeStateType(name);
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
    public void reload(String gameName) {
        gameManager.reload(gameName);
    }

    @MessageMapping("/admin/disableGame")
    public void disableGame(String gameName) {
        gameManager.disableGame(gameName);
    }

    @MessageMapping("/admin/enableGame")
    public void enableGame(String gameName) {
        gameManager.enableGame(gameName);
    }

    @MessageMapping("/admin/all-in")
    public void allIn(AdminActionDTO actionDTO) {
        final Optional<Player> playerByName = gameManager.getPlayerByName(actionDTO.getName());
        playerByName.ifPresent(
                player -> actionService.setAction(actionDTO.getName(), new All(player.getChipsCount()))
        );
    }

    @MessageMapping("/admin/startGame")
    public void startGame(String gameName) {
        gameManager.startGame(gameName);
    }

    @MessageMapping("/admin/addBotToQueue")
    public void startGame() {

    }
}
