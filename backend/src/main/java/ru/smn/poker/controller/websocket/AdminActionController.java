package ru.smn.poker.controller.websocket;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.smn.poker.action.Action;
import ru.smn.poker.action.holdem.*;
import ru.smn.poker.dto.ActionInfo;
import ru.smn.poker.dto.AdminAction;
import ru.smn.poker.dto.AdminActionInfo;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.ActionType;
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

    @MessageMapping("/admin/doAction")
    public void doAction(AdminActionInfo actionInfo) {
        //TODO
        if (actionInfo.getActionType() == ActionType.ALLIN){
            final long count = gameDataService.getPlayerByName(actionInfo.getName()).orElseThrow()
                    .getChipsCount()
                    .getCount();
            actionInfo.setCount(String.valueOf(count));
        }
        final Action action = ActionType.getActionByType(actionInfo.getActionType(), Long.parseLong(actionInfo.getCount()));
        actionService.setAction(actionInfo.getName(), action);
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
