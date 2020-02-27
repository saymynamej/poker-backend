package ru.sm.poker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.service.common.OrderActionService;
import ru.sm.poker.util.DTOUtilTest;
import ru.sm.poker.util.WaitUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class OrderActionServiceTest {

    @Autowired
    private OrderActionService orderActionService;

    private final static ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Test
    public void test() {
//        final HoldemRoundSettings roundSettingsDTO = DTOUtilTest.getRoundSettingsDTOWithPlayers(3);
//        executorService.submit(() -> orderActionService.start(roundSettingsDTO));
//
//        WaitUtil.waitOfCondition(()-> roundSettingsDTO.getActivePlayer().getName() != null);
//
//        System.out.println(roundSettingsDTO.getActivePlayer().getName());

    }
}
