package ru.sm.poker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.sm.poker.config.SocketMappingConfig;

@Service
@RequiredArgsConstructor
public class BroadCastService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SocketMappingConfig socketMappingConfig;

    public void sendErrorToUser(String userName, Object message){
        simpMessagingTemplate.convertAndSendToUser(userName, socketMappingConfig.getErrorPath(), message);
    }

    public void sendErrorToAll(Object message){
        simpMessagingTemplate.convertAndSend(socketMappingConfig.getErrorPath(), message);
    }

    public void sendToAllPlayerReadyMessage(Object message){
        simpMessagingTemplate.convertAndSend(socketMappingConfig.getReadyPlayersPath(), message);
    }

    public void sendToAll(Object message){
        simpMessagingTemplate.convertAndSend(socketMappingConfig.getPokerGamePath(), message);
    }
}
