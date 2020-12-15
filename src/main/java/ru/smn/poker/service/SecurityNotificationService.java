package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.smn.poker.converter.RoundSettingsConverter;
import ru.smn.poker.dto.HoldemRoundSettingsDTO;
import ru.smn.poker.game.RoundSettings;
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

        final HoldemRoundSettingsDTO holdemRoundSetting = RoundSettingsConverter.toDTO(secureSettings);

        secureSettings.getPlayers().forEach(player -> simpleNotificationService.sendGameInformationToUser(
                player.getName(),
                holdemRoundSetting)
        );
    }

    public void sendToAllWithSecurity(RoundSettings roundSettings) {
        roundSettings.getPlayers().forEach(player -> sendToUserWithSecurity(roundSettings, player.getName()));
    }

    public void sendToUserWithSecurity(RoundSettings roundSettings, String userName) {
        final RoundSettings secureSettings = securityService.secureCards(List.of(userName), roundSettings);
        final HoldemRoundSettingsDTO holdemRoundSettings = RoundSettingsConverter.toDTO(secureSettings);
        simpleNotificationService.sendGameInformationToUser(userName, holdemRoundSettings);
    }
}
