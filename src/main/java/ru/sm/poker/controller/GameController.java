package ru.sm.poker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sm.poker.dto.ActionDTO;
import ru.sm.poker.game.Game;
import ru.sm.poker.model.action.*;
import ru.sm.poker.service.holdem.ActionServiceHoldem;
import ru.sm.poker.game.holdem.HoldemManager;
import ru.sm.poker.model.Player;

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
        holdemManager.addPlayer(Player.builder()
                .name(principal.getName())
                .chipsCount(5000)
                .build());
    }

    @MessageMapping("/addPlayerInGame")
    public void addUserInGame(Principal principal, String gameName) {
        holdemManager.addPlayer(Player
                .builder()
                .name(principal.getName())
                .chipsCount(5000L)
                .build(), gameName);
    }

    @MessageMapping("/raise")
    public void raise(Principal principal, ActionDTO actionDTO) {
        actionServiceHoldem.setAction(actionDTO.getName(), new Raise(Long.parseLong(actionDTO.getCount()), actionDTO.getGameName()));
    }

    @MessageMapping("/call")
    public void call(Principal principal, ActionDTO actionDTO) {
        actionServiceHoldem.setAction(actionDTO.getName(), new Call(Long.parseLong(actionDTO.getCount()), actionDTO.getGameName()));
    }

    @MessageMapping("/fold")
    public void fold(Principal principal, ActionDTO actionDTO) {
        actionServiceHoldem.setAction(actionDTO.getName(), new Fold(actionDTO.getGameName()));
    }

    @MessageMapping("/bet")
    public void bet(Principal principal, ActionDTO actionDTO) {
        actionServiceHoldem.setAction(principal.getName(), new Bet(Long.parseLong(actionDTO.getCount()), actionDTO.getGameName()));
    }

    @MessageMapping("/check")
    public void check(Principal principal, ActionDTO actionDTO) {
        actionServiceHoldem.setAction(principal.getName(), new Check(actionDTO.getGameName()));
    }

    @MessageMapping("/reload")
    public void reload(Principal principal) {
        final Optional<Pair<String, Player>> player = holdemManager.getPlayerByName(principal.getName());
        if (player.isPresent()) {
            final Pair<String, Player> playerPair = player.get();
            final Game game = holdemManager.getGames().get(playerPair.getLeft());
            game.reload();
        }
    }
}
