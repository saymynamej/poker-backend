package ru.sm.poker.converter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.model.CardEntity;
import ru.sm.poker.model.GameEntity;
import ru.sm.poker.model.PlayerEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlayerConverter {
    private final CardConverter cardConverter;

    public PlayerEntity convertPlayerDTOToPlayer(PlayerDTO playerDTO, List<GameEntity> gameEntity) {
        final PlayerEntity playerEntity = PlayerEntity.builder()
                .name(playerDTO.getName())
                .games(gameEntity)
                .roleType(playerDTO.getRoleType())
                .build();
        final List<CardType> cardTypes = playerDTO.getCards();
        if (cardTypes != null) {
            final List<CardEntity> cardEntities = cardTypes.stream()
                    .map(cardType -> cardConverter.convertToCard(cardType, playerEntity, gameEntity.get(0)))
                    .collect(Collectors.toList());
            playerEntity.setCards(cardEntities);
        }
        return playerEntity;
    }
}
