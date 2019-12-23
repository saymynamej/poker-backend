package ru.sm.poker.service.holdem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.sm.poker.enums.StateType;
import ru.sm.poker.game.holdem.HoldemManager;
import ru.sm.poker.game.holdem.HoldemSecurityService;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.RoundSettings;
import ru.sm.poker.model.action.*;
import ru.sm.poker.service.ActionService;

import java.util.Optional;

import static java.lang.String.*;

@RequiredArgsConstructor
@Slf4j
@Component
public class ActionServiceHoldem implements ActionService {

    private final HoldemSecurityService holdemSecurityService;
    private final HoldemManager holdemManager;
    private final WaitActionService waitActionService;
    private final BroadCastService broadCastService;

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
    public void setActions(RoundSettings roundSettings) {
        roundSettings.getPlayers().forEach(player -> {
            if (player.getStateType() == StateType.IN_GAME) {
                setActivePlayer(roundSettings, player);
                broadCastService.sendToAll(roundSettings);
                waitActionService.waitPlayerAction(player, roundSettings.getPlayers(), roundSettings);
                setInActivePlayer(roundSettings, player);
            }
        });
    }

    @Override
    public void parseAction(Player player, RoundSettings roundSettings) {
        final Action action = player.getAction();
        if (action instanceof Call) {
            call(player, (Call) action, roundSettings);
        } else if (action instanceof Fold) {
            fold(player, (Fold) action);
        } else if (action instanceof Raise) {
            raise(player, (Raise) action, roundSettings);
        }

        player.setAction(new Wait(player.getGameName()));
    }

    private void raise(Player player, Raise raise, RoundSettings roundSettings) {
        if (raise.getCount() < roundSettings.getLastBet() * 2) {
            player.setAction(new Wait(player.getGameName()));
            waitActionService.waitPlayerAction(player, roundSettings.getPlayers(), roundSettings);
        }
        removeChipsPlayerAndAddToBank(player, raise.getCount(), roundSettings);
        setLastBet(roundSettings, raise.getCount());
    }

    private void setLastBet(RoundSettings roundSettings, long count) {
        roundSettings.setLastBet(count);
    }

    private void fold(Player player, Fold fold) {
        player.setAction(fold);
        player.setStateType(StateType.WAIT);
    }

    private void removeChipsPlayerAndAddToBank(Player player, long chips, RoundSettings roundSettings) {
        removeChips(player, chips);
        addBank(roundSettings, chips);
    }

    private Player getActivePlayer(RoundSettings roundSettings) {
        return
                roundSettings.getPlayers()
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

    private void addBank(RoundSettings roundSettings, long count) {
        roundSettings.setBank(roundSettings.getBank() + count);
    }

    private void setActivePlayer(RoundSettings roundSettings, Player player) {
        player.setActive(true);
        roundSettings.setActivePlayer(player);
    }

    private void setInActivePlayer(RoundSettings roundSettings, Player player) {
        player.setActive(false);
        roundSettings.setActivePlayer(null);
    }


    private void call(Player player, Call call, RoundSettings roundSettings) {
        if (call.getCount() != roundSettings.getLastBet()) {
            player.setAction(new Wait(player.getGameName()));
            waitActionService.waitPlayerAction(player, roundSettings.getPlayers(), roundSettings);
            return;
        }
        removeChipsPlayerAndAddToBank(player, call.getCount(), roundSettings);
        roundSettings.setLastBet(call.getCount());
    }

}
