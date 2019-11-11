package ru.sm.poker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sm.poker.game.Game;
import ru.sm.poker.model.Message;
import ru.sm.poker.model.action.Call;
import ru.sm.poker.model.action.Fold;
import ru.sm.poker.model.action.Raise;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.BroadCastService;
import ru.sm.poker.service.PlayerService;
import java.security.Principal;

@RequestMapping("/game")
@RestController
@RequiredArgsConstructor
@Slf4j
public class GameController {
    private final ActionService actionService;
    private final PlayerService playerService;
    private final BroadCastService broadCastService;
    private final Game game;

    @MessageMapping("/addPlayer")
    public void addUser(Principal principal) {
        playerService.addPlayer(principal.getName());
    }

    @MessageMapping("/raise")
    public void raise(Principal principal, long raise) {
        actionService.setAction(principal.getName(), new Raise(raise));
    }

    @MessageMapping("/call")
    public void call(Message message) {
        actionService.setAction(message.getName(), new Call(Long.parseLong(message.getCount())));
    }

    @MessageMapping("/fold")
    public void fold(Message message) {
        actionService.setAction(message.getName(), new Fold());
    }

    @MessageMapping("/reload")
    public void reload(Principal principal){
        game.reload();
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
