package ru.sm.poker.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "chips_count")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "playerEntity")
@Setter
public class ChipsCountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    private PlayerEntity playerEntity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    private GameEntity gameEntity;

    private Long count;
}
