package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.game.SecurityService;
import ru.sm.poker.util.RoundSettingsUtil;

import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class HoldemSecurityService implements SecurityService {
    private final GameManager holdemGameManager;

    @Override
    public boolean isLegalPlayer(String gameName, PlayerDTO player) {
        final Game game = holdemGameManager.getGameByName(gameName);
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
    public HoldemRoundSettingsDTO secureCards(List<String> filterName, HoldemRoundSettingsDTO holdemRoundSettings) {
        return RoundSettingsUtil.copyWithSecureCard(holdemRoundSettings, filterName);
    }

    @Override
    public boolean isLegalPlayer(PlayerDTO player) {
        return isLegalPlayer(player.getGameName(), player);
    }
}
