package ru.sm.poker.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;
import ru.sm.poker.enums.MessageType;
import ru.sm.poker.game.Game;
import ru.sm.poker.service.GameDataService;
import ru.sm.poker.service.SecurityService;
import ru.sm.poker.service.common.SimpleNotificationService;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class SocketListeners {
    private final GameDataService gameDataService;
    private final SimpleNotificationService simpleNotificationService;
    private final SecurityService securityService;
    private final Map<String, Game> games;

    @EventListener(SessionSubscribeEvent.class)
    public void handleWebsocketConnectListener(SessionSubscribeEvent event) {
        final Principal user = event.getUser();
        if (user != null) {
            final Optional<Player> optionalPlayer = gameDataService.getPlayerByName(user.getName());
            if (optionalPlayer.isPresent()) {
                final Player player = optionalPlayer.get();
                if (player.getGameName() != null) {
                    final Game game = games.get(player.getGameName());
                    final HoldemRoundSettings holdemRoundSettings = game.getRoundSettings();
                    simpleNotificationService.sendGameInformationToUser(
                            player.getName(),
                            securityService.secureCards(List.of(user.getName()),
                                    holdemRoundSettings)
                    );
                }
            }
        }
    }


    @EventListener(SessionConnectEvent.class)
    public void handleWebsocketConnectListener(SessionConnectEvent event) {
        final Principal user = event.getUser();
        if (user != null) {
            simpleNotificationService.sendSystemMessageToAll(format(MessageType.SUCCESS_CONNECTED_TO_SERVER.getMessage(), user.getName()));
        }
    }
}
