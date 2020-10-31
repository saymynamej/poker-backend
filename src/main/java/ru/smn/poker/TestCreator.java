package ru.smn.poker;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.entities.ChipsCountEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.service.GameManagementService;
import ru.smn.poker.service.OrderService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@AllArgsConstructor
public class TestCreator {
    private final GameManagementService gameManagementService;
    private final OrderService orderService;

    @PostConstruct
    public void init() {
        PlayerEntity player1 = PlayerEntity.builder()
                .name("3")
                .build();

        PlayerEntity player2 = PlayerEntity.builder()
                .name("2")
                .build();


        ChipsCountEntity chipsCountEntity1 = ChipsCountEntity.builder()
                .count(5000L)
                .build();

        ChipsCountEntity chipsCountEntity2 = ChipsCountEntity.builder()
                .count(5000L)
                .build();

        player1.setChipsCount(chipsCountEntity1);
        player2.setChipsCount(chipsCountEntity2);

        gameManagementService.createNewGameTest(
                List.of(player1, player2),
                List.of(chipsCountEntity1, chipsCountEntity2),
                GameType.HOLDEM_HU
        );


    }

}
