package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.SecurityService;
import ru.sm.poker.model.Player;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class HoldemSecurityService implements SecurityService {

    private final Map<String, Game> games;

    @Override
    public boolean isLegalPlayer(String gameName, Player player) {
        final Game game = games.get(gameName);
        if (game != null) {
            return game
                    .getRoundSettings()
                    .getActivePlayer()
                    .equals(player);
        }
        return false;
    }
}
