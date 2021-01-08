package ru.smn.poker.action;

import org.springframework.stereotype.Component;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.stream.PlayerPredicates;

import java.util.List;
import java.util.stream.Stream;

@Component
public class DefaultNextPlayerSelector implements NextPlayerSelector {

    @Override
    public PlayerEntity getPlayerForAction(List<PlayerEntity> sortedPlayers, PlayerEntity previousActivePlayer) {
        if (previousActivePlayer == null) {
            return sortedPlayers.get(0);
        }

        final int indexOfPreviousActivePlayer = sortedPlayers.indexOf(previousActivePlayer);

        final Stream<PlayerEntity> firstHalf = sortedPlayers.subList(indexOfPreviousActivePlayer + 1, sortedPlayers.size()).stream();

        final Stream<PlayerEntity> secondHalf = sortedPlayers.subList(0, indexOfPreviousActivePlayer + 1).stream();

        return Stream.concat(firstHalf, secondHalf)
                .filter(PlayerPredicates.playerCanMakeMove())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("cannot find active player, global error"));
    }
}
