package ru.smn.poker.entities;

import lombok.*;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.enums.StageType;

import javax.persistence.*;

@Table(name = "actions")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ActionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    private PlayerEntity player;

    @Enumerated(value = EnumType.STRING)
    private ActionType actionType;

    private Long count;

    @Enumerated(value = EnumType.STRING)
    private StageType stageType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "round_id")
    private RoundEntity round;

}
