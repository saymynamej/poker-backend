package ru.smn.poker.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.smn.poker.action.holdem.Call;
import ru.smn.poker.dto.Player;
import ru.smn.poker.dto.RoundSettings;
import ru.smn.poker.service.common.GameService;

import static ru.smn.poker.util.DTOUtilTest.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class GameEntityServiceTest {

    @Autowired
    private GameService gameService;

    @Test
    public void testDoAction(){
        final long countCall = 4;
        final RoundSettings roundSettingsDTO = getRoundSettingsDTO();
        final Player player = getPlayer();
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
