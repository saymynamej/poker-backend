package ru.sm.poker.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sm.poker.converter.GameConverter;
import ru.sm.poker.dto.GameDTO;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.service.GameManagementService;
import ru.sm.poker.service.OrderService;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/game")
public class AdminGameController {
    private final GameManagementService gameManagementService;
    private final OrderService orderService;
    private final GameConverter gameConverter;

    @GetMapping("/create/{gameType}")
    public GameDTO createGame(@PathVariable String gameType) {
        return gameConverter.toGameDTO(gameManagementService.createGame(
                new ArrayList<>(),
                GameType.getGameTypeByName(gameType),
                orderService,
                true
        ));
    }

    @GetMapping("/stop/{gameName}")
    public String stopGame(@PathVariable String gameName) {
        try {
            return gameManagementService.stopGame(gameName).getGameName();
        }
        catch (RuntimeException e){
            return "not found";
        }
    }
}
