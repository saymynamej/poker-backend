package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.game.SecurityService;
import ru.sm.poker.model.Player;

import java.util.List;
import java.util.stream.Collectors;

import static ru.sm.poker.util.PlayerUtil.getPlayersInGame;

@Component
@RequiredArgsConstructor
public class SecurityNotificationService {
    private final SimpleNotificationService simpleNotificationService;
    private final SecurityService securityService;

    public void sendToAllWithSecurityWhoIsNotInTheGame(RoundSettingsDTO roundSettingsDTO) {
        final List<String> filter = getPlayersInGame(roundSettingsDTO.getPlayers())
                .stream()
                .map(Player::getName)
                .collect(Collectors.toList());

        final RoundSettingsDTO secureSettings = securityService.secureCards(filter, roundSettingsDTO);
        secureSettings.getPlayers()
                .forEach(player -> simpleNotificationService.sendToUser(player.getName(), secureSettings));

    }

    public void sendToAllWithSecurity(RoundSettingsDTO roundSettingsDTO) {
        roundSettingsDTO.getPlayers().forEach(player -> {
            sendToUserWithSecurity(roundSettingsDTO, player.getName());
        });
    }

    public void sendToUserWithSecurity(RoundSettingsDTO roundSettingsDTO, String userName) {
        simpleNotificationService.sendToUser(userName, securityService.secureCards(List.of(userName), roundSettingsDTO));
    }
}
