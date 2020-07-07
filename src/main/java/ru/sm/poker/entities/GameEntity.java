package ru.sm.poker.entities;

import lombok.*;
import ru.sm.poker.enums.GameType;

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

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PlayerEntity> players;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<RoundEntity> rounds;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<ChipsCountEntity> counts;
}
