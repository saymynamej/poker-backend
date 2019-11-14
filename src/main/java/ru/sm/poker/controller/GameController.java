package ru.sm.poker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sm.poker.game.holdem.HoldemActionHandler;
import ru.sm.poker.game.holdem.HoldemManager;
import ru.sm.poker.model.Message;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.Bet;
import ru.sm.poker.model.action.Fold;
import ru.sm.poker.model.action.Raise;
import ru.sm.poker.service.BroadCastService;

import java.security.Principal;

@RequestMapping("/game")
@RestController
@RequiredArgsConstructor
@Slf4j
public class GameController {
    private final BroadCastService broadCastService;
    private final HoldemManager holdemManager;
    private final HoldemActionHandler holdemActionHandler;

    @MessageMapping("/addPlayer")
    public void addUser(Principal principal) {
        holdemManager
                .addPlayer(Player.builder()
                .name(principal.getName())
                .chipsCount(5000)
                .build());
    }

    @MessageMapping("/raise")
    public void raise(Principal principal, Message message) {
        holdemActionHandler.setAction(principal.getName(), new Raise(Long.parseLong(message.getCount()), message.getGameName()));
    }

    @MessageMapping("/call")
    public void call(Principal principal, Message message) {
        holdemActionHandler.setAction(principal.getName(), new Bet(Long.parseLong(message.getCount()),message.getGameName()));
    }

    @MessageMapping("/fold")
    public void fold(Principal principal, Message message) {
        holdemActionHandler.setAction(principal.getName(), new Fold(message.getGameName()));
    }

//    @MessageMapping("/reload")
//    public void reload(Principal principal){
//    }
//
//    @MessageMapping("/get/flop")
//    public void getFlop(Principal principal){
//
//    }
//
//    @MessageMapping("/get/tern")
//    public void getTern(Principal principal){
//    }
//
//
//    @MessageMapping("/get/river")
//    public void getRiver(Principal principal){
//    }
//
//    @MessageMapping("/get/bank")
//    public void getBank(Principal principal){
//    }
}
