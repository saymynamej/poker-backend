package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sm.poker.action.Action;
import ru.sm.poker.action.ExecutableAction;
import ru.sm.poker.action.holdem.AllIn;
import ru.sm.poker.action.holdem.Call;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.dto.ResultTimeDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.enums.InformationType;
import ru.sm.poker.enums.MessageType;
import ru.sm.poker.game.Game;
import ru.sm.poker.service.GameDataService;
import ru.sm.poker.service.SecurityService;
import ru.sm.poker.service.ActionService;

import static java.lang.String.format;
import static ru.sm.poker.enums.MessageType.QUEUE_ERROR;
import static ru.sm.poker.enums.MessageType.SETTINGS_NOT_FOUND;

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
        final PlayerDTO player = holdemGameDataService.getPlayerByName(playerName)
                .orElseThrow(() -> new RuntimeException("cannot find player with name:" + playerName));

        if (player.hasGame()) {
            final Game game = holdemGameDataService.getGameByName(player.getGameName());
            if (game.getRoundSettings() == null) {
                log.info(format(SETTINGS_NOT_FOUND.getMessage(), playerName));
                return;
            }
            final HoldemRoundSettingsDTO roundSettings = game.getRoundSettings();
            player.changeState();
            securityNotificationService.sendToAllWithSecurity(roundSettings);
            log.info(format(InformationType.CHANGED_STATE_TYPE_INFO.getMessage(), playerName, player.getStateType()));
        }
    }


    @Override
    public void setAction(String playerName, Action action) {
        final PlayerDTO player = holdemGameDataService.getPlayerByName(playerName)
                .orElseThrow(() -> new RuntimeException("cannot find player with name:" + playerName));

        action = changeCallOnAllInIfNeeded(action, player);

        if (!holdemSecurityService.isLegalPlayer(player.getGameName(), player)) {
            simpleNotificationService.sendSystemMessageToUser(playerName, format(MessageType.QUEUE_ERROR.getMessage(), player.getName()));
            log.info(format(QUEUE_ERROR.getMessage(), player.getName()));
            return;
        }
        player.setAction(action);
    }

    private Action changeCallOnAllInIfNeeded(Action action, PlayerDTO player) {
        if (action.getActionType() == ActionType.CALL){
            final Call call = (Call) action;
            if (call.getCount() == player.getChipsCount()){
                action = new AllIn(call.getCount());
            }
        }
        return action;
    }

    @Override
    public void waitUntilPlayerWillHasAction(PlayerDTO player, HoldemRoundSettingsDTO holdemRoundSettings) {
        log.info("waiting action from player:" + player.getName());
        player.setWait();
        gameService.setActivePlayer(holdemRoundSettings, player);
        securityNotificationService.sendToAllWithSecurity(holdemRoundSettings);
        waitPlayerAction(player, holdemRoundSettings);
        gameService.setInActivePlayer(holdemRoundSettings, player);
    }

    private void waitPlayerAction(PlayerDTO player, HoldemRoundSettingsDTO holdemRoundSettings) {
        final ResultTimeDTO timer = simpleTimeBankService.activateTime(player);
        while (true) {
            if (player.isNotInGame()) {
                break;
            }
            if (holdemRoundSettings.isOnePlayerLeft()){
                break;
            }
            if (player.didAction()) {
                simpleTimeBankService.cancel(timer, player);
                doAction(player, holdemRoundSettings);
                break;
            }
        }
    }


    public void doAction(PlayerDTO player, HoldemRoundSettingsDTO holdemRoundSettings) {
        final Action action = player.getAction();
        if (action instanceof ExecutableAction) {
            ((ExecutableAction) action).doAction(holdemRoundSettings, player, gameService, this);
            log.info("player: " + player.getName() + " did action:" + action);
        }
    }
}
