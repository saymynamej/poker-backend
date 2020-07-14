package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.dto.Player;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.service.ActionService;
import ru.smn.poker.service.OrderService;
import ru.smn.poker.util.PlayerUtil;
import ru.smn.poker.util.SortUtil;

import java.util.List;

import static ru.smn.poker.util.HistoryUtil.canMoveNext;
import static ru.smn.poker.util.HistoryUtil.canMoveNextAndStageRiver;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderActionService implements OrderService {
    private final ActionService actionServiceHoldem;
    private final SecurityNotificationService securityNotificationService;

    @Override
    public boolean start(HoldemRoundSettings holdemRoundSettings) {
        final List<Player> sortedPlayers = PlayerUtil.getPlayersInGame(
                SortUtil.sort(holdemRoundSettings.getPlayers(), holdemRoundSettings.getStageType())
        );
        while (true) {
            if (holdemRoundSettings.playersInAllIn()) {
                securityNotificationService.sendToAllWithSecurityWhoIsNotInTheGame(holdemRoundSettings);
                return false;
            }
            if (canMoveNextAndStageRiver(holdemRoundSettings)) {
                securityNotificationService.sendToAllWithSecurityWhoIsNotInTheGame(holdemRoundSettings);
                return false;
            }
            if (canMoveNext(holdemRoundSettings)) {
                return false;
            }
            for (Player player : PlayerUtil.getPlayersInGame(sortedPlayers)) {
                if (player.isNotFirstMoveOnBigBlind() && canMoveNext(holdemRoundSettings)) {
                    return false;
                }
                //THIS IS FOR HU
                if (holdemRoundSettings.getStageType() != StageType.PREFLOP && canMoveNext(holdemRoundSettings)){
                    return false;
                }
                if (holdemRoundSettings.isOnePlayerLeft()) {
                    return true;
                }
                if (player.isInAllIn()) {
                    continue;
                }
                actionServiceHoldem.waitUntilPlayerWillHasAction(player, holdemRoundSettings);
            }
        }
    }
}
