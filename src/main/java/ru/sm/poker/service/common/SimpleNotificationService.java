package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.sm.poker.config.SocketMappingConfig;
import ru.sm.poker.service.NotificationService;


@Service
@RequiredArgsConstructor
public class SimpleNotificationService implements NotificationService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SocketMappingConfig socketMappingConfig;


    @Override
    public void sendErrorToUser(String userName, Object message) {
        simpMessagingTemplate.convertAndSendToUser(userName, socketMappingConfig.getErrorPath(), message);
    }

    @Override
    public void sendErrorToAll(Object message) {
        simpMessagingTemplate.convertAndSend(socketMappingConfig.getErrorPath(), message);
    }

    @Override
    public void sendToUser(String userName, Object message) {
        simpMessagingTemplate.convertAndSendToUser(userName, socketMappingConfig.getPokerGamePath(), message);
    }

    @Override
    public void sendToAll(Object message) {
        simpMessagingTemplate.convertAndSend(socketMappingConfig.getPokerGamePath(), message);
    }

    @Override
    public void sendGamesInformationToAll(Object games) {
        simpMessagingTemplate.convertAndSend(socketMappingConfig.getGamesPath(), games);
    }

}
