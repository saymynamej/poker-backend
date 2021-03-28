package ru.smn.poker.entities;


import lombok.*;
import ru.smn.poker.enums.CardType;
import ru.smn.poker.enums.StageType;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hands")
@EqualsAndHashCode(of = "id")
public class HandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "table_id")
    private TableEntity table;

    @OneToMany(mappedBy = "hand", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ActionEntity> actions;

    private long lastBet;

    private long bank;

    private long smallBlindBet;

    private long bigBlindBet;

    @OneToOne
    @JoinColumn(name = "big_bling_id")
    private PlayerEntity bigBlind;

    @OneToOne
    @JoinColumn(name = "small_bling_id")
    private PlayerEntity smallBlind;

    @OneToOne
    @JoinColumn(name = "button_id")
    private PlayerEntity button;

    @OneToOne
    @JoinColumn(name = "active_player_id")
    private PlayerEntity activePlayer;

    @Enumerated(value = EnumType.STRING)
    private CardType f1;

    @Enumerated(value = EnumType.STRING)
    private CardType f2;

    @Enumerated(value = EnumType.STRING)
    private CardType f3;

    @Enumerated(value = EnumType.STRING)
    private CardType tern;

    @Enumerated(value = EnumType.STRING)
    private CardType river;

    @Enumerated(value = EnumType.STRING)
    private StageType stageType;

    private boolean finished;
}
