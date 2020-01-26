package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.holdem.HoldemGameManager;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.NotificationService;
import ru.sm.poker.util.PlayerUtil;

import javax.annotation.PostConstruct;
import java.util.Queue;
import static java.lang.String.format;
import static ru.sm.poker.enums.ErrorType.PLAYER_ALREADY_EXIST;
import static ru.sm.poker.enums.ErrorType.SUCCESS_JOIN_IN_QUEUE;
import static ru.sm.poker.util.PlayerUtil.*;

@RequiredArgsConstructor
@Component
public class SeatManager {

    private final HoldemGameManager holdemGameManager;
    private final SecurityNotificationService securityNotificationService;
    private final NotificationService notificationService;
    private final Queue<Player> players;


    public void joinInGame(String gameName, Player player) {
        final Game game = holdemGameManager.getGameByName(gameName);

        synchronized (this) {
            if (game.getRoundSettings().getPlayers().size() < game.getMaxPlayersSize()){
                player.setStateType(StateType.IN_GAME);
                game.getRoundSettings().getPlayers().add(player);
                securityNotificationService.sendToAllWithSecurityWhoIsNotInTheGame(game.getRoundSettings());
            }
        }
    }

    public void joinInQueue(Player player) {
        final boolean isExist = players.contains(player);
        if (isExist) {
            notificationService.sendToAll(
                    format(PLAYER_ALREADY_EXIST.getMessage(), player.getName())
            );
            return;
        }
        players.add(player);
        notificationService.sendToAll(
                format(SUCCESS_JOIN_IN_QUEUE.getMessage(), player.getName())
        );
    }


    @PostConstruct
    public void init() {
        joinInQueue(getDefaultPlayerForHoldem("1"));
        joinInQueue(getDefaultPlayerForHoldem("2"));
        joinInQueue(getDefaultPlayerForHoldem("3"));
    }
}
