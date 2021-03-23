package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.smn.poker.dto.GameInfo;
import ru.smn.poker.service.InfoService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Profile({"enableInfoService"})
@RequiredArgsConstructor
@Service
@EnableScheduling
public class GamesInfoService implements InfoService {

    @Scheduled(cron = "*/2 * * * * *")
    @Override
    public void send() {

    }

}
