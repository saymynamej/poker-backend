package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.game.SecurityService;
import ru.sm.poker.model.Player;
import ru.sm.poker.util.RoundSettingsUtil;


@Component
@RequiredArgsConstructor
@Slf4j
public class HoldemSecurityService implements SecurityService {

    private final GameManager holdemGameManager;

    @Override
    public boolean isLegalPlayer(String gameName, Player player) {
        final Game game = holdemGameManager.getGames().get(gameName);
        if (game != null && player != null
                && game.getRoundSettings() != null && game.getRoundSettings().getActivePlayer() != null) {

            return game
                    .getRoundSettings()
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
    public RoundSettingsDTO secureCards(String filterName, RoundSettingsDTO roundSettingsDTO) {
        return RoundSettingsUtil.copyWithSecureCard(roundSettingsDTO, filterName);
    }

    @Override
    public boolean isLegalPlayer(Player player) {
        return isLegalPlayer(player.getGameName(), player);
    }
}
