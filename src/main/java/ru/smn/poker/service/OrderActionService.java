package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.smn.poker.action.NextPlayerSelector;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.game.TableSettings;

import java.util.List;

import static ru.smn.poker.util.HistoryUtil.canMoveNext;

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
    public boolean start(TableSettings tableSettings) {
        final List<PlayerEntity> sortedPlayers = sortService.sort(
                tableSettings.getPlayers(),
                tableSettings.getStageType()
        );

        PlayerEntity activePlayer = null;

        while (true) {
            if (tableSettings.isOnePlayerLeft()) {
                return true;
            }
            if (tableSettings.playersInAllIn()) {
                securityNotificationService.sendToAllWithSecurityWhoIsNotInTheGame(tableSettings);
                return false;
            }
            if (canMoveNext(tableSettings)) {
                return false;
            }
            activePlayer = nextPlayerSelector.getPlayerForAction(sortedPlayers, activePlayer);
            actionServiceHoldem.waitPlayerAction(activePlayer, tableSettings);

            if (tableSettings.getStageType() == StageType.RIVER){
                return true;
            }
        }
    }

}
