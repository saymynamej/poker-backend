package ru.sm.poker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.RoundSettings;
import ru.sm.poker.model.action.Action;
import ru.sm.poker.model.action.Call;
import ru.sm.poker.model.action.Fold;
import ru.sm.poker.model.action.Raise;

@Component
@RequiredArgsConstructor
public class ActionService {
//
//    public void doAction(Action action, RoundSettings roundSettings) {
//
//    }
//
//    private void raise(Player player, Raise raise, RoundSettings roundSettings) {
//        if (raise.getCount() < roundSettings.getLastBet() * 2) {
//            return;
//        }
//        removeChipsPlayerAndAddToBank(player, raise.getCount());
//        setLastBet(raise.getCount());
//    }
//
//
//    private void fold(Player player, Fold fold) {
//        player.setAction(fold);
//    }
//
//
//
//    private void call(Player player, Call call) {
//        if (call.getCount() != roundSettings.getLastBet()) {
//            return;
//        }
//        removeChipsPlayerAndAddToBank(player, call.getCount());
//    }

}
