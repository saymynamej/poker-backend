package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.game.SecurityService;
import ru.sm.poker.util.PlayerUtil;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SecurityNotificationService {
    private final SimpleNotificationService simpleNotificationService;
    private final SecurityService securityService;

    public void sendToAllWithSecurityWhoIsNotInTheGame(HoldemRoundSettings holdemRoundSettings) {
        final List<String> filter = PlayerUtil.getNamesOfPlayersInGame(holdemRoundSettings.getPlayers());

        final HoldemRoundSettings secureSettings = securityService.secureCards(filter, holdemRoundSettings);

        secureSettings.getPlayers()
                .forEach(player -> simpleNotificationService.sendGameInformationToUser(player.getName(), secureSettings));
    }

    public void sendToAllWithSecurity(HoldemRoundSettings holdemRoundSettings) {
        holdemRoundSettings.getPlayers().forEach(player -> sendToUserWithSecurity(holdemRoundSettings, player.getName()));
    }

    public void sendToUserWithSecurity(HoldemRoundSettings holdemRoundSettings, String userName) {
        simpleNotificationService.sendGameInformationToUser(userName, securityService.secureCards(List.of(userName), holdemRoundSettings));
    }
}
