package ru.smn.poker.entities;

import lombok.*;
import ru.smn.poker.action.Action;
import ru.smn.poker.enums.AccessType;
import ru.smn.poker.enums.StageType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder(toBuilder = true)
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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "table_id")
    private List<TableEntity> tables;

    private String name;

    private String password;

    private boolean enable;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_type")
    private AccessType accessType;


    public PlayerEntity(String name) {
        this.name = name;
    }

    public PlayerEntity(Long id) {
        this.id = id;
    }

    //TODO
    public PlayerSettingsEntity getTableSettings() {
        return settings.get(0);
    }

    //TODO
    public Action getAction() {
        return settings.get(0).getAction();
    }

    public void addAction(Action action, long handId) {
        getTableSettings().getActions().add(
                ActionEntity.builder()
                        .count(action.getCount())
                        .hand(HandEntity.builder()
                                .id(handId)
                                .build())
                        .actionType(action.getActionType())
                        .player(this)
                        .stageType(StageType.PREFLOP)
                        .build()
        );
    }

    public PlayerEntity copy() {
        return PlayerEntity
                .builder()
                .name(name)
                .accessType(accessType)
                .enable(enable)
                .tables(tables)
                .settings(this.settings.stream()
                        .map(settings -> settings.toBuilder().build())
                        .collect(Collectors.toList())
                )
                .build();
    }

}
