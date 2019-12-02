package ru.sm.poker.service;


import com.github.javafaker.Faker;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.holdem.HoldemManager;
import ru.sm.poker.game.holdem.HoldemSecurityService;
import ru.sm.poker.model.Player;
import ru.sm.poker.util.ThreadUtil;

import java.util.Map;
import java.util.Optional;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class HoldemSecurityServiceTest {
    @Autowired
    private HoldemSecurityService holdemSecurityService;

    @Autowired
    private HoldemManager holdemManager;

    @Autowired
    private Map<String, Game> games;

    private final static int NINE_PLAYERS_TABLE = 9;

    private final static int CHIPS_COUNT = 5000;


    @BeforeEach
    public void before() {
        final Faker faker = new Faker();
        for (int i = 0; i < NINE_PLAYERS_TABLE; i++) {
            holdemManager.addPlayer((Player
                    .builder()
                    .name(faker.name().firstName())
                    .chipsCount(CHIPS_COUNT)
                    .build()));
        }
    }

    @Test
    void testIsLegalPlayer() {
        ThreadUtil.sleep(2);
        final Optional<Game> gameOptional = games
                .values()
                .stream()
                .findFirst();

        if (gameOptional.isPresent()) {
            final Game game = gameOptional.get();
            final Player activePlayer = game
                    .getRoundSettings()
                    .getActivePlayer();
            final String gameName = games.values()
                    .stream()
                    .map(Game::getName)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("cannot find game"));
            Assert.assertTrue(holdemSecurityService.isLegalPlayer(gameName, activePlayer));
        } else {
            throw new RuntimeException("cannot testing testIsLegalPlayer");
        }
    }
}
