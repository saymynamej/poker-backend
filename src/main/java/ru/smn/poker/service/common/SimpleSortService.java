package ru.smn.poker.service.common;

import org.springframework.stereotype.Service;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.RoleType;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.service.SortService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class SimpleSortService implements SortService {

    public List<PlayerEntity> sort(List<PlayerEntity> players, StageType stageType) {
        return stageType == StageType.PREFLOP ? sortPreflop(players) : sortPostflop(players);
    }

    private List<PlayerEntity> sortPreflop(List<PlayerEntity> players) {
        if (players.size() == 2) {
            final List<PlayerEntity> sortedPlayers = new ArrayList<>();
            final Optional<PlayerEntity> button = getPlayerByRole(players, RoleType.BUTTON);
            final Optional<PlayerEntity> bigBlind = getPlayerByRole(players, RoleType.BIG_BLIND);
            if (bigBlind.isEmpty() || button.isEmpty()) {
                throw new RuntimeException("cannot find all roles for preflop");
            }
            sortedPlayers.add(button.get());
            sortedPlayers.add(bigBlind.get());
            return sortedPlayers;
        }
        final List<PlayerEntity> sortedPlayers = new ArrayList<>(players);
        final Optional<PlayerEntity> bigBlind = getPlayerByRole(players, RoleType.BIG_BLIND);
        final Optional<PlayerEntity> smallBlind = getPlayerByRole(players, RoleType.SMALL_BLIND);
        final Optional<PlayerEntity> button = getPlayerByRole(players, RoleType.BUTTON);

        if (bigBlind.isEmpty() || smallBlind.isEmpty() || button.isEmpty()) {
            throw new RuntimeException("cannot find all roles for preflop");
        }
        sortedPlayers.remove(bigBlind.get());
        sortedPlayers.remove(smallBlind.get());
        sortedPlayers.remove(button.get());
        sortedPlayers.add(button.get());
        sortedPlayers.add(smallBlind.get());
        sortedPlayers.add(bigBlind.get());
        return sortedPlayers;
    }


    private List<PlayerEntity> sortPostflop(List<PlayerEntity> players) {
        final List<PlayerEntity> sortedList = new LinkedList<>(players);

        final Optional<PlayerEntity> smallBlind = getPlayerByRole(players, RoleType.SMALL_BLIND);
        final Optional<PlayerEntity> bigBlind = getPlayerByRole(players, RoleType.BIG_BLIND);
        final Optional<PlayerEntity> button = getPlayerByRole(players, RoleType.BUTTON);

        if (smallBlind.isPresent()) {
            sortedList.remove(smallBlind.get());
            sortedList.add(0, smallBlind.get());
        }
        if (bigBlind.isPresent()) {
            sortedList.remove(bigBlind.get());
            sortedList.add(1, bigBlind.get());
        }
        if (button.isPresent()) {
            sortedList.remove(button.get());
            sortedList.add(button.get());
        }

        return sortedList;
    }


    private Optional<PlayerEntity> getPlayerByRole(List<PlayerEntity> players, RoleType roleType) {
        return players.stream()
                .filter(player -> player.getSettings().getRoleType() == roleType)
                .findFirst();
    }
}
