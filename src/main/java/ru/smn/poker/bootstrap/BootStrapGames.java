package ru.smn.poker.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.repository.GameRepository;
import ru.smn.poker.service.GameManagementService;
import ru.smn.poker.service.GameService;
import ru.smn.poker.service.OrderService;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
public class BootStrapGames {
    private final GameManagementService gameManagementService;
    private final GameService gameService;
    private final OrderService orderService;
    private final PasswordEncoder passwordEncoder;
    private final GameRepository gameRepository;

//    @PostConstruct
//    public void init() {
//        gameManagementService.restoreAll();
//    }

    @PostConstruct
    public void init2() {
        gameManagementService.create(3, 5000L, GameType.HOLDEM_HU);

//        gameManagementService.createEmptyGame(GameType.HOLDEM_HU);

    }


}
