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
    public boolean isLegalPlayer(String gameName, PlayerDTO playerDTO) {
        final Game game = holdemGameManager.getGameByName(gameName);
        if (game != null && playerDTO != null && game.getRoundSettings() != null && game.getRoundSettings().getActivePlayerDTO() != null) {

            return game.getRoundSettings()
                    .getActivePlayerDTO()
                    .equals(playerDTO);
        }

        assert playerDTO != null;
        log.info(String.format("cannot define legality for player:%s and game: %s",
                playerDTO.getName(),
                gameName)
        );

        return false;
    }

    @Override
    public HoldemRoundSettingsDTO secureCards(List<String> filterName, HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        return RoundSettingsUtil.copyWithSecureCard(holdemRoundSettingsDTO, filterName);
    }

    @Override
    public boolean isLegalPlayer(PlayerDTO playerDTO) {
        return isLegalPlayer(playerDTO.getGameName(), playerDTO);
    }
}
