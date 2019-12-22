package ru.sm.poker.listeners;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.holdem.HoldemManager;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.RoundSettings;
import ru.sm.poker.service.BroadCastService;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SocketListeners {
    private final HoldemManager holdemManager;
    private final BroadCastService broadCastService;

//    @EventListener(SessionDisconnectEvent.class)
//    public void handleWebsocketDisconnectListener(SessionDisconnectEvent event) {
////        final Optional<Pair<String, Player>> playerByName = holdemManager.getPlayerByName(Objects.requireNonNull(event.getUser()).getName());
////        playerByName.ifPresent(stringPlayerPair -> holdemManager.removePlayer(stringPlayerPair.getRight()));
//    }

    @EventListener(SessionConnectedEvent.class)
    public void handleWebsocketConnectListener(SessionConnectedEvent event) {
        final Principal user = event.getUser();
        if (user != null) {

            final Optional<Pair<String, Player>> playerByName
                    = holdemManager.getPlayerByName(user.getName());

            if (playerByName.isPresent()) {
                final Pair<String, Player> playerPair = playerByName.get();
                final Map<String, Game> allGames = holdemManager.getAllGames();
                final Game game = allGames.get(playerPair.getLeft());
                final RoundSettings roundSettings = game.getRoundSettings();
                broadCastService.sendToUser(playerPair.getRight().getName(), roundSettings);
            }
        }
    }
}
