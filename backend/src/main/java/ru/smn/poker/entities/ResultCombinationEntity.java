package ru.smn.poker.entities;


import lombok.*;
import ru.smn.poker.enums.CombinationType;

import javax.persistence.*;

@Entity
@Table(name = "result_combinations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResultCombinationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "hand_id")
    private HandEntity handEntity;

    @Enumerated(value = EnumType.STRING)
    private CombinationType combinationType;

    private String cards;

    private boolean winner;

    private Integer power;

    @OneToOne
    @JoinColumn(name = "player_id")
    private PlayerEntity player;

}
