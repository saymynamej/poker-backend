package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.Game;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.service.GameDataService;
import ru.smn.poker.service.SecurityService;
import ru.smn.poker.util.RoundSettingsUtil;

import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class SimpleSecurityService implements SecurityService {
    private final GameDataService holdemGameDataService;

    @Override
    public boolean isLegalPlayer(String gameName, PlayerEntity player) {
        final Game game = holdemGameDataService.getGameByName(gameName);
        if (player != null && game.getTableSettings() != null && game.getTableSettings().getActivePlayer() != null) {
            return game.getTableSettings()
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
    public TableSettings secureCards(List<String> filterName, TableSettings tableSettings) {
        return RoundSettingsUtil.copyWithSecureCard(tableSettings, filterName);
    }

    @Override
    public boolean isLegalPlayer(PlayerEntity player) {
        return isLegalPlayer(player.getGameName(), player);
    }
}
