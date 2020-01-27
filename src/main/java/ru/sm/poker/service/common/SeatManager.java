package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sm.poker.enums.MessageType;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.holdem.HoldemGameManager;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.NotificationService;

import javax.annotation.PostConstruct;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.String.format;
import static ru.sm.poker.enums.MessageType.PLAYER_ALREADY_EXIST;
import static ru.sm.poker.enums.MessageType.SUCCESS_JOIN_IN_QUEUE;
import static ru.sm.poker.util.PlayerUtil.*;

@RequiredArgsConstructor
@Component
@Slf4j
public class SeatManager {

    private final HoldemGameManager holdemGameManager;
    private final SecurityNotificationService securityNotificationService;
    private final NotificationService notificationService;
    private final Queue<Player> players = new LinkedBlockingQueue<>();


    public void joinInGame(String gameName, Player player) {
        if (holdemGameManager.getPlayerByName(player.getName()).isPresent()) {
            notificationService.sendSystemMessageToUser(player.getName(), MessageType.ONLY_ONE_TABLE_MESSAGE.getMessage());
            return;
        }

        final Game game = holdemGameManager.getGameByName(gameName);
        synchronized (this) {
            final int actualSize = game.getPlayers().size();
            final int maxPlayerSize = game.getGameSettings().getMaxPlayerSize();
            if (actualSize < maxPlayerSize) {
                game.addPlayer(player);
                log.info(format("player:%s, joined to the game:%s", player.getName(), gameName));
            }
        }
    }

    public void joinInQueue(Player player) {
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


    public Queue<Player> getQueue() {
        return players;
    }


    @PostConstruct
    public void init() {
        joinInQueue(getDefaultPlayerForHoldem("1"));
        joinInQueue(getDefaultPlayerForHoldem("2"));
        joinInQueue(getDefaultPlayerForHoldem("3"));
    }
}
