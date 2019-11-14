package ru.sm.poker.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.sm.poker.game.holdem.HoldemManager;
import ru.sm.poker.model.Player;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SocketListeners {
    private final HoldemManager holdemManager;

    @EventListener(SessionDisconnectEvent.class)
    public void handleWebsocketDisconnectListener(SessionDisconnectEvent event) {
        final Map<String, Player> playerByName = holdemManager.getPlayerByName(Objects.requireNonNull(event.getUser()).getName());
        playerByName.forEach(holdemManager::removePlayer);
    }
}
