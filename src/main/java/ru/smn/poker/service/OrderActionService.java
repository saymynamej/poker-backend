package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.smn.poker.action.NextPlayerSelector;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.RoundSettings;

import java.util.List;

import static ru.smn.poker.util.HistoryUtil.canMoveNext;
import static ru.smn.poker.util.HistoryUtil.canMoveNextAndStageRiver;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderActionService implements OrderService {
    private final ActionService actionServiceHoldem;
    private final SecurityNotificationService securityNotificationService;
    private final SortService sortService;
    private final NextPlayerSelector nextPlayerSelector;

    /*
     * @return true if need skip next stages, false if not
     * */
    @Override
    public boolean start(RoundSettings roundSettings) {
        final List<PlayerEntity> sortedPlayers = sortService.sort(
                roundSettings.getPlayers(),
                roundSettings.getStageType()
        );

        PlayerEntity activePlayer = null;

        while (true) {
            if (roundSettings.isOnePlayerLeft()) {
                return true;
            }
            if (roundSettings.playersInAllIn()) {
                securityNotificationService.sendToAllWithSecurityWhoIsNotInTheGame(roundSettings);
                return false;
            }
            if (canMoveNext(roundSettings) && roundSettings.isNotFirstMoveOnBigBlind()) {
                return false;
            }
            activePlayer = nextPlayerSelector.getPlayerForAction(sortedPlayers, activePlayer);
            actionServiceHoldem.waitPlayerAction(activePlayer, roundSettings);
        }
    }

}
