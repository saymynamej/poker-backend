package ru.smn.poker.util;

import ru.smn.poker.dto.HoldemTableSettings;
import ru.smn.poker.entities.CardEntity;
import ru.smn.poker.entities.GameEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.game.TableSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoundSettingsUtil {

    public static TableSettings copyWithSecureCard(TableSettings tableSettings, List<String> filters) {
        final List<PlayerEntity> playersWithSecureCards = PlayerUtil.copies(tableSettings.getPlayers());
        playersWithSecureCards.forEach(player -> {
            if (!filters.contains(player.getName())) {
                player.addCards(Collections.emptyList());
            }
        });

        return copy(tableSettings, playersWithSecureCards);
    }

    public static void substituteCardsForPlayer(TableSettings tableSettings, GameEntity gameEntity){
        if (tableSettings.getStageType() == StageType.PREFLOP) {
            for (final PlayerEntity player : tableSettings.getPlayers()) {
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

    public static TableSettings copy(TableSettings tableSettings, List<PlayerEntity> players) {
        return HoldemTableSettings.builder()
                .gameName(tableSettings.getGameName())
                .bank(tableSettings.getBank())
                .smallBlindBet(tableSettings.getSmallBlindBet())
                .bigBlindBet(tableSettings.getBigBlindBet())
                .bigBlind(tableSettings.getBigBlind())
                .smallBlind(tableSettings.getSmallBlind())
                .button(tableSettings.getButton())
                .players(players)
                .stageType(tableSettings.getStageType())
                .lastBet(tableSettings.getLastBet())
                .activePlayer(tableSettings.getActivePlayer())
                .flop(tableSettings.getFlop())
                .river(tableSettings.getRiver())
                .tern(tableSettings.getTern())
                .build();
    }

}
