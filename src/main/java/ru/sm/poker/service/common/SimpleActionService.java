package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.sm.poker.action.Action;
import ru.sm.poker.action.ExecutableAction;
import ru.sm.poker.action.holdem.Fold;
import ru.sm.poker.action.holdem.Wait;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.Player;
import ru.sm.poker.enums.InformationType;
import ru.sm.poker.enums.MessageType;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.game.SecurityService;
import ru.sm.poker.service.ActionService;

import java.util.Optional;
import java.util.Timer;

import static java.lang.String.format;
import static ru.sm.poker.enums.MessageType.*;

@RequiredArgsConstructor
@Slf4j
@Component
public class SimpleActionService implements ActionService {

    private final SecurityService holdemSecurityService;
    private final GameManager holdemGameManager;
    private final SecurityNotificationService securityNotificationService;
    private final SimpleNotificationService simpleNotificationService;
    private final TimeBankService timeBankService = new TimeBankService();
    private final GameService gameService;

    @Override
    public void changeStateType(String playerName) {
        final Player player = holdemGameManager.getPlayerByName(playerName)
                .orElseThrow(() -> new RuntimeException("cannot find player with name:" + playerName));

        if (player.hasGame()) {
            final Game game = holdemGameManager.getGameByName(player.getGameName());
            if (game.getRoundSettings() == null) {
                log.info(format(SETTINGS_NOT_FOUND.getMessage(), playerName));
                return;
            }
            final HoldemRoundSettingsDTO roundSettings = game.getRoundSettings();
            setStateType(player);
            securityNotificationService.sendToAllWithSecurity(roundSettings);
            log.info(format(InformationType.CHANGED_STATE_TYPE_INFO.getMessage(), playerName, player.getStateType()));
        }
    }

    private void setStateType(Player player){
        if (player.getStateType() == StateType.IN_GAME){
            player.setStateType(StateType.AFK);
            player.setAction(new Fold());
            return;
        }
        player.setStateType(StateType.IN_GAME);
        player.setAction(new Wait());
    }


    @Override
    public void setAction(String playerName, Action action) {
        final Player player = holdemGameManager.getPlayerByName(playerName)
                .orElseThrow(() -> new RuntimeException("cannot find player with name:" + playerName));;

        if (!holdemSecurityService.isLegalPlayer(player.getGameName(), player)) {
            simpleNotificationService.sendSystemMessageToUser(playerName, format(MessageType.QUEUE_ERROR.getMessage(), player.getName()));
            log.info(format(QUEUE_ERROR.getMessage(), player.getName()));
            return;
        }
        player.setAction(action);
    }

    @Override
    public void waitUntilPlayerWillHasAction(Player player, HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        log.info("waiting action from player:" + player.getName());
        setPlayerWait(player);
        gameService.setActivePlayer(holdemRoundSettingsDTO, player);
        securityNotificationService.sendToAllWithSecurity(holdemRoundSettingsDTO);
        waitPlayerAction(player, holdemRoundSettingsDTO);
        gameService.setInActivePlayer(holdemRoundSettingsDTO, player);
    }

    public void setPlayerWait(Player playerWait) {
        playerWait.setAction(new Wait());
    }

    private void waitPlayerAction(Player player, HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        final Pair<Timer, Long> timeBank = timeBankService.activateTimeBank(player);

        while (true) {
            if (player.isNotInGame()) {
                break;
            }
            if (player.didAction()) {
                timeBankService.cancel(timeBank.getValue(), player, timeBank.getKey());
                doAction(player, holdemRoundSettingsDTO);
                break;
            }
        }
    }

    private void doAction(Player player, HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        final Action action = player.getAction();
        if (action instanceof ExecutableAction) {
            ((ExecutableAction) action).doAction(holdemRoundSettingsDTO, player, gameService, this);
            log.info("player: " + player.getName() + " did action:" + action);
        }
    }
}
