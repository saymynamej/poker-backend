package ru.smn.poker.entities;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tables")
public class TableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    @Builder.Default
    private List<PlayerEntity> players = new ArrayList<>();

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
    @Builder.Default
    private List<HandEntity> hands = new ArrayList<>();

}
