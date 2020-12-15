package ru.smn.poker.util;

import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.entities.CardEntity;
import ru.smn.poker.entities.GameEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.game.RoundSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoundSettingsUtil {

    public static RoundSettings copyWithSecureCard(RoundSettings roundSettings, List<String> filters) {
        final List<PlayerEntity> playersWithSecureCards = PlayerUtil.copies(roundSettings.getPlayers());
        playersWithSecureCards.forEach(player -> {
            if (!filters.contains(player.getName())) {
                player.addCards(Collections.emptyList());
            }
        });

        return copy(roundSettings, playersWithSecureCards);
    }

    public static void substituteCardsForPlayer(RoundSettings roundSettings, GameEntity gameEntity){
        if (roundSettings.getStageType() == StageType.PREFLOP) {
            for (final PlayerEntity player : roundSettings.getPlayers()) {
                final PlayerEntity playerEntity = gameEntity.getPlayers().stream()
                        .filter(pe -> pe.getName().equals(player.getName()))
                        .findFirst()
                        .orElseThrow();

                final List<CardEntity> cards = new ArrayList<>();
                for (CardEntity card : player.getSettings().getCards()) {
                    final CardEntity finalCard = card;
                    final CardEntity cardEntity = playerEntity.getSettings().getCards()
                            .stream()
                            .filter(ce -> ce.getCardType()
                                    .equals(finalCard.getCardType()))
                            .findFirst()
                            .orElseThrow();

                    cards.add(CardEntity.builder()
                            .id(cardEntity.getId())
                            .cardType(cardEntity.getCardType())
                            .build());
                }
                player.getSettings().setCards(cards);
            }
        }
    }

    public static RoundSettings copy(RoundSettings roundSettings, List<PlayerEntity> players) {
        return HoldemRoundSettings.builder()
                .gameName(roundSettings.getGameName())
                .bank(roundSettings.getBank())
                .smallBlindBet(roundSettings.getSmallBlindBet())
                .bigBlindBet(roundSettings.getBigBlindBet())
                .bigBlind(roundSettings.getBigBlind())
                .smallBlind(roundSettings.getSmallBlind())
                .button(roundSettings.getButton())
                .players(players)
                .stageType(roundSettings.getStageType())
                .lastBet(roundSettings.getLastBet())
                .activePlayer(roundSettings.getActivePlayer())
                .flop(roundSettings.getFlop())
                .river(roundSettings.getRiver())
                .tern(roundSettings.getTern())
                .build();
    }

}
