package ru.smn.poker.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import ru.smn.poker.enums.MessageType;
import ru.smn.poker.game.Table;
import ru.smn.poker.service.GameDataService;
import ru.smn.poker.service.SecurityService;
import ru.smn.poker.service.common.SecurityNotificationService;
import ru.smn.poker.service.common.SimpleNotificationService;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class SocketListeners {
    private final GameDataService gameDataService;
    private final SecurityNotificationService securityNotificationService;
    private final SimpleNotificationService simpleNotificationService;
    private final SecurityService securityService;
    private final Map<String, Table> tables;

    @EventListener(SessionSubscribeEvent.class)
    public void handleWebsocketConnectListener(SessionSubscribeEvent event) {
        Optional.ofNullable(event.getUser())
                .map(Principal::getName)
                .map(gameDataService::getPlayerByName)
                .map(Optional::get)
                .map(player -> tables.get(player.getGameName()))
                .map(Table::getTableSettings)
                .ifPresent(tableSettings -> securityNotificationService.sendToUserWithSecurity(tableSettings, event.getUser().getName()));
    }

    @EventListener(SessionConnectEvent.class)
    public void handleWebsocketConnectListener(SessionConnectEvent event) {
        final Principal user = event.getUser();
        if (user != null) {
            simpleNotificationService.sendSystemMessageToAll(format(MessageType.SUCCESS_CONNECTED_TO_SERVER.getMessage(), user.getName()));
        }
    }
}
