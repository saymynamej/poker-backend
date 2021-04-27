package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.smn.poker.dto.GameInfo;
import ru.smn.poker.game.Table;
import ru.smn.poker.service.InfoService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@EnableScheduling
public class GamesInfoService implements InfoService {
    private final SimpleNotificationService simpleNotificationService;
    private final Map<String, Table> games;

    @Scheduled(cron = "*/2 * * * * *")
    @Override
    public void send() {
        final List<GameInfo> games = this.games.entrySet().stream()
                .map(entry -> {
                    final Table table = entry.getValue();
                    return GameInfo.builder()
                            .name(entry.getKey())
                            .gameType(table.getGameSettings().getGameType())
                            .countPlayers(table.getPlayers().size())
                            .maxPlayersSize(table.getGameSettings().getMaxPlayerSize())
                            .build();
                }).collect(Collectors.toList());

        simpleNotificationService.sendGamesInformationToAll(games);
    }

}
