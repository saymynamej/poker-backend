package ru.sm.poker.converter;

import org.springframework.stereotype.Service;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.model.CardEntity;
import ru.sm.poker.model.GameEntity;
import ru.sm.poker.model.PlayerEntity;

@Service
public class CardConverter {

    public CardEntity convertToCard(CardType cardType, PlayerEntity playerEntity, GameEntity gameEntity) {
        return CardEntity.builder()
                .player(playerEntity)
                .cardType(cardType)
                .game(gameEntity)
                .build();
    }
}
