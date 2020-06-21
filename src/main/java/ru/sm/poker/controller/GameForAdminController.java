package ru.sm.poker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.game.Game;
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
    private final List<PlayerDTO> playerDTOList = new ArrayList<>();


    @PostMapping("/api/game/create")
    public Game createGame() {
        return this.gameManager.createGame(playerDTOList, GameType.HOLDEM_HU, orderService);
    }
}
