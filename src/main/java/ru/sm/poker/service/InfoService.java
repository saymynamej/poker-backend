package ru.sm.poker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.GameDTO;
import ru.sm.poker.game.Game;
import ru.sm.poker.game.holdem.HoldemManager;
import ru.sm.poker.service.holdem.BroadCastService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Profile({"enableInfoService"})
@RequiredArgsConstructor
@Service
@EnableScheduling

public class InfoService {

    private final HoldemManager holdemManager;
    private final BroadCastService broadCastService;

    @Scheduled(cron = "*/3 * * * * *")
    public void sendGamesInformation() {
        final List<GameDTO> gameDTOS = convertMapToLisGameDTO(holdemManager.getGames());
        broadCastService.sendGamesToAll(gameDTOS);
    }

    private List<GameDTO> convertMapToLisGameDTO(Map<String, Game> games) {
        return games
                .entrySet()
                .stream()
                .map(entry -> GameDTO.builder()
                        .name(entry.getKey())
                        .countPlayers(entry.getValue().getRoundSettings().getPlayers().size())
                        .build())
                .collect(Collectors.toList());
    }
}
