package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sm.poker.converter.GameInfoConverter;
import ru.sm.poker.dto.GameInfo;
import ru.sm.poker.game.Game;
import ru.sm.poker.service.InfoService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Profile({"enableInfoService"})
@RequiredArgsConstructor
@Service
@EnableScheduling
public class GamesInfoService implements InfoService {
    private final Map<Game, ExecutorService> games;
    private final SimpleNotificationService simpleNotificationService;

    @Scheduled(cron = "*/2 * * * * *")
    @Override
    public void send() {
        final List<GameInfo> gamesDTO = GameInfoConverter.toDTOs(games.keySet());
        simpleNotificationService.sendGamesInformationToAll(gamesDTO);
    }

}
