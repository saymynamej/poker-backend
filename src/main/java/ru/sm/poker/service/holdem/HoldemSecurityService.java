package ru.sm.poker.service.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;
import ru.sm.poker.game.Game;
import ru.sm.poker.service.GameDataService;
import ru.sm.poker.service.SecurityService;
import ru.sm.poker.util.RoundSettingsUtil;

import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class HoldemSecurityService implements SecurityService {
    private final GameDataService holdemGameDataService;

    @Override
    public boolean isLegalPlayer(String gameName, Player player) {
        final Game game = holdemGameDataService.getGameByName(gameName);
        if (player != null && game.getRoundSettings() != null && game.getRoundSettings().getActivePlayer() != null) {
            return game.getRoundSettings()
                    .getActivePlayer()
                    .equals(player);
        }

        assert player != null;
        log.info(String.format("cannot define legality for player:%s and game: %s",
                player.getName(),
                gameName)
        );

        return false;
    }

    @Override
    public HoldemRoundSettings secureCards(List<String> filterName, HoldemRoundSettings holdemRoundSettings) {
        return RoundSettingsUtil.copyWithSecureCard(holdemRoundSettings, filterName);
    }

    @Override
    public boolean isLegalPlayer(Player player) {
        return isLegalPlayer(player.getGameName(), player);
    }
}
