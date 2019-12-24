package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.SecurityService;
import ru.sm.poker.model.Player;
import ru.sm.poker.util.PlayerUtil;
import ru.sm.poker.util.RoundSettingsUtil;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HoldemSecurityService implements SecurityService {

    private final HoldemManager holdemManager;

    @Override
    public boolean isLegalPlayer(String gameName, Player player) {
        final Game game = holdemManager.getAllGames().get(gameName);
        if (game != null && player != null) {
            return game
                    .getRoundSettings()
                    .getActivePlayer()
                    .equals(player);
        }
        return false;
    }


    @Override
    public RoundSettingsDTO secureCards(String name, RoundSettingsDTO roundSettingsDTO) {

        final List<Player> playersWithSecureCards = PlayerUtil.copies(roundSettingsDTO.getPlayers());

        playersWithSecureCards.forEach(player ->  {
            if (!player.getName().equals(name)){
                player.addCards(Collections.emptyList());
            }
        });

        return RoundSettingsUtil.copyWithSecureCard(roundSettingsDTO, playersWithSecureCards);
    }

    @Override
    public boolean isLegalPlayer(Player player) {
        return isLegalPlayer(player.getGameName(), player);
    }
}
