package ru.sm.poker.controller;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sm.poker.action.holdem.*;
import ru.sm.poker.dto.AdminActionDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.SeatManager;
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
        seatManager.joinInGame(actionDTO.getName(), PlayerUtil.getDefaultPlayerForHoldem(new Faker().name().name(), 50));
    }

    @MessageMapping("/admin/addChips")
    public void addChips(String name) {
        gameManager.addChips(name);
    }

    @MessageMapping("/admin/changeStateType")
    public void changeStateType(String name) {
        actionService.changeStateType(name);
    }

    @MessageMapping("/admin/doRaise")
    public void doRaise(AdminActionDTO actionDTO) {
        try {
            actionService.setAction(actionDTO.getName(), new Raise(parseLong(actionDTO.getCount())));
        } catch (RuntimeException e) {
            log.info("cannot parse raise: " + actionDTO);
        }
    }

    @MessageMapping("/admin/doCall")
    public void doCall(AdminActionDTO actionDTO) {
        try {
            actionService.setAction(actionDTO.getName(), new Call(parseLong(actionDTO.getCount())));
        } catch (RuntimeException e) {
            log.info("cannot parse call: " + actionDTO);
        }
    }

    @MessageMapping("/admin/doFold")
    public void doFold(AdminActionDTO actionDTO) {
        actionService.setAction(actionDTO.getName(), new Fold());
    }

    @MessageMapping("/admin/doBet")
    public void doBet(AdminActionDTO actionDTO) {
        actionService.setAction(actionDTO.getName(), new Bet(Long.parseLong(actionDTO.getCount())));
    }

    @MessageMapping("/admin/doCheck")
    public void doCheck(AdminActionDTO actionDTO) {
        actionService.setAction(actionDTO.getName(), new Check());
    }

    @MessageMapping("/admin/doAllIn")
    public void doAllIn(AdminActionDTO actionDTO) {
        final Optional<PlayerDTO> playerByName = gameManager.getPlayerByName(actionDTO.getName());
        playerByName.ifPresent(
                player -> actionService.setAction(actionDTO.getName(), new AllIn(player.getChipsCount()))
        );
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

    @MessageMapping("/admin/startGame")
    public void startGame(String gameName) {
        gameManager.startGame(gameName);
    }

    @MessageMapping("/admin/addBotToQueue")
    public void startGame() {

    }
}
