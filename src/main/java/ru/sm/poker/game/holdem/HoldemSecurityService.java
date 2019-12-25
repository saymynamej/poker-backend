package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.SecurityService;
import ru.sm.poker.model.Player;
import ru.sm.poker.util.RoundSettingsUtil;


@Component
@RequiredArgsConstructor
public class HoldemSecurityService implements SecurityService {

    private final HoldemManager holdemManager;

    @Override
    public boolean isLegalPlayer(String gameName, Player player) {
        final Game game = holdemManager.getGames().get(gameName);
        if (game != null && player != null) {
            return game
                    .getRoundSettings()
                    .getActivePlayer()
                    .equals(player);
        }
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
