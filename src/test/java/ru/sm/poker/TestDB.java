package ru.sm.poker;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.model.Card;
import ru.sm.poker.model.Player;
import ru.sm.poker.repository.CardRepository;
import ru.sm.poker.repository.PlayerRepository;

import java.util.Arrays;
import java.util.Collections;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("dev")
public class TestDB {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CardRepository cardRepository;


    @Test
    public void test() {


        final Player player = Player.builder()
                .name("fail")
                .build();
        playerRepository.save(player);


        final Card card = Card.builder()
                .cardType(CardType.A_C)
                .player(player)
                .build();

        cardRepository.save(card);


    }
}
