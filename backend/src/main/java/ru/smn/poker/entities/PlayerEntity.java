package ru.smn.poker.entities;

import lombok.*;
import ru.smn.poker.action.Action;
import ru.smn.poker.enums.RoleType;

import javax.persistence.*;
import java.util.ArrayList;
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "settings_id")
    private List<PlayerSettingsEntity> settings = new ArrayList<>();

    private String name;

    private String password;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private RoleType roleType = RoleType.ORDINARY;

    private boolean enable;


    public PlayerEntity copy() {
        return PlayerEntity
                .builder()
                .name(name)
                .settings(settings)
                .build();
    }

}
