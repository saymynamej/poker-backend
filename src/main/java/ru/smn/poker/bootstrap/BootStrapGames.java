package ru.smn.poker.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.entities.GameEntity;
import ru.smn.poker.service.GameManagementService;
import ru.smn.poker.service.common.GameService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@AllArgsConstructor
public class BootStrapGames {
    private final GameManagementService gameManagementService;
    private final GameService gameService;

//    @PostConstruct
//    public void init() {
//        final List<GameEntity> all = gameService.findAll();
//        for (GameEntity gameEntity : all) {
//            gameManagementService.restoreGame(gameEntity);
//        }
//    }

}
