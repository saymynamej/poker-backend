package ru.sm.poker.service.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.sm.poker.dto.CombinationDTO;
import ru.sm.poker.enums.StageType;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.game.holdem.HoldemManager;
import ru.sm.poker.game.holdem.HoldemSecurityService;
import ru.sm.poker.model.Player;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.action.*;
import ru.sm.poker.service.ActionService;

import java.util.List;
import java.util.Optional;

import static java.lang.String.*;

@RequiredArgsConstructor
@Slf4j
@Component
public class ActionServiceHoldem implements ActionService {

    private final HoldemSecurityService holdemSecurityService;
    private final HoldemManager holdemManager;
    private final BroadCastService broadCastService;
    private final WinnerServiceHoldem winnerServiceHoldem;

    @Override
    public void setAction(String playerName, Action action) {
        final Optional<Pair<String, Player>> playerByName = holdemManager.getPlayerByName(playerName);

        if (playerByName.isPresent()) {

            final Pair<String, Player> pairGameAndPlayer = playerByName.get();

            final String gameName = pairGameAndPlayer.getLeft();
            final Player player = pairGameAndPlayer.getRight();

            if (holdemSecurityService.isLegalPlayer(gameName, player)) {
                player.setAction(action);
            } else {
                log.info(format("player tried send bet to not own game. name:%s", player.getName()));
            }

        } else {
            log.info("cannot find player with playerName:" + playerName);
        }
    }


    @Override
    public void setActions(RoundSettingsDTO roundSettingsDTO) {

        roundSettingsDTO.getPlayers().forEach(player -> {
            if (player.getStateType() == StateType.IN_GAME && player.getAction().getClass() != Fold.class) {
                log.info(roundSettingsDTO.getLastBet() + " is last bet");
                setActivePlayer(roundSettingsDTO, player);
                player.setAction(new Wait(player.getGameName()));
                broadCastService.sendToAllWithSecure(roundSettingsDTO);
                waitPlayerAction(player, roundSettingsDTO);
                setInActivePlayer(roundSettingsDTO, player);
            }
        });

        if (roundSettingsDTO.getStageType().equals(StageType.RIVER)) {
            final List<Pair<Player, CombinationDTO>> winners = checkWinner(roundSettingsDTO);
            broadCastService.sendToAllWithSecure(winners);
        }
    }

    @Override
    public void parseAction(Player player, RoundSettingsDTO roundSettingsDTO) {
        final Action action = player.getAction();
        if (action instanceof Call) {
            call(player, (Call) action, roundSettingsDTO);
        } else if (action instanceof Fold) {
            fold(player, (Fold) action);
        } else if (action instanceof Raise) {
            raise(player, (Raise) action, roundSettingsDTO);
        } else if (action instanceof Check) {
            check(player, (Check) action, roundSettingsDTO);
        }
    }


    private void waitPlayerAction(Player player, RoundSettingsDTO roundSettingsDTO) {
        while (true) {
            if (checkAllAfk(roundSettingsDTO.getPlayers())) {
                break;
            }
            if (!(player.getAction() instanceof Wait)) {
                parseAction(player, roundSettingsDTO);
                break;
            }
        }
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

    private boolean isLonely(List<Player> players) {
        return players.stream()
                .filter(player -> !(player.getAction() instanceof Fold))
                .count() == 1;
    }


    private void raise(Player player, Raise raise, RoundSettingsDTO roundSettingsDTO) {
        if (raise.getCount() < roundSettingsDTO.getLastBet() * 2) {
            setPlayerWait(player);
            waitPlayerAction(player, roundSettingsDTO);
        }
        removeChipsPlayerAndAddToBank(player, raise.getCount(), roundSettingsDTO);
        setLastBet(roundSettingsDTO, raise.getCount());
    }

    private void setLastBet(RoundSettingsDTO roundSettingsDTO, long count) {
        roundSettingsDTO.setLastBet(count);
    }

    private void fold(Player player, Fold fold) {
        player.setAction(fold);
    }

    private void removeChipsPlayerAndAddToBank(Player player, long chips, RoundSettingsDTO roundSettingsDTO) {
        removeChips(player, chips);
        addBank(roundSettingsDTO, chips);
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

    private void call(Player player, Call call, RoundSettingsDTO roundSettingsDTO) {
        if (call.getCount() != roundSettingsDTO.getLastBet()) {
            setPlayerWait(player);
            waitPlayerAction(player, roundSettingsDTO);
            return;
        }
        removeChipsPlayerAndAddToBank(player, call.getCount(), roundSettingsDTO);
        roundSettingsDTO.setLastBet(call.getCount());
    }

    private void check(Player player, Check check, RoundSettingsDTO roundSettingsDTO) {
        if (roundSettingsDTO.getLastBet() == roundSettingsDTO.getBigBlindBet() && player.isBigBlind()) {
            setPlayerWait(player);
            player.setAction(check);
            return;
        }
        waitPlayerAction(player, roundSettingsDTO);
    }

    private void setPlayerWait(Player playerWait) {
        playerWait.setAction(new Wait(playerWait.getGameName()));
    }
}
