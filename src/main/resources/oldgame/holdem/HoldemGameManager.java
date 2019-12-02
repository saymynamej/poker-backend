package ru.sm.poker.oldgame.holdem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sm.poker.oldgame.GameManager;
import ru.sm.poker.model.Player;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class HoldemGameManager implements GameManager {

    private final HoldemGame game;
    private final static int MAX_PLAYERS = 9;
    private final static int MIN_PLAYER = 4;

    @Override
    public boolean addPlayer(Player player) {
        if (game.getPlayers().size() == MAX_PLAYERS ||
                game.getPlayers()
                        .stream()
                        .anyMatch(pl -> pl.getName().equals(player.getName()))) {
            return false;
        }
        game.addPlayer(player);
        return true;
    }

    @Override
    public boolean playerExistByName(String name) {
        return game.getPlayers()
                .stream()
                .anyMatch(exist -> exist.getName().equals(name));
    }


    @Override
    public void addPlayers(List<Player> players) {
        players.forEach(this::addPlayer);
    }

    @Override
    public void removePlayer(Player player) {
        this.game.getPlayers().remove(player);
    }

    @Override
    public Optional<Player> getPlayerByName(String name) {
        return this.game.getPlayers()
                .stream()
                .filter(player -> player.getName().equals(name))
                .findFirst();
    }

    @Override
    public Player getActivePlayer() {
        return this.game.getRound().getActivePlayer();
    }


//    @Override
//    public RoundSettings getRoundSettings() {
//        return RoundSettings.builder()
//                .flop(this.round.getFlop())
//                .tern(this.round.getTern())
//                .river(this.round.getRiver())
//                .activePlayer(this.round.getActivePlayer())
//                .button(this.round.getPlayerByRole( RoleType.BUTTON))
//                .bank(this.round.getBank())
//                .smallBlind(this.round.getPlayerByRole(RoleType.SMALL_BLIND))
//                .bigBlind(this.round.getPlayerByRole(RoleType.BIG_BLIND))
//                .players(this.round.getAllPlayers())
//                .build();
//    }
}
