package ru.sm.poker.game.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sm.poker.game.ActionHandler;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.Action;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class HoldemActionHandler implements ActionHandler {

    private final HoldemSecurityService holdemSecurityService;
    private final HoldemManager holdemManager;

    @Override
    public void setAction(String playerName, Action action) {
        final Map<String, Player> playerByName = holdemManager.getPlayerByName(playerName);
        playerByName.forEach((game, player) -> {
            if (holdemSecurityService.isLegalPlayer(game,player)){
                player.setAction(action);
            }
            else {
                log.info(String.format("player tried send bet to not own game. name:%s", player.getName()));
            }
        });

    }
}
