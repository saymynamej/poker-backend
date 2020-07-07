package ru.sm.poker.entities;

import lombok.*;
import ru.sm.poker.enums.ActionType;

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

}
