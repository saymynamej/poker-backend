package ru.sm.poker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.service.OrderService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/game/create")
public class GameForAdminController {
    private final GameManager gameManager;
    private final OrderService orderService;

    @PostMapping("/api/game/create")
    public void createGame(GameType gameType, String gameName) {
        final List<PlayerDTO> playerDTOList = new ArrayList<>();
        gameManager.createGame(playerDTOList, gameType, orderService);
        gameManager.startGame(gameName);
    }
}
