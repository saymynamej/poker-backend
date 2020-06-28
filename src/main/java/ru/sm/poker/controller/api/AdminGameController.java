package ru.sm.poker.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping("/create")
    public void createGame(GameType gameType) {
        gameManagementService.createGame(
                new ArrayList<>(),
                gameType,
                orderService,
                true,
                true
        );
    }
}
