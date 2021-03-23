package ru.smn.poker.entities;

import lombok.*;
import ru.smn.poker.action.Action;

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
    private List<TableEntity> tables;

    private String name;

    private String password;

    private boolean enable;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_type")
    private AccessType accessType;


    //TODO
    public PlayerSettingsEntity getTableSettings(){
        return settings.get(0);
    }

    //TODO
    public Action getAction(){
        return settings.get(0).getAction();
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
