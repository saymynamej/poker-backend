package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sm.poker.converter.GameConverter;
import ru.sm.poker.dto.GameDTO;
import ru.sm.poker.game.Game;
import ru.sm.poker.service.InfoService;

import java.util.List;
import java.util.Map;

@Profile({"enableInfoService"})
@RequiredArgsConstructor
@Service
@EnableScheduling
public class GamesInfoService implements InfoService {
    private final Map<String, Game> games;
    private final SimpleNotificationService simpleNotificationService;
    private final GameConverter gameConverter;

    @Scheduled(cron = "*/2 * * * * *")
    @Override
    public void send() {
        final List<GameDTO> gamesDTO = gameConverter.convertGamesMapToList(games);
        simpleNotificationService.sendGamesInformationToAll(gamesDTO);
    }

}
