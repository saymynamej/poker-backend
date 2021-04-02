package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.smn.poker.action.Action;
import ru.smn.poker.entities.ActionEntity;
import ru.smn.poker.entities.HandEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;

import static ru.smn.poker.util.HistoryUtil.addActionInFullHistory;
import static ru.smn.poker.util.HistoryUtil.addActionInStageHistory;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final TableService tableService;

    public void doAction(
            PlayerEntity player,
            TableSettings tableSettings,
            long removeChips,
            long lastBet,
            Action action
    ) {
        removeChipsFromPlayer(player, removeChips);
        setBank(tableSettings, removeChips);
        setLastBet(tableSettings, lastBet);
        addActionInStageHistory(tableSettings, player);
        addActionInFullHistory(tableSettings, player);
        addActionToPlayer(player, tableSettings, action);
        tableService.updateInfo(player);
        tableService.updateHand(tableSettings);
    }

    private void removeChipsFromPlayer(PlayerEntity player, long removeChips) {
        player.getTableSettings().removeChips(removeChips);
    }

    private void setBank(TableSettings tableSettings, long removeChips) {
        tableSettings.setBank(tableSettings.getBank() + removeChips);
    }

    private void addActionToPlayer(PlayerEntity player, TableSettings tableSettings, Action action) {
        player.addAction(action, tableSettings.getHandId(), tableSettings.getStageType());
    }

    public void setActivePlayer(TableSettings tableSettings, PlayerEntity player) {
        player.getTableSettings().setActive(true);
        tableSettings.setActivePlayer(player);
        tableService.updateHand(tableSettings);
    }

    public void setInActivePlayer(TableSettings tableSettings, PlayerEntity player) {
        player.getTableSettings().setActive(false);
        tableSettings.setActivePlayer(null);
    }

    public void setLastBet(TableSettings tableSettings, long count) {
        tableSettings.setLastBet(count);
    }

}
