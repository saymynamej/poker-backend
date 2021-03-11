package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.smn.poker.converter.RoundSettingsConverter;
import ru.smn.poker.dto.HoldemRoundSettingsDTO;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.util.PlayerUtil;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SecurityNotificationService {
    private final SimpleNotificationService simpleNotificationService;
    private final SecurityService securityService;

    public void sendToAllWithSecurityWhoIsNotInTheGame(TableSettings tableSettings) {
        final List<String> filter = PlayerUtil.getNamesOfPlayersInGame(tableSettings.getPlayers());

        final TableSettings secureSettings = securityService.secureCards(filter, tableSettings);

        final HoldemRoundSettingsDTO holdemRoundSetting = RoundSettingsConverter.toDTO(secureSettings);

        secureSettings.getPlayers().forEach(player -> simpleNotificationService.sendGameInformationToUser(
                player.getName(),
                holdemRoundSetting)
        );
    }

    public void sendToAllWithSecurity(TableSettings tableSettings) {
        tableSettings.getPlayers().forEach(player -> sendToUserWithSecurity(tableSettings, player.getName()));
    }

    public void sendToUserWithSecurity(TableSettings tableSettings, String userName) {
        final TableSettings secureSettings = securityService.secureCards(List.of(userName), tableSettings);
        final HoldemRoundSettingsDTO holdemRoundSettings = RoundSettingsConverter.toDTO(secureSettings);
        simpleNotificationService.sendGameInformationToUser(userName, holdemRoundSettings);
    }
}
