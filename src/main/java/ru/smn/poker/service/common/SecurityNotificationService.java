package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.smn.poker.dto.RoundSettings;
import ru.smn.poker.service.SecurityService;
import ru.smn.poker.util.PlayerUtil;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SecurityNotificationService {
    private final SimpleNotificationService simpleNotificationService;
    private final SecurityService securityService;

    public void sendToAllWithSecurityWhoIsNotInTheGame(RoundSettings roundSettings) {
        final List<String> filter = PlayerUtil.getNamesOfPlayersInGame(roundSettings.getPlayers());

        final RoundSettings secureSettings = securityService.secureCards(filter, roundSettings);

        secureSettings.getPlayers()
                .forEach(player -> simpleNotificationService.sendGameInformationToUser(player.getName(), secureSettings));
    }

    public void sendToAllWithSecurity(RoundSettings roundSettings) {
        roundSettings.getPlayers().forEach(player -> sendToUserWithSecurity(roundSettings, player.getName()));
    }

    public void sendToUserWithSecurity(RoundSettings roundSettings, String userName) {
        simpleNotificationService.sendGameInformationToUser(userName, securityService.secureCards(List.of(userName), roundSettings));
    }
}
