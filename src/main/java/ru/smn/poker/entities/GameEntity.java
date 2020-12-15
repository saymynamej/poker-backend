package ru.smn.poker.entities;

import lombok.*;
import ru.smn.poker.enums.GameType;

import javax.persistence.*;
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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    private List<PlayerEntity> players;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<RoundEntity> rounds;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<ChipsCountEntity> counts;
}
