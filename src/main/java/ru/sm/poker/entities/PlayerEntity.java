package ru.sm.poker.entities;

import lombok.*;
import ru.sm.poker.enums.RoleType;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "players")
@EqualsAndHashCode(of = "name")
public class PlayerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CardEntity> cards;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    private GameEntity games;

    @OneToOne(cascade = CascadeType.ALL)
    private ChipsCountEntity chipsCount;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private RoleType roleType = RoleType.ORDINARY;

}
