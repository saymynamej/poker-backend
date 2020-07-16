package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.smn.poker.action.Action;
import ru.smn.poker.action.CountAction;
import ru.smn.poker.action.ExecutableAction;
import ru.smn.poker.action.holdem.AllIn;
import ru.smn.poker.auto.AutoBot;
import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.dto.Player;
import ru.smn.poker.dto.ResultTime;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.enums.InformationType;
import ru.smn.poker.enums.MessageType;
import ru.smn.poker.enums.PlayerType;
import ru.smn.poker.game.Game;
import ru.smn.poker.service.GameDataService;
import ru.smn.poker.service.SecurityService;
import ru.smn.poker.service.ActionService;

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
    private final ActionLogService actionLogService;
    private final AutoBot autoBot;

    @Override
    public void changeStateType(String playerName) {
        final Player player = holdemGameDataService.getPlayerByName(playerName)
                .orElseThrow(() -> new RuntimeException("cannot find player with name:" + playerName));

        if (player.hasGame()) {
            final Game game = holdemGameDataService.getGameByName(player.getGameName());
            if (game.getRoundSettings() == null) {
                log.info(String.format(MessageType.SETTINGS_NOT_FOUND.getMessage(), playerName));
                return;
            }
            final HoldemRoundSettings roundSettings = game.getRoundSettings();
            player.changeState();
            securityNotificationService.sendToAllWithSecurity(roundSettings);
            log.info(format(InformationType.CHANGED_STATE_TYPE_INFO.getMessage(), playerName, player.getStateType()));
        }
    }


    @Override
    public void setAction(String playerName, Action action) {
        final Player player = holdemGameDataService.getPlayerByName(playerName)
                .orElseThrow(() -> new RuntimeException("cannot find player with name:" + playerName));

        action = changeCallOnAllInIfNeeded(action, player);

        if (!holdemSecurityService.isLegalPlayer(player.getGameName(), player)) {
            simpleNotificationService.sendSystemMessageToUser(playerName, format(MessageType.QUEUE_ERROR.getMessage(), player.getName()));
            log.info(String.format(MessageType.QUEUE_ERROR.getMessage(), player.getName()));
            return;
        }
        player.setAction(action);
    }

    private Action changeCallOnAllInIfNeeded(Action action, Player player) {
        if (action.getActionType() == ActionType.CALL || action.getActionType() == ActionType.RAISE) {
            final CountAction countAction = (CountAction) action;
            if (countAction.getCount() == player.getChipsCount()) {
                action = new AllIn(countAction.getCount());
            }
        }
        return action;
    }

    @Override
    public void waitUntilPlayerWillHasAction(Player player, HoldemRoundSettings holdemRoundSettings) {
        log.info("waiting action from player:" + player.getName());
        player.setWait();
        gameService.setActivePlayer(holdemRoundSettings, player);
        securityNotificationService.sendToAllWithSecurity(holdemRoundSettings);
        waitPlayerAction(player, holdemRoundSettings);
        gameService.setInActivePlayer(holdemRoundSettings, player);
    }

    private void waitPlayerAction(Player player, HoldemRoundSettings holdemRoundSettings) {
//        if (player.getPlayerType() == PlayerType.BOT){
//            autoBot.auto(player, holdemRoundSettings);
//            return;
//        }

        final ResultTime timer = simpleTimeBankService.activateTime(player);
        while (true) {
            if (player.isNotInGame()) {
                break;
            }
            if (holdemRoundSettings.isOnePlayerLeft()) {
                break;
            }
            if (player.didAction()) {
                simpleTimeBankService.cancel(timer, player);
                doAction(player, holdemRoundSettings);
                break;
            }
        }
    }


    public void doAction(Player player, HoldemRoundSettings holdemRoundSettings) {
        final Action action = player.getAction();
        if (action instanceof ExecutableAction) {
            ((ExecutableAction) action).doAction(holdemRoundSettings, player, gameService, this);
            log.info("player: " + player.getName() + " did action:" + action);
            gameService.update(holdemRoundSettings);
        }
        actionLogService.log(player, action);
    }
}
