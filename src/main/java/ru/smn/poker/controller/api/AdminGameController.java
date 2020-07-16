package ru.smn.poker.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.smn.poker.game.Game;
import ru.smn.poker.service.GameManagementService;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.service.OrderService;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/game")
public class AdminGameController {
    private final GameManagementService gameManagementService;
    private final OrderService orderService;

    @PostMapping("/create")
    public void createGame(GameType gameType) {
        final Game game = gameManagementService.createGame(
                new ArrayList<>(),
                gameType,
                orderService, 0);
        gameManagementService.restoreGame(game);
    }
}
