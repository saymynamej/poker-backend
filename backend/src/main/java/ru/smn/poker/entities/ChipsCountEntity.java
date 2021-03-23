package ru.smn.poker.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "chips_count")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@Setter
public class ChipsCountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameEntity game;

    private long count;


    public ChipsCountEntity(long count) {
        this.count = count;
    }
}
