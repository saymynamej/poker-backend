package ru.sm.poker.listeners;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.game.SecurityService;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.holdem.BroadCastService;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SocketListeners {
    private final GameManager gameManager;
    private final BroadCastService broadCastService;
    private final SecurityService securityService;


    @EventListener(SessionSubscribeEvent.class)
    public void handleWebsocketConnectListener(SessionSubscribeEvent event) {
        final Principal user = event.getUser();
        if (user != null) {

            final Optional<Pair<String, Player>> playerByName
                    = gameManager.getPlayerByName(user.getName());

            if (playerByName.isPresent()) {
                final Pair<String, Player> playerPair = playerByName.get();
                final Map<String, Game> allGames = gameManager.getGames();
                final Game game = allGames.get(playerPair.getLeft());
                final RoundSettingsDTO roundSettingsDTO = game.getRoundSettings();
                broadCastService.sendToUser(playerPair.getRight().getName(), securityService.secureCards(user.getName(), roundSettingsDTO));
            }
        }
    }
}
