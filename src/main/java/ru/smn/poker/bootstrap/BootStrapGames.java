package ru.smn.poker.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.smn.poker.entities.ChipsCountEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.entities.PlayerSettingsEntity;
import ru.smn.poker.enums.GameType;
import ru.smn.poker.enums.PlayerType;
import ru.smn.poker.repository.GameRepository;
import ru.smn.poker.service.GameManagementService;
import ru.smn.poker.service.GameService;
import ru.smn.poker.service.OrderService;

import javax.annotation.PostConstruct;
import java.util.List;

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
//        final List<GameEntity> all = gameService.findAll();
//        for (GameEntity gameEntity : all) {
//            gameManagementService.startGame(gameEntity);
//        }
//    }
//
    @PostConstruct
    public void init2() {
        PlayerEntity player1 = PlayerEntity.builder()
                .name("3")
                .enable(true)
                .settings(PlayerSettingsEntity.builder()
                        .timeBank(60L)
                        .playerType(PlayerType.ORDINARY)
                        .build())
                .password(passwordEncoder.encode("3"))
                .build();

        PlayerEntity player2 = PlayerEntity.builder()
                .name("2")
                .enable(true)
                .settings(PlayerSettingsEntity.builder()
                        .timeBank(60L)
                        .playerType(PlayerType.ORDINARY)
                        .build())
                .password(passwordEncoder.encode("2"))
                .build();


        ChipsCountEntity chipsCountEntity1 = ChipsCountEntity.builder()
                .count(5000L)
                .build();

        ChipsCountEntity chipsCountEntity2 = ChipsCountEntity.builder()
                .count(5000L)
                .build();


        player1.setChipsCount(chipsCountEntity1);
        player2.setChipsCount(chipsCountEntity2);


        gameManagementService.createGame(
                List.of(player1, player2),
                GameType.HOLDEM_HU
        );

    }


}
