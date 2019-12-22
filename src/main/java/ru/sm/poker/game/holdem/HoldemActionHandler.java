package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.sm.poker.game.ActionHandler;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.Action;

import java.util.Optional;

import static java.lang.String.*;

@RequiredArgsConstructor
@Slf4j
@Component
public class HoldemActionHandler implements ActionHandler {

    private final HoldemSecurityService holdemSecurityService;
    private final HoldemManager holdemManager;

    @Override
    public void setAction(String playerName, Action action) {
        final Optional<Pair<String, Player>> playerByName = holdemManager.getPlayerByName(playerName);

        if (playerByName.isPresent()) {

            final Pair<String, Player> pairGameAndPlayer = playerByName.get();

            final String gameName = pairGameAndPlayer.getLeft();
            final Player player = pairGameAndPlayer.getRight();

            if (holdemSecurityService.isLegalPlayer(gameName, player)) {
                player.setAction(action);
            } else {
                log.info(format("player tried send bet to not own game. name:%s", player.getName()));
            }

        } else {
            log.info("cannot find player with playerName:" + playerName);
        }
    }

}
