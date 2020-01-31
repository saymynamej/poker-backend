package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.game.SecurityService;
import ru.sm.poker.dto.PlayerDTO;

import java.util.List;
import java.util.stream.Collectors;

import static ru.sm.poker.util.PlayerUtil.getPlayersInGame;

@Component
@RequiredArgsConstructor
public class SecurityNotificationService {
    private final SimpleNotificationService simpleNotificationService;
    private final SecurityService securityService;

    public void sendToAllWithSecurityWhoIsNotInTheGame(HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        final List<String> filter = getPlayersInGame(holdemRoundSettingsDTO.getPlayerDTOS())
                .stream()
                .map(PlayerDTO::getName)
                .collect(Collectors.toList());

        final HoldemRoundSettingsDTO secureSettings = securityService.secureCards(filter, holdemRoundSettingsDTO);
        secureSettings.getPlayerDTOS()
                .forEach(player -> simpleNotificationService.sendGameInformationToUser(player.getName(), secureSettings));

    }

    public void sendToAllWithSecurity(HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        holdemRoundSettingsDTO.getPlayerDTOS().forEach(player -> {
            sendToUserWithSecurity(holdemRoundSettingsDTO, player.getName());
        });
    }

    public void sendToUserWithSecurity(HoldemRoundSettingsDTO holdemRoundSettingsDTO, String userName) {
        simpleNotificationService.sendGameInformationToUser(userName, securityService.secureCards(List.of(userName), holdemRoundSettingsDTO));
    }
}
