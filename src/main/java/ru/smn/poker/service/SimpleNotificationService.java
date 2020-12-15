package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.smn.poker.config.SocketMappingConfig;
import ru.smn.poker.service.NotificationService;


@Service
@RequiredArgsConstructor
public class SimpleNotificationService implements NotificationService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SocketMappingConfig socketMappingConfig;

    @Override
    public void sendSystemMessageToUser(String userName, Object message) {
        simpMessagingTemplate.convertAndSendToUser(userName, socketMappingConfig.getErrorPath(), message);
    }

    @Override
    public void sendSystemMessageToAll(Object message) {
        simpMessagingTemplate.convertAndSend(socketMappingConfig.getErrorPath(), message);
    }

    @Override
    public void sendGameInformationToUser(String userName, Object message) {
        simpMessagingTemplate.convertAndSendToUser(userName, socketMappingConfig.getPokerGamePath(), message);
    }

    @Override
    public void sendGameInformationToAll(Object message) {
        simpMessagingTemplate.convertAndSend(socketMappingConfig.getPokerGamePath(), message);
    }

    @Override
    public void sendGamesInformationToAll(Object games) {
        simpMessagingTemplate.convertAndSend(socketMappingConfig.getGamesPath(), games);
    }

}
