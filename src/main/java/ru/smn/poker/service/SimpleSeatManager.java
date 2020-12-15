package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.MessageType;
import ru.smn.poker.game.Game;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.String.format;
import static ru.smn.poker.enums.MessageType.PLAYER_ALREADY_EXIST;
import static ru.smn.poker.enums.MessageType.SUCCESS_JOIN_IN_QUEUE;

@RequiredArgsConstructor
@Component
@Slf4j
public class SimpleSeatManager implements SeatManager {

    private final CommonGameDataService commonGameManager;
    private final NotificationService notificationService;
    private final Queue<PlayerEntity> players = new LinkedBlockingQueue<>();

    @Override
    public void joinInGame(String gameName, PlayerEntity player) {
        synchronized (this) {
            if (commonGameManager.getPlayerByName(player.getName()).isPresent()) {
                notificationService.sendSystemMessageToUser(player.getName(), MessageType.ONLY_ONE_TABLE_MESSAGE.getMessage());
                return;
            }
            final Game game = commonGameManager.getGameByName(gameName);
            final int actualSize = game.getPlayers().size();
            final int maxPlayerSize = game.getGameSettings().getMaxPlayerSize();
            if (actualSize < maxPlayerSize) {
                game.addPlayer(player);
                log.info(format("player:%s, joined to the game:%s", player.getName(), gameName));
            }
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
        final Game game = commonGameManager.getGameByName(gameName);
        if (game.removePlayer(playerName)) {
            log.info(format("player %s leave the game %s", playerName, gameName));
        }
    }

    @Override
    public Queue<PlayerEntity> getQueue() {
        return players;
    }

}