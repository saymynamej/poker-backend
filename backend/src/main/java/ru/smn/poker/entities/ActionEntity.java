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

    @ManyToOne
    @JoinColumn(name = "player_id")
    private PlayerEntity player;

    @Enumerated(value = EnumType.STRING)
    private ActionType actionType;

    private Long count;

    @Enumerated(value = EnumType.STRING)
    private StageType stageType;

    @ManyToOne
    @JoinColumn(name = "hand_id")
    private HandEntity hand;

}
