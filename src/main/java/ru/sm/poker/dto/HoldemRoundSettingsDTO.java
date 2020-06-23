package ru.sm.poker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.sm.poker.action.Action;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.enums.CardType;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.util.PlayerUtil;

import java.util.List;
import java.util.Map;

import static ru.sm.poker.util.StreamUtil.playerInAllIn;
import static ru.sm.poker.util.StreamUtil.playersHasCheck;

@Builder
@Getter
@Setter
@ToString
public class HoldemRoundSettingsDTO {
    private final Map<PlayerDTO, List<Action>> stageHistory;
    private final Map<PlayerDTO, List<Action>> fullHistory;
    private final List<PlayerDTO> players;
    private final List<CardType> flop;
    private final CardType tern;
    private final CardType river;
    private final String gameName;
    private final long smallBlindBet;
    private final long bigBlindBet;
    private final PlayerDTO button;
    private final PlayerDTO smallBlind;
    private final PlayerDTO bigBlind;
    private final StageType stageType;
    private Long lastBet;
    private long bank;
    private PlayerDTO activePlayer;
    private boolean isAfk;


    public boolean lastBetIsNotZero() {
        return this.lastBet != 0;
    }

    public boolean playersInAllIn() {
        return getPlayersInGame()
                .stream()
                .allMatch(playerInAllIn());
    }

    public boolean allPlayersCheck() {
        return getPlayersInGame()
                .stream()
                .filter(playerInAllIn().negate())
                .allMatch(playersHasCheck());
    }

    public boolean isHU(){
        return players.stream()
                .filter(playerDTO -> playerDTO.getStateType() == StateType.IN_GAME)
                .count() == 2;
    }

    public List<PlayerDTO> getPlayersInGame(){
        return PlayerUtil.getPlayersInGame(this.players);
    }

    public boolean isOnePlayerLeft() {
        return getPlayersInGame().size() == 1;
    }

    public boolean isOnePlayerWhoHasChips() {
        return this.players.stream()
                .filter(player -> player.getAction().getActionType() != ActionType.ALLIN && player.getChipsCount() != 0)
                .count() == 1;
    }
}
