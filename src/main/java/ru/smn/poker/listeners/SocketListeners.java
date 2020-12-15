package ru.smn.poker.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import ru.smn.poker.converter.RoundSettingsConverter;
import ru.smn.poker.dto.HoldemRoundSettingsDTO;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.MessageType;
import ru.smn.poker.game.Game;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.service.GameDataService;
import ru.smn.poker.service.SecurityService;
import ru.smn.poker.service.SimpleNotificationService;

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
            final Optional<PlayerEntity> optionalPlayer = gameDataService.getPlayerByName(user.getName());
            if (optionalPlayer.isPresent()) {
                final PlayerEntity player = optionalPlayer.get();
                if (player.getGameName() != null) {
                    final Game game = games.get(player.getGameName());

                    final RoundSettings roundSettings = game.getRoundSettings();

                    final RoundSettings securedRoundSettings = securityService.secureCards(
                            List.of(user.getName()),
                            roundSettings
                    );

                    final HoldemRoundSettingsDTO holdemRoundSettings = RoundSettingsConverter.toDTO(securedRoundSettings);

                    simpleNotificationService.sendGameInformationToUser(
                            player.getName(),
                            holdemRoundSettings
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
