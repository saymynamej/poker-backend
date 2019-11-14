package ru.sm.poker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sm.poker.game.ActionHandler;
import ru.sm.poker.game.holdem.HoldemActionHandler;
import ru.sm.poker.game.holdem.HoldemManager;
import ru.sm.poker.model.Message;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.Call;
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
    public void raise(Principal principal, long raise) {

    }

    @MessageMapping("/call")
    public void call(Message message) {

    }

    @MessageMapping("/fold")
    public void fold(Message message) {
    }

    @MessageMapping("/reload")
    public void reload(Principal principal){
    }

    @MessageMapping("/get/flop")
    public void getFlop(Principal principal){

    }

    @MessageMapping("/get/tern")
    public void getTern(Principal principal){
    }


    @MessageMapping("/get/river")
    public void getRiver(Principal principal){
    }

    @MessageMapping("/get/bank")
    public void getBank(Principal principal){
    }
}
