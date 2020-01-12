//package ru.sm.poker.service;
//
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import ru.sm.poker.action.holdem.Call;
//import ru.sm.poker.action.holdem.Fold;
//import ru.sm.poker.action.holdem.Raise;
//import ru.sm.poker.dto.RoundSettingsDTO;
//import ru.sm.poker.enums.CardType;
//import ru.sm.poker.enums.StateType;
//import ru.sm.poker.model.Player;
//import ru.sm.poker.service.holdem.BroadCastService;
//import ru.sm.poker.util.GameUtil;
//import ru.sm.poker.util.ThreadUtil;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.concurrent.Executors;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@ActiveProfiles("test")
//public class OrderActionServiceTest {
//
//    @Autowired
//    private OrderActionService orderActionService;
//
//    @MockBean
//    private BroadCastService broadCastService;
//
//    @Test
//    public void testPipeline() {
//        final RoundSettingsDTO roundSettingsDTO = getRoundSettingsDTO();
//
//        Executors.newSingleThreadExecutor().submit(() -> {
//            ThreadUtil.sleep(3);
//            roundSettingsDTO.getPlayers()
//                    .stream()
//                    .filter(player -> !(player.getAction() instanceof Fold))
//                    .forEach(player -> {
//                        if (player.getName().equals("first")) {
//                            player.setAction(new Raise(18, player.getGameName()));
//                        }
//                        else if (player.getName().equals("fourth")){
//                        }
//
//                        else {
//                            player.setAction(new Call(18, player.getGameName()));
//                        }
//                    });
//            ThreadUtil.sleep(3);
//
//            roundSettingsDTO.getPlayers().stream().filter(player -> player.getName().equals("fourth")).findFirst().get() .setAction(new Call(18, roundSettingsDTO.getPlayers().get(0).getGameName()));
//        });
//
//        orderActionService.start(roundSettingsDTO, Collections.emptyList());
//
//    }
//
//
//    private List<Player> getPlayers() {
//
//        final String gameName = GameUtil.getRandomGOTCityName();
//        return Arrays.asList(
//                Player
//                        .builder()
//                        .name("first")
//                        .action(new Call(2, gameName))
//                        .gameName(gameName)
//                        .stateType(StateType.IN_GAME)
//                        .build(),
//                Player
//                        .builder()
//                        .name("second")
//                        .action(new Call(2, gameName))
//                        .gameName(gameName)
//                        .stateType(StateType.IN_GAME)
//                        .build(),
//                Player
//                        .builder()
//                        .name("third")
//                        .action(new Call(2, gameName))
//                        .gameName(gameName)
//                        .stateType(StateType.IN_GAME)
//                        .build(),
//                Player
//                        .builder()
//                        .gameName(gameName)
//                        .action(new Raise(6, gameName))
//                        .name("fourth")
//                        .stateType(StateType.IN_GAME)
//                        .build()
//        );
//    }
//
//    private RoundSettingsDTO getRoundSettingsDTO() {
//
//        final List<Player> players = getPlayers();
//
//        return RoundSettingsDTO
//                .builder()
//                .lastBet(2L)
//                .flop(Arrays.asList(CardType.A_C, CardType.A_D, CardType.A_H))
//                .players(players)
//                .build();
//    }
//}
