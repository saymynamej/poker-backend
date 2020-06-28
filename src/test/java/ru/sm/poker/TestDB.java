package ru.sm.poker;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.GameType;
import ru.sm.poker.model.CardEntity;
import ru.sm.poker.model.GameEntity;
import ru.sm.poker.model.PlayerEntity;
import ru.sm.poker.repository.CardRepository;
import ru.sm.poker.repository.GameRepository;
import ru.sm.poker.repository.PlayerRepository;

import java.util.Collections;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("dev")
public class TestDB {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private GameRepository gameRepository;


    @Test
    public void test() {
        final PlayerEntity playerEntity = PlayerEntity.builder()
                .name("fail")
                .build();

        playerRepository.save(playerEntity);

        GameEntity gameEntity = GameEntity.builder()
                .playerEntities(Collections.singletonList(playerEntity))
                .gameType(GameType.HOLDEM_FULL)
                .build();

        playerEntity.setGameEntities(Collections.singletonList(gameEntity));


        final CardEntity cardEntity = CardEntity.builder()
                .cardType(CardType.A_C)
                .playerEntity(playerEntity)
                .build();
        cardRepository.save(cardEntity);

        gameRepository.save(gameEntity);

        System.out.println(gameEntity.getPlayerEntities().get(0).getName());

    }
}
