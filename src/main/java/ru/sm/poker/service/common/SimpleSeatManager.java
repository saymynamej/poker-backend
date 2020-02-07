package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.MessageType;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.common.CommonGameManager;
import ru.sm.poker.service.NotificationService;
import ru.sm.poker.service.SeatManager;

import javax.annotation.PostConstruct;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.String.format;
import static ru.sm.poker.enums.MessageType.PLAYER_ALREADY_EXIST;
import static ru.sm.poker.enums.MessageType.SUCCESS_JOIN_IN_QUEUE;
import static ru.sm.poker.util.PlayerUtil.getDefaultPlayerForHoldem;

@RequiredArgsConstructor
@Component
@Slf4j
public class SimpleSeatManager implements SeatManager {

    private final CommonGameManager commonGameManager;
    private final SecurityNotificationService securityNotificationService;
    private final NotificationService notificationService;
    private final Queue<PlayerDTO> players = new LinkedBlockingQueue<>();


    @Override
    public void joinInGame(String gameName, PlayerDTO playerDTO) {
        synchronized (this) {
            if (commonGameManager.getPlayerByName(playerDTO.getName()).isPresent()) {
                notificationService.sendSystemMessageToUser(playerDTO.getName(), MessageType.ONLY_ONE_TABLE_MESSAGE.getMessage());
                return;
            }
            final Game game = commonGameManager.getGameByName(gameName);
            final int actualSize = game.getPlayers().size();
            final int maxPlayerSize = game.getGameSettings().getMaxPlayerSize();
            if (actualSize < maxPlayerSize) {
                game.addPlayer(playerDTO);
                log.info(format("player:%s, joined to the game:%s", playerDTO.getName(), gameName));
            }
        }
    }

    @Override
    public void joinInQueue(PlayerDTO playerDTO) {
        synchronized (this) {
            final boolean isExist = players.contains(playerDTO);
            if (isExist) {
                notificationService.sendGameInformationToAll(
                        format(PLAYER_ALREADY_EXIST.getMessage(), playerDTO.getName())
                );
                return;
            }
            players.add(playerDTO);
            notificationService.sendGameInformationToAll(
                    format(SUCCESS_JOIN_IN_QUEUE.getMessage(), playerDTO.getName())
            );
        }
    }


    @Override
    public void leaveGame(String playerName, String gameName) {
        final Game game = commonGameManager.getGameByName(gameName);
        if (game != null) {
            if (game.removePlayer(playerName)) {
                log.info(format("player %s leave the game %s", playerName, gameName));
            }
        }

    }

    @Override
    public Queue<PlayerDTO> getQueue() {
        return players;
    }


    @PostConstruct
    public void init() {
        joinInQueue(getDefaultPlayerForHoldem("1"));
        joinInQueue(getDefaultPlayerForHoldem("2"));
        joinInQueue(getDefaultPlayerForHoldem("3"));
    }
}
