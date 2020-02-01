package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;
import ru.sm.poker.action.Action;
import ru.sm.poker.action.ExecutableAction;
import ru.sm.poker.action.holdem.Wait;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.ActionType;
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
        final Optional<PlayerDTO> optionalPlayer = holdemGameManager.getPlayerByName(playerName);

        if (optionalPlayer.isEmpty()) {
            log.info(format(FIND_PLAYER_ERROR.getMessage(), playerName));
            return;
        }

        final PlayerDTO playerDTO = optionalPlayer.get();

        if (playerDTO.getGameName() != null) {
            final Game game = holdemGameManager.getGameByName(playerDTO.getGameName());
            if (game.getRoundSettings() == null) {
                log.info(format(SETTINGS_NOT_FOUND.getMessage(), playerName));
                return;
            }

            final HoldemRoundSettingsDTO roundSettings = game.getRoundSettings();
            playerDTO.setStateType(playerDTO.getStateType() == StateType.AFK ? StateType.IN_GAME : StateType.AFK);

            securityNotificationService.sendToAllWithSecurity(roundSettings);
            log.info(format(InformationType.CHANGED_STATE_TYPE_INFO.getMessage(), playerName, playerDTO.getStateType()));
        }
    }


    @Override
    public void setAction(String playerName, Action action) {
        final Optional<PlayerDTO> optionalPlayer = holdemGameManager.getPlayerByName(playerName);
        if (optionalPlayer.isEmpty()) {
            log.info(format(FIND_PLAYER_ERROR.getMessage(), playerName));
            return;
        }
        final PlayerDTO playerDTO = optionalPlayer.get();
        if (!holdemSecurityService.isLegalPlayer(playerDTO.getGameName(), playerDTO)) {
            simpleNotificationService.sendSystemMessageToUser(playerName, format(MessageType.QUEUE_ERROR.getMessage(), playerDTO.getName()));
            log.info(format(QUEUE_ERROR.getMessage(), playerDTO.getName()));
            return;
        }
        playerDTO.setAction(action);
    }

    @Override
    public void waitUntilPlayerWillHasAction(PlayerDTO playerDTO, HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        log.info("waiting action from player:" + playerDTO.getName());
        setPlayerWait(playerDTO);
        gameService.setActivePlayer(holdemRoundSettingsDTO, playerDTO);
        securityNotificationService.sendToAllWithSecurity(holdemRoundSettingsDTO);
        waitPlayerAction(playerDTO, holdemRoundSettingsDTO);
        gameService.setInActivePlayer(holdemRoundSettingsDTO, playerDTO);
    }


    public void setPlayerWait(PlayerDTO playerDTOWait) {
        playerDTOWait.setAction(new Wait());
    }


    private void waitPlayerAction(PlayerDTO playerDTO, HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        final ImmutablePair<Timer, Long> timeBank = timeBankService.activateTimeBank(playerDTO);
        while (true) {
            if (playerDTO.getStateType() == StateType.LEAVE) {
                break;
            }
            if (!(playerDTO.getAction().getActionType() == ActionType.WAIT)) {
                timeBankService.cancel(timeBank.getValue(), playerDTO, timeBank.getKey());
                doAction(playerDTO, holdemRoundSettingsDTO);
                break;
            }
        }
    }

    private void doAction(PlayerDTO playerDTO, HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        final Action action = playerDTO.getAction();
        if (action instanceof ExecutableAction) {
            ((ExecutableAction) action).doAction(holdemRoundSettingsDTO, playerDTO, gameService, this);
            log.info("player: " + playerDTO.getName() + " did action:" + action);
        }
    }
}
