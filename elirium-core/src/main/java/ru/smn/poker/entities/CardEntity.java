package ru.smn.poker.entities;

import lombok.*;
import ru.smn.combination.data.CardType;

import javax.persistence.*;

@Entity
@Table(name = "cards")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "player")
@Setter
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private CardType cardType;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private PlayerEntity player;

    @ManyToOne
    @JoinColumn(name = "settings_id")
    private PlayerSettingsEntity settings;

}
