package ru.sm.poker.service.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.sm.poker.action.Action;
import ru.sm.poker.action.ExecutableAction;
import ru.sm.poker.action.holdem.Wait;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.ErrorType;
import ru.sm.poker.enums.InformationType;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.game.SecurityService;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.common.*;

import java.util.Optional;
import java.util.Timer;

import static java.lang.String.format;
import static ru.sm.poker.enums.ErrorType.*;

@RequiredArgsConstructor
@Slf4j
@Component
public class HoldemActionService implements ActionService {

    private final SecurityService holdemSecurityService;
    private final GameManager holdemGameManager;
    private final SecurityNotificationService securityNotificationService;
    private final SimpleNotificationService simpleNotificationService;
    private final TimeBankService timeBankService = new TimeBankService();
    private final RoundSettingsService roundSettingsService;
    private final GameService gameService;

    @Override
    public void setUnSetAfkPlayer(String playerName) {
        final Optional<Pair<String, Player>> playerByName = holdemGameManager.getPlayerByName(playerName);

        if (playerByName.isEmpty()) {
            log.info(format(FIND_PLAYER_ERROR.getMessage(), playerByName));
            return;
        }

        final Pair<String, Player> playerPair = playerByName.get();
        final Game game = holdemGameManager.getGames().get(playerPair.getLeft());
        final Player player = playerPair.getRight();

        if (game.getRoundSettings() == null || player == null) {
            log.info(format(SETTINGS_NOT_FOUND.getMessage(), playerName));
            return;
        }

        final RoundSettingsDTO roundSettings = game.getRoundSettings();
        player.setStateType(player.getStateType() == StateType.AFK ? StateType.IN_GAME : StateType.AFK);

        securityNotificationService.sendToAllWithSecurity(roundSettings);
        log.info(format(InformationType.CHANGED_STATE_TYPE_INFO.getMessage(), playerName, player.getStateType()));
    }


    @Override
    public void setAction(String playerName, Action action) {
        final Optional<Pair<String, Player>> playerByName = holdemGameManager.getPlayerByName(playerName);
        if (playerByName.isEmpty()) {
            log.info(format(FIND_PLAYER_ERROR.getMessage(), playerName));
            return;
        }
        final Pair<String, Player> pairGameAndPlayer = playerByName.get();
        final Player player = pairGameAndPlayer.getRight();
        if (!holdemSecurityService.isLegalPlayer(pairGameAndPlayer.getLeft(), player)) {
            simpleNotificationService.sendErrorToUser(playerName, format(ErrorType.QUEUE_ERROR.getMessage(), player.getName()));
            log.info(format(QUEUE_ERROR.getMessage(), player.getName()));
            return;
        }
        player.setAction(action);
    }

    @Override
    public void waitUntilPlayerWillHasAction(Player player, RoundSettingsDTO roundSettingsDTO) {
        setPlayerWait(player);
        roundSettingsService.setActivePlayer(roundSettingsDTO, player);
        securityNotificationService.sendToAllWithSecurity(roundSettingsDTO);
        waitPlayerAction(player, roundSettingsDTO);
        roundSettingsService.setInActivePlayer(roundSettingsDTO, player);
    }


    public void setPlayerWait(Player playerWait) {
        playerWait.setAction(new Wait());
    }


    private void waitPlayerAction(Player player, RoundSettingsDTO roundSettingsDTO) {
        final Timer timer = timeBankService.activateTimeBank(player);
        while (true) {
            if (!(player.getAction() instanceof Wait)) {
                timer.cancel();
                doAction(player, roundSettingsDTO);
                break;
            }
        }
    }


    private void doAction(Player player, RoundSettingsDTO roundSettingsDTO) {
        final Action action = player.getAction();
        if (action instanceof ExecutableAction) {
            ((ExecutableAction) action).doAction(roundSettingsDTO, player, gameService, this);
        }
    }
}
