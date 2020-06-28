package ru.sm.poker.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sm.poker.converter.PlayerConverter;
import ru.sm.poker.model.GameEntity;
import ru.sm.poker.service.GameManagementService;
import ru.sm.poker.service.OrderService;
import ru.sm.poker.service.common.GameService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@AllArgsConstructor
public class BootStrapGames {
    private final GameManagementService gameManagementService;
    private final OrderService orderService;
    private final GameService gameService;

    @PostConstruct
    public void init() {
        final List<GameEntity> all = gameService.findAll();
        for (GameEntity gameEntity : all) {
            gameManagementService.createGame(
                    PlayerConverter.toDTOs(gameEntity.getPlayers()),
                    gameEntity.getGameType(),
                    orderService,
                    true,
                    false
            );
        }

    }
}
