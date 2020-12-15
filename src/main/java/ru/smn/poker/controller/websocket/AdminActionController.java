package ru.smn.poker.controller.websocket;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.smn.poker.action.holdem.*;
import ru.smn.poker.dto.AdminAction;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.GameDataService;
import ru.smn.poker.service.SeatManager;
import ru.smn.poker.util.PlayerUtil;

import java.security.Principal;
import java.util.Optional;

import static ru.smn.poker.util.LongUtil.parseLong;


@RequiredArgsConstructor
@RestController
@Slf4j
public class AdminActionController {
    private final GameDataService gameDataService;
    private final ActionService actionService;
    private final SeatManager seatManager;

    @MessageMapping("/admin/addPlayerInQueue")
    public void joinInQueue(Principal principal) {
        seatManager.joinInQueue(PlayerUtil.getDefaultPlayerForHoldem(principal.getName()));
    }

    @MessageMapping("/admin/addPlayerInGame")
    public void joinInGame(AdminAction actionDTO) {
        seatManager.joinInGame(actionDTO.getName(), PlayerUtil.getDefaultPlayerForHoldem(new Faker().name().name(), 5000));
    }

    @MessageMapping("/admin/addChips")
    public void addChips(String name) {

    }

    @MessageMapping("/admin/changeStateType")
    public void changeStateType(String name) {
        actionService.changeStateType(name);
    }

    @MessageMapping("/admin/doRaise")
    public void doRaise(AdminAction actionDTO) {
        try {
            actionService.setAction(actionDTO.getName(), new Raise(parseLong(actionDTO.getCount())));
        } catch (RuntimeException e) {
            log.info("cannot parse raise: " + e.getMessage() + actionDTO);
        }
    }

    @MessageMapping("/admin/doCall")
    public void doCall(AdminAction actionDTO) {
        try {
            actionService.setAction(actionDTO.getName(), new Call(parseLong(actionDTO.getCount())));
        } catch (RuntimeException e) {
            log.info("cannot parse call: " + actionDTO);
        }
    }

    @MessageMapping("/admin/doFold")
    public void doFold(AdminAction actionDTO) {
        actionService.setAction(actionDTO.getName(), new Fold());
    }

    @MessageMapping("/admin/doBet")
    public void doBet(AdminAction actionDTO) {
        actionService.setAction(actionDTO.getName(), new Bet(Long.parseLong(actionDTO.getCount())));
    }

    @MessageMapping("/admin/doCheck")
    public void doCheck(AdminAction actionDTO) {
        actionService.setAction(actionDTO.getName(), new Check());
    }

    @MessageMapping("/admin/doAllIn")
    public void doAllIn(AdminAction actionDTO) {
        final Optional<PlayerEntity> playerByName = gameDataService.getPlayerByName(actionDTO.getName());
        playerByName.ifPresent(
                player -> actionService.setAction(actionDTO.getName(), new AllIn(player.getChipsCount().getCount()))
        );
    }

    @MessageMapping("/admin/reload")
    public void reload(String gameName) {
    }

    @MessageMapping("/admin/disableGame")
    public void disableGame(String gameName) {
    }

    @MessageMapping("/admin/enableGame")
    public void enableGame(String gameName) {
    }

    @MessageMapping("/admin/addBotToQueue")
    public void startGame() {

    }
}
