package ru.sm.poker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sm.poker.game.Game;
import ru.sm.poker.dto.MessageDTO;
import ru.sm.poker.service.holdem.ActionServiceHoldem;
import ru.sm.poker.game.holdem.HoldemManager;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.Bet;
import ru.sm.poker.model.action.Call;
import ru.sm.poker.model.action.Fold;
import ru.sm.poker.model.action.Raise;

import java.security.Principal;
import java.util.Optional;

@RequestMapping("/game")
@RestController
@RequiredArgsConstructor
@Slf4j
public class GameController {
    private final HoldemManager holdemManager;
    private final ActionServiceHoldem actionServiceHoldem;

    @MessageMapping("/addPlayer")
    public void addUser(Principal principal) {
        holdemManager
                .addPlayer(Player.builder()
                        .name(principal.getName())
                        .chipsCount(5000)
                        .build());
    }

    @MessageMapping("/raise")
    public void raise(Principal principal, MessageDTO messageDTO) {
        actionServiceHoldem.setAction(messageDTO.getName(), new Raise(Long.parseLong(messageDTO.getCount()), messageDTO.getGameName()));
    }

    @MessageMapping("/call")
    public void call(Principal principal, MessageDTO messageDTO) {
        actionServiceHoldem.setAction(messageDTO.getName(), new Call(Long.parseLong(messageDTO.getCount()), messageDTO.getGameName()));
    }

    @MessageMapping("/fold")
    public void fold(Principal principal, MessageDTO messageDTO) {
        actionServiceHoldem.setAction(messageDTO.getName(), new Fold(messageDTO.getGameName()));
    }

    @MessageMapping("/bet")
    public void bet(Principal principal, MessageDTO messageDTO) {
        actionServiceHoldem.setAction(principal.getName(), new Bet(Long.parseLong(messageDTO.getCount()), messageDTO.getGameName()));
    }

    @MessageMapping("/reload")
    public void reload(Principal principal) {
        final Optional<Pair<String, Player>> player = holdemManager.getPlayerByName(principal.getName());
        if (player.isPresent()) {
            final Pair<String, Player> playerPair = player.get();
            final Game game = holdemManager.getAllGames().get(playerPair.getLeft());
            game.reload();
        }
    }
}
