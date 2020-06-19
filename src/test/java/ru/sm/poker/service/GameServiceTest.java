package ru.sm.poker.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.sm.poker.action.holdem.Call;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.service.common.GameService;

import static ru.sm.poker.util.DTOUtilTest.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Test
    public void testDoAction(){
        final long countCall = 4;
        final HoldemRoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO();
        final PlayerDTO player = getPlayer();
        final Call call = new Call(countCall);
        gameService.doAction(player, roundSettingsDTO, call.getCount(), call.getCount());
        Assertions.assertEquals(DEFAULT_CHIPS_COUNT - call.getCount(), player.getChipsCount());
        Assertions.assertEquals(call.getCount(), roundSettingsDTO.getLastBet());
        Assertions.assertEquals(
                call.getCount() + roundSettingsDTO.getBigBlindBet() + roundSettingsDTO.getSmallBlindBet(),
                roundSettingsDTO.getBank()
        );
    }
}
