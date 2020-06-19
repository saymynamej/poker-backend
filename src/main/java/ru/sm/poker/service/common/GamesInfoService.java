package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sm.poker.converter.GameConverter;
import ru.sm.poker.dto.GameDTO;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.service.InfoService;

import java.util.List;

@Profile({"enableInfoService"})
@RequiredArgsConstructor
@Service
@EnableScheduling

public class GamesInfoService implements InfoService {

    private final GameManager holdemGameManager;
    private final SimpleNotificationService simpleNotificationService;
    private final GameConverter gameConverter;

    @Scheduled(cron = "*/2 * * * * *")
    @Override
    public void send() {
        final List<GameDTO> games = gameConverter.convertGamesMapToList(holdemGameManager.getGames());
        simpleNotificationService.sendGamesInformationToAll(games);
    }

}
