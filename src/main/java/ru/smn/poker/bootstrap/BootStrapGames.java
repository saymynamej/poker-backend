package ru.smn.poker.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.repository.GameRepository;
import ru.smn.poker.service.GameManagementService;
import ru.smn.poker.service.common.GameService;
import ru.smn.poker.service.OrderActionService;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
public class BootStrapGames {
    private final GameManagementService gameManagementService;
    private final GameService gameService;
    private final OrderActionService orderActionService;
    private final PasswordEncoder passwordEncoder;
    private final GameRepository gameRepository;

//    @PostConstruct
//    public void init() {
//        gameManagementService.restoreAll();
//    }

    @PostConstruct
    public void init2() {
        gameManagementService.create(2, 5000L, GameType.HOLDEM_HU);
    }


}
