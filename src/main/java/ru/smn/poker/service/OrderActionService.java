package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.stream.PlayerPredicates;

import java.util.List;
import java.util.stream.Stream;

import static ru.smn.poker.util.HistoryUtil.canMoveNext;
import static ru.smn.poker.util.HistoryUtil.canMoveNextAndStageRiver;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderActionService implements OrderService {
    private final ActionService actionServiceHoldem;
    private final SecurityNotificationService securityNotificationService;
    private final SortService sortService;
    private final GameService gameService;

    /*
     * @return true if need skip next stages, false if not
     * */
    @Override
    public boolean start(RoundSettings roundSettings) {
        final List<PlayerEntity> sortedPlayers = sortService.sort(
                roundSettings.getPlayers(),
                roundSettings.getStageType()
        );

        sortedPlayers.forEach(playerEntity -> playerEntity.setActive(false));

        PlayerEntity activePlayer = null;

        while (true) {
            if (roundSettings.isOnePlayerLeft()) {
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

            activePlayer = getPlayerForAction(sortedPlayers, activePlayer);
            actionServiceHoldem.waitPlayerAction(activePlayer, roundSettings);
        }
    }

    public PlayerEntity getPlayerForAction(List<PlayerEntity> sortedPlayers, PlayerEntity previousActivePlayer) {
        if (previousActivePlayer == null) {
            return sortedPlayers.get(0);
        }

        final int indexOfPreviousActivePlayer = sortedPlayers.indexOf(previousActivePlayer);

        final Stream<PlayerEntity> firstHalf = sortedPlayers.subList(indexOfPreviousActivePlayer + 1, sortedPlayers.size()).stream();

        final Stream<PlayerEntity> secondHalf = sortedPlayers.subList(0, indexOfPreviousActivePlayer + 1).stream();

       return Stream.concat(firstHalf, secondHalf)
                .filter(PlayerPredicates.playerCanMakeMove())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("cannot find active player, global error"));
    }
}
