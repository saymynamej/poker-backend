package ru.sm.poker.entities;

import lombok.*;
import ru.sm.poker.enums.CardType;

import javax.persistence.*;

@Entity
@Table(name = "cards")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "player")
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    private GameEntity game;

    @Enumerated(value = EnumType.STRING)
    private CardType cardType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    private PlayerEntity player;
}
