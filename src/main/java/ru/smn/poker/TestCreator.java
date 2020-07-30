package ru.smn.poker;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.service.GameManagementService;
import ru.smn.poker.service.OrderService;
import ru.smn.poker.util.PlayerUtil;

import javax.annotation.PostConstruct;

import static java.util.Arrays.asList;

@Service
@AllArgsConstructor
public class TestCreator {
    private final GameManagementService gameManagementService;
    private final OrderService orderService;

    @PostConstruct
    public void init(){
        gameManagementService.createNewGame(asList(
                PlayerUtil.getDefaultPlayerForHoldem("3"),
                PlayerUtil.getDefaultPlayerForHoldem("2")),
                GameType.HOLDEM_HU,
                orderService
        );

    }

}
