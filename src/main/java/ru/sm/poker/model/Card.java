package ru.sm.poker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ru.sm.poker.enums.CardType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Enumerated(value = EnumType.STRING)
    private CardType cardType;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Player.class)
    private Player player;
}
