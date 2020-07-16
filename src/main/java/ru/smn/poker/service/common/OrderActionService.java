package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.smn.poker.dto.Player;
import ru.smn.poker.game.RoundSettings;
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
    public boolean start(RoundSettings roundSettings) {
        final List<Player> sortedPlayers = PlayerUtil.getPlayersInGame(
                SortUtil.sort(roundSettings.getPlayers(), roundSettings.getStageType())
        );
        while (true) {
            if (roundSettings.playersInAllIn()) {
                securityNotificationService.sendToAllWithSecurityWhoIsNotInTheGame(roundSettings);
                return false;
            }
            if (canMoveNextAndStageRiver(roundSettings)) {
                securityNotificationService.sendToAllWithSecurityWhoIsNotInTheGame(roundSettings);
                return false;
            }
            if (canMoveNext(roundSettings)) {
                return false;
            }
            for (Player player : PlayerUtil.getPlayersInGame(sortedPlayers)) {
                if (player.isNotFirstMoveOnBigBlind() && canMoveNext(roundSettings)) {
                    return false;
                }
                //THIS IS FOR HU
                if (roundSettings.getStageType() != StageType.PREFLOP && canMoveNext(roundSettings)){
                    return false;
                }
                if (roundSettings.isOnePlayerLeft()) {
                    return true;
                }
                if (player.isInAllIn()) {
                    continue;
                }
                actionServiceHoldem.waitUntilPlayerWillHasAction(player, roundSettings);
            }
        }
    }
}
