package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.holdem.HoldemGameManager;
import ru.sm.poker.model.Player;
import ru.sm.poker.service.NotificationService;

import static java.lang.String.format;

@RequiredArgsConstructor
@Component
public class SeatManager {

    private final HoldemGameManager holdemGameManager;
    private final SecurityNotificationService securityNotificationService;
    private final NotificationService notificationService;

    public void joinInGame(String gameName, Player player) {
        synchronized (this) {
            notificationService.sendToAll(
                    holdemGameManager.joinInGame(gameName, player).getMessage()
            );
        }
        final Game game = holdemGameManager.getGames().get(gameName);
        if (game != null) {
            final RoundSettingsDTO roundSettings = game.getRoundSettings();
            if (roundSettings != null) {
                securityNotificationService.sendToAllWithSecurity(roundSettings);
                return;
            }
        }
    }

    public void joinInQueue(Player player) {
        notificationService.sendToAll(
                format(holdemGameManager.joinInQueue(player).getMessage(), player.getName())
        );
    }

}
