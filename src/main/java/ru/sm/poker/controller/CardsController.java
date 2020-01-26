package ru.sm.poker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sm.poker.enums.CardType;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardsController {

    @GetMapping("/get/all")
    public List<CardType> getCardsForHoldem(){
        return CardType.getAllCardsAsList();
    }
}
