package ru.sm.poker.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sm.poker.service.GameManagementService;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.service.OrderService;
import ru.sm.poker.util.PlayerUtil;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Service
@AllArgsConstructor
public class BootStrapGames {
    private final GameManagementService gameManagementService;
    private final OrderService orderService;

    @PostConstruct
    public void init() {
        gameManagementService.createGame(Arrays.asList(
                PlayerUtil.getDefaultBotForHoldem("1"),
                PlayerUtil.getDefaultBotForHoldem("2")),
                GameType.HOLDEM_FULL,
                orderService,
                true);
    }
}
