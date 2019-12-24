package ru.sm.poker.service.holdem;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.sm.poker.config.SocketMappingConfig;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.game.holdem.HoldemSecurityService;

@Service
@RequiredArgsConstructor
public class BroadCastService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SocketMappingConfig socketMappingConfig;
    private final HoldemSecurityService holdemSecurityService;

    public void sendErrorToUser(String userName, Object message) {
        simpMessagingTemplate.convertAndSendToUser(userName, socketMappingConfig.getErrorPath(), message);
    }

    public void sendErrorToAll(Object message) {
        simpMessagingTemplate.convertAndSend(socketMappingConfig.getErrorPath(), message);
    }

    public void sendToUser(String userName, Object message) {
        simpMessagingTemplate.convertAndSendToUser(userName, socketMappingConfig.getPokerGamePath(), message);
    }

    public void sendToAllPlayerReadyMessage(Object message) {
        simpMessagingTemplate.convertAndSend(socketMappingConfig.getReadyPlayersPath(), message);
    }

    public void sendToAllWithSecure(Object message) {
        simpMessagingTemplate.convertAndSend(socketMappingConfig.getPokerGamePath(), message);
    }

    public void sendToAllWithSecure(RoundSettingsDTO roundSettingsDTO) {
        roundSettingsDTO.getPlayers().forEach(player -> {
            final RoundSettingsDTO secureRoundSettings = holdemSecurityService.secureCards(player.getName(), roundSettingsDTO);
            simpMessagingTemplate.convertAndSendToUser(player.getName(), socketMappingConfig.getPokerGamePath(), secureRoundSettings);
        });
    }


    public void sendFlopToAll(Object flop) {
        simpMessagingTemplate.convertAndSend(socketMappingConfig.getFlopPath(), flop);
    }

    public void sendTernToAll(Object tern) {
        simpMessagingTemplate.convertAndSend(socketMappingConfig.getTernPath(), tern);
    }

    public void sendRiverToAll(Object river) {
        simpMessagingTemplate.convertAndSend(socketMappingConfig.getRiverPath(), river);
    }

    public void sendBankToAll(Object bank) {
        simpMessagingTemplate.convertAndSend(socketMappingConfig.getBankPath(), bank);
    }
}
