package ru.sm.poker.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.sm.poker.game.Game;

@Component
@RequiredArgsConstructor
public class SocketListeners {

    private final Game game;

    @EventListener(SessionDisconnectEvent.class)
    public void handleWebsocketDisconnectListener(SessionDisconnectEvent event) {
        game.removePlayer(game.getPlayerByName(event.getSessionId()));
    }

}
