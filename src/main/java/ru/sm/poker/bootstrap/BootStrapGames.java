package ru.sm.poker.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sm.poker.entities.PlayerEntity;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.game.Game;
import ru.sm.poker.entities.GameEntity;
import ru.sm.poker.service.GameManagementService;
import ru.sm.poker.service.OrderService;
import ru.sm.poker.service.common.GameService;
import ru.sm.poker.util.PlayerUtil;

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
                PlayerUtil.getDefaultPlayerForHoldem("1"),
                PlayerUtil.getDefaultBotForHoldem("3")),
                GameType.HOLDEM_HU,
                orderService);

        gameManagementService.saveGame(game);
    }
}
