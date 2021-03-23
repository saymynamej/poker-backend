package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.MessageType;
import ru.smn.poker.game.Table;
import ru.smn.poker.service.NotificationService;
import ru.smn.poker.service.SeatManager;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.String.format;
import static ru.smn.poker.enums.MessageType.PLAYER_ALREADY_EXIST;
import static ru.smn.poker.enums.MessageType.SUCCESS_JOIN_IN_QUEUE;

@RequiredArgsConstructor
@Component
@Slf4j
public class SimpleSeatManager implements SeatManager {

    private final SimpleGameDataService commonGameManager;
    private final NotificationService notificationService;
    private final Queue<PlayerEntity> players = new LinkedBlockingQueue<>();

    @Override
    public void joinInGame(String gameName, PlayerEntity player) {
        synchronized (this) {
            if (commonGameManager.getPlayerByName(player.getName()).isPresent()) {
                notificationService.sendSystemMessageToUser(player.getName(), MessageType.ONLY_ONE_TABLE_MESSAGE.getMessage());
                return;
            }
            final Table game = commonGameManager.getGameByName(gameName);

            game.addPlayer(player);
        }
    }

    @Override
    public void joinInQueue(PlayerEntity player) {
        synchronized (this) {
            final boolean isExist = players.contains(player);
            if (isExist) {
                notificationService.sendGameInformationToAll(
                        format(PLAYER_ALREADY_EXIST.getMessage(), player.getName())
                );
                return;
            }
            players.add(player);
            notificationService.sendGameInformationToAll(
                    format(SUCCESS_JOIN_IN_QUEUE.getMessage(), player.getName())
            );
        }
    }

    @Override
    public void leaveGame(String playerName, String gameName) {

    }

    @Override
    public Queue<PlayerEntity> getQueue() {
        return players;
    }

}
