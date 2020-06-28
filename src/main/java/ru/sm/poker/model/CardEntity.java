package ru.sm.poker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.sm.poker.enums.CardType;

import javax.persistence.*;

@Entity
@Table(name = "cards")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    private GameEntity game;

    @Enumerated(value = EnumType.STRING)
    private CardType cardType;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = PlayerEntity.class)
    @JoinColumn(name = "card_id")
    private PlayerEntity player;
}
