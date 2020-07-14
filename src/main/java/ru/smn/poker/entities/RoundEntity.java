package ru.smn.poker.entities;


import lombok.*;
import ru.smn.poker.enums.CardType;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rounds")
public class RoundEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    private GameEntity game;

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL)
    private List<ActionEntity> actions;


    @Enumerated(value = EnumType.STRING)
    private CardType f1;

    @Enumerated(value = EnumType.STRING)
    private CardType f2;

    @Enumerated(value = EnumType.STRING)
    private CardType f3;

    @Enumerated(value = EnumType.STRING)
    private CardType tern;

    @Enumerated(value = EnumType.STRING)
    private CardType river;

}
