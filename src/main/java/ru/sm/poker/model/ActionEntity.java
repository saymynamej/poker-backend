package ru.sm.poker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.sm.poker.enums.ActionType;

import javax.persistence.*;

@Table(name = "actions")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ActionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private PlayerEntity player;

    private ActionType actionType;

    private Long count;

}
