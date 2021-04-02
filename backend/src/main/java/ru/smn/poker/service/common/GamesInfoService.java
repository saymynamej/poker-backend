package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.smn.poker.service.InfoService;

@RequiredArgsConstructor
@Service
@EnableScheduling
public class GamesInfoService implements InfoService {

    @Scheduled(cron = "*/2 * * * * *")
    @Override
    public void send() {

    }

}
