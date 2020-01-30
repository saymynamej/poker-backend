package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.GameDTO;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.service.InfoService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Profile({"enableInfoService"})
@RequiredArgsConstructor
@Service
@EnableScheduling

public class GamesInfoService implements InfoService {

    private final GameManager holdemGameManager;
    private final SimpleNotificationService simpleNotificationService;


    @Scheduled(cron = "*/2 * * * * *")
    @Override
    public void send() {
        final List<GameDTO> gameDTOS = convertMapToLisGameDTO(holdemGameManager.getGames());
        simpleNotificationService.sendGamesInformationToAll(gameDTOS);
    }

    private List<GameDTO> convertMapToLisGameDTO(Map<String, Game> games) {
        return games.values().stream()
                .map(game -> GameDTO.builder()
                        .name(game.getGameName())
                        .maxPlayersSize(game.getGameSettings().getMaxPlayerSize())
                        .gameType(game.getGameSettings().getGameType())
                        .countPlayers(game.getPlayers().size())
                        .build())
                .collect(Collectors.toList());
    }
}
