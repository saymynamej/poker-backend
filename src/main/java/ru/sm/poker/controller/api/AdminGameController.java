package ru.sm.poker.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.holdem.HoldemGame;
import ru.sm.poker.game.holdem.HoldemRound;
import ru.sm.poker.service.GameManagementService;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.service.OrderService;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/game")
public class AdminGameController {
    private final GameManagementService gameManagementService;
    private final OrderService orderService;
    private final HoldemRound holdemRound;

    @PostMapping("/create")
    public void createGame(GameType gameType) {
        gameManagementService.createGame(
                new ArrayList<>(),
                gameType,
                orderService,
                true
        );
    }

    @PostMapping("/stop")
    public void stopGame (){
        gameManagementService.stopGame(holdemRound.getGameName());
    }
}
