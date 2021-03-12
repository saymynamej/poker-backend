package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.smn.poker.action.Action;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.action.ExecutableAction;
import ru.smn.poker.action.holdem.AllIn;
import ru.smn.poker.dto.ResultTime;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.enums.InformationType;
import ru.smn.poker.enums.MessageType;
import ru.smn.poker.game.Game;
import ru.smn.poker.game.TableSettings;

import static java.lang.String.format;

@RequiredArgsConstructor
@Slf4j
@Component
public class SimpleActionService implements ActionService {
    private final SecurityService holdemSecurityService;
    private final GameDataService holdemGameDataService;
    private final SecurityNotificationService securityNotificationService;
    private final SimpleNotificationService simpleNotificationService;
    private final SimpleTimeBankService simpleTimeBankService = new SimpleTimeBankService();
    private final GameService gameService;

    @Override
    public void changeStateType(String playerName) {
        final PlayerEntity player = holdemGameDataService.getPlayerByName(playerName)
                .orElseThrow(() -> new RuntimeException("cannot find player with name:" + playerName));

        if (player.hasGame()) {
            final Game game = holdemGameDataService.getGameByName(player.getGameName());
            if (game.getTableSettings() == null) {
                log.info(String.format(MessageType.SETTINGS_NOT_FOUND.getMessage(), playerName));
                return;
            }
            final TableSettings tableSettings = game.getTableSettings();
            player.changeState();
            securityNotificationService.sendToAllWithSecurity(tableSettings);
            log.info(format(InformationType.CHANGED_STATE_TYPE_INFO.getMessage(), playerName, player.getStateType()));
        }
    }


    @Override
    public void setAction(String playerName, Action action) {
        final PlayerEntity player = holdemGameDataService.getPlayerByName(playerName)
                .orElseThrow(() -> new RuntimeException("cannot find player with name:" + playerName));

        action = changeCallOnAllInIfNeeded(action, player);

        if (!holdemSecurityService.isLegalPlayer(player.getGameName(), player)) {
            simpleNotificationService.sendSystemMessageToUser(playerName, format(MessageType.QUEUE_ERROR.getMessage(), player.getName()));
            log.info(String.format(MessageType.QUEUE_ERROR.getMessage(), player.getName()));
            return;
        }
        player.setAction(action);
    }

    @Override
    public void waitPlayerAction(PlayerEntity player, TableSettings tableSettings) {
        log.info("waiting action from player:" + player.getName());
        player.setWait();
        gameService.setActivePlayer(tableSettings, player);
        securityNotificationService.sendToAllWithSecurity(tableSettings);
        final ResultTime timer = simpleTimeBankService.activateTime(player);

        while (true) {
            if (player.isNotInGame()) {
                break;
            }
            if (player.didAction()) {
                simpleTimeBankService.cancel(timer, player);
                doAction(player, tableSettings);
                break;
            }
        }
        gameService.setInActivePlayer(tableSettings, player);
    }

    public void doAction(PlayerEntity player, TableSettings tableSettings) {
        final Action action = player.getAction();
        if (action instanceof ExecutableAction) {
            ((ExecutableAction) action).doAction(tableSettings, player, gameService, this);
            log.info("player: " + player.getName() + " did action:" + action);
        }
    }

    private Action changeCallOnAllInIfNeeded(Action action, PlayerEntity player) {
        if (action.getActionType() == ActionType.CALL || action.getActionType() == ActionType.RAISE) {
            final CountAction countAction = (CountAction) action;
            if (countAction.getCount() == player.getChipsCount().getCount()) {
                action = new AllIn(countAction.getCount());
            }
        }
        return action;
    }
}
