package ru.smn.poker.entities;


import lombok.*;
import ru.smn.poker.enums.PlayerType;
import ru.smn.poker.enums.RoleType;
import ru.smn.poker.enums.StateType;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "player_settings")
@EqualsAndHashCode()
@ToString
public class PlayerSettingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(mappedBy = "settings",fetch = FetchType.EAGER)
    private PlayerEntity player;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    private List<ActionEntity> actions;

    private String gameName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "settings", fetch = FetchType.EAGER)
    private List<CardEntity> cards;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    private GameEntity game;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private RoleType roleType = RoleType.ORDINARY;

    @Enumerated(value = EnumType.STRING)
    private PlayerType playerType;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "chips_id")
    private ChipsCountEntity chipsCount;

    @Enumerated(value = EnumType.STRING)
    private StateType stateType;

    private long timeBank;

    private boolean active;

}
