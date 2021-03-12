package ru.smn.poker.entities;

import lombok.*;
import ru.smn.poker.enums.GameType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "games")
@EqualsAndHashCode(of = "name")
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private GameType gameType;

    private String name;

    @OneToMany(mappedBy = "gameEntity")
    private List<TableEntity> tables;

}
