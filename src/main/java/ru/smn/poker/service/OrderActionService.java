package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.util.PlayerUtil;

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


    /*
    * @return true if need skip next stages, false or not
    * */
    @Override
    public boolean start(RoundSettings roundSettings) {
        final List<PlayerEntity> sort = sortService.sort(
                roundSettings.getPlayers(),
                roundSettings.getStageType()
        );
        final List<PlayerEntity> sortedPlayers = PlayerUtil.getPlayersInGame(
                sort
        );

        while (true) {
            if (roundSettings.isOnePlayerLeft()){
                return true;
            }
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
            for (PlayerEntity player : PlayerUtil.getPlayersInGame(sortedPlayers)) {
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
                actionServiceHoldem.waitPlayerAction(player, roundSettings);
            }
        }
    }
}
