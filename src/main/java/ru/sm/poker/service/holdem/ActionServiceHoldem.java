package ru.sm.poker.service.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.sm.poker.dto.CombinationDTO;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.game.GameManager;
import ru.sm.poker.game.SecurityService;
import ru.sm.poker.model.Player;
import ru.sm.poker.action.Action;
import ru.sm.poker.action.CountAction;
import ru.sm.poker.action.ExecutableAction;
import ru.sm.poker.action.holdem.Fold;
import ru.sm.poker.action.holdem.Wait;
import ru.sm.poker.service.ActionService;
import ru.sm.poker.service.TimeBankService;
import ru.sm.poker.service.WinnerService;

import java.util.List;
import java.util.Optional;
import java.util.Timer;

import static java.lang.String.format;

@RequiredArgsConstructor
@Slf4j
@Component
public class ActionServiceHoldem implements ActionService {

    private final SecurityService holdemSecurityService;
    private final GameManager holdemGameManager;
    private final BroadCastService broadCastService;
    private final WinnerService winnerServiceHoldem;
    private final TimeBankService timeBankService = new TimeBankService();

    @Override
    public void setActions(RoundSettingsDTO roundSettingsDTO) {
        roundSettingsDTO.getPlayers().forEach(player -> setAction(player, roundSettingsDTO));
        if (roundSettingsDTO.getStageType().equals(StageType.RIVER)) {
            final List<Pair<Player, CombinationDTO>> winners = checkWinner(roundSettingsDTO);
            broadCastService.sendToAllWithSecure(winners);
        }
    }


    @Override
    public void setUnSetAfkPlayer(String name) {
        final Optional<Pair<String, Player>> optionalPlayer = holdemGameManager.getPlayerByName(name);
        if (optionalPlayer.isPresent()) {
            final Player player = optionalPlayer.get().getValue();
            player.setStateType(player.getStateType() == StateType.AFK ? StateType.IN_GAME : StateType.AFK);
        }
    }

    @Override
    public void doAction(Player player, RoundSettingsDTO roundSettingsDTO) {
        final Action action = player.getAction();
        if (action instanceof ExecutableAction) {
            ((ExecutableAction) action).doAction(roundSettingsDTO, player, this);
        }
    }

    @Override
    public void setAction(String playerName, Action action) {
        final Optional<Pair<String, Player>> playerByName = holdemGameManager.getPlayerByName(playerName);
        if (playerByName.isPresent()) {
            final Pair<String, Player> pairGameAndPlayer = playerByName.get();
            final String gameName = pairGameAndPlayer.getLeft();
            final Player player = pairGameAndPlayer.getRight();
            if (holdemSecurityService.isLegalPlayer(gameName, player)) {
                player.setAction(action);
            } else {
                log.info(format("player  send bet to not own game. name:%s", player.getName()));
            }
        } else {
            log.info("cannot find player with playerName:" + playerName);
        }
    }

    @Override
    public void waitOneMoreAction(Player player, RoundSettingsDTO roundSettingsDTO) {
        setPlayerWait(player);
        waitPlayerAction(player, roundSettingsDTO);
    }

    @Override
    public void setAction(Player player, RoundSettingsDTO roundSettingsDTO) {
        if (player.getStateType() == StateType.IN_GAME) {
            if (player.getAction() != null && player.getAction().getClass() != Fold.class) {
                setActivePlayer(roundSettingsDTO, player);
                broadCastService.sendToAllWithSecure(roundSettingsDTO);
                waitPlayerAction(player, roundSettingsDTO);
                if (player.getAction() instanceof CountAction) {
                    log.info(roundSettingsDTO.getLastBet().toString());
                    log.info("player action: " + player.getAction().getActionType() + ":"
                            + ((CountAction) player.getAction()).getCount() + ", player name: " + player.getName());
                }
                setInActivePlayer(roundSettingsDTO, player);
            }
        }
    }

    @Override
    public void waitPlayerAction(Player player, RoundSettingsDTO roundSettingsDTO) {
        final Timer timer = timeBankService.activateTimeBank(player);
        while (true) {
            if (player.getStateType() == StateType.AFK){
                break;
            }
            if (checkAllAfk(roundSettingsDTO.getPlayers())) {
                break;
            }
            if (!(player.getAction() instanceof Wait)) {
                timer.cancel();
                doAction(player, roundSettingsDTO);
                break;
            }
        }
    }

    @Override
    public void setLastBet(RoundSettingsDTO roundSettingsDTO, long count) {
        if (roundSettingsDTO.getLastBet() < count) {
            log.info("prev last bet less than new last bet");
        }
        roundSettingsDTO.setLastBet(count);
    }

    @Override
    public void removeChipsPlayerAndAddToBank(Player player, long chips, RoundSettingsDTO roundSettingsDTO) {
        removeChips(player, chips);
        addBank(roundSettingsDTO, chips);
    }

    private boolean checkAllAfk(List<Player> players) {
        return players.stream()
                .allMatch(player -> player.getStateType() == StateType.AFK);
    }

    private List<Pair<Player, CombinationDTO>> checkWinner(RoundSettingsDTO roundSettingsDTO) {
        return winnerServiceHoldem.findWinners(
                roundSettingsDTO.getPlayers(),
                roundSettingsDTO.getFlop(),
                roundSettingsDTO.getTern(),
                roundSettingsDTO.getRiver()
        );
    }

    private Player getActivePlayer(RoundSettingsDTO roundSettingsDTO) {
        return roundSettingsDTO.getPlayers()
                .stream()
                .filter(Player::isActive)
                .findFirst()
                .orElse(null);
    }

    private void removeChips(Player player, long chips) {
        player.removeChips(chips);
    }

    private void addChips(Player player, long chips) {
        player.addChips(chips);
    }

    private void addBank(RoundSettingsDTO roundSettingsDTO, long count) {
        roundSettingsDTO.setBank(roundSettingsDTO.getBank() + count);
    }

    private void setActivePlayer(RoundSettingsDTO roundSettingsDTO, Player player) {
        player.setActive(true);
        roundSettingsDTO.setActivePlayer(player);
    }

    private void setInActivePlayer(RoundSettingsDTO roundSettingsDTO, Player player) {
        player.setActive(false);
        roundSettingsDTO.setActivePlayer(null);
    }

    @Override
    public void setPlayerWait(Player playerWait) {
        playerWait.setAction(new Wait(playerWait.getGameName()));
    }
}
