package ru.smn.poker.service.classic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.smn.poker.action.NextPlayerSelector;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.OrderActionService;
import ru.smn.poker.service.SortService;
import ru.smn.poker.service.common.SecurityNotificationService;

import java.util.List;

import static ru.smn.poker.util.HistoryUtil.canMoveNext;

@RequiredArgsConstructor
@Service
@Slf4j
public class ClassicOrderActionService implements OrderActionService {
    private final ActionService actionServiceHoldem;
    private final SecurityNotificationService securityNotificationService;
    private final SortService sortService;
    private final NextPlayerSelector nextPlayerSelector;

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
        }
    }

}
