package ru.sm.poker.converter;

import ru.sm.poker.enums.CardType;
import ru.sm.poker.model.CardEntity;
import ru.sm.poker.model.GameEntity;
import ru.sm.poker.model.PlayerEntity;

import java.util.List;
import java.util.stream.Collectors;

public class CardConverter {

    public static CardEntity toEntity(CardType cardType, PlayerEntity playerEntity, GameEntity gameEntity) {
        return CardEntity.builder()
                .player(playerEntity)
                .cardType(cardType)
                .game(gameEntity)
                .build();
    }

    public static List<CardEntity> toEntities(List<CardType> cardTypes, PlayerEntity playerEntity, GameEntity gameEntity) {
        return cardTypes.stream()
                .map(cardType -> toEntity(cardType, playerEntity, gameEntity))
                .collect(Collectors.toList());
    }
}
