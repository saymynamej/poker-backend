package ru.sm.poker.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.sm.poker.game.Game;
import ru.sm.poker.model.Player;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SocketListeners {
    private final Game game;

    @EventListener(SessionDisconnectEvent.class)
    public void handleWebsocketDisconnectListener(SessionDisconnectEvent event) {
        final Optional<Player> player = game.getPlayerByName(Objects.requireNonNull(event.getUser()).getName());
        player.ifPresent(game::removePlayer);
    }
}
