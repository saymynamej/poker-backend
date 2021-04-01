package ru.smn.poker.entities;

import lombok.*;
import org.springframework.transaction.annotation.Transactional;
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
@Table(name = "tables")
public class TableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private GameType gameType;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "tables")
    @Builder.Default
    private List<PlayerEntity> players = new ArrayList<>();

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
    @Builder.Default
    private List<HandEntity> hands = new ArrayList<>();


    public HandEntity getLastHand(){
        return hands.get(hands.size() - 1);
    }

}
