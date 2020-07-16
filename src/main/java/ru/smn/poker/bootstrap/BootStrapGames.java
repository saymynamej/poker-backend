package ru.smn.poker.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.game.Game;
import ru.smn.poker.entities.GameEntity;
import ru.smn.poker.service.GameManagementService;
import ru.smn.poker.service.OrderService;
import ru.smn.poker.service.common.GameService;
import ru.smn.poker.util.PlayerUtil;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class BootStrapGames {
    private final GameManagementService gameManagementService;
    private final OrderService orderService;
    private final GameService gameService;

    @PostConstruct
    public void init() {
        save();
        final List<GameEntity> all = gameService.findAll();
        for (GameEntity gameEntity : all) {
            gameManagementService.restoreGame(gameEntity);
        }
    }

    public void save() {
        Game game = gameManagementService.createGame(Arrays.asList(
                PlayerUtil.getDefaultPlayerForHoldem("3"),
                PlayerUtil.getDefaultPlayerForHoldem("2")),
                GameType.HOLDEM_HU,
                orderService,
                0
        );
        gameManagementService.saveGame(game);
    }
}
