package ru.sm.poker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sm.poker.game.Game;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.Action;
import ru.sm.poker.model.action.Bet;

@Service
@RequiredArgsConstructor
public class ActionService {

    private final Game game;
    private final ObjectMapper objectMapper;

    public void parseAction(String name, String json){
       final Player playerByName = game.getPlayerByName(name);
       if (!playerByName.equals(game.getActivePlayer())){
           return;
       }
       long lastBet = Long.parseLong(json);
       if (lastBet > game.getLastBet().getCount()){
           final Bet bet = new Bet(lastBet);
           setActionToPlayer(playerByName, bet);
       }
    }

    private void setActionToPlayer(Player player, Action action){
        player.setAction(action);
    }
}
