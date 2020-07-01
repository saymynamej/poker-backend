package ru.sm.poker;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.sm.poker.repository.CardRepository;
import ru.sm.poker.repository.GameRepository;
import ru.sm.poker.repository.PlayerRepository;

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


    }
}
