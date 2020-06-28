package ru.sm.poker.model;

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
public class PlayerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CardEntity> cards;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    private List<GameEntity> games;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private RoleType roleType = RoleType.ORDINARY;

}
