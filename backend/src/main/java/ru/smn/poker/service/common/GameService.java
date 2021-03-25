package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smn.poker.action.Action;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.repository.HandRepository;

import static ru.smn.poker.util.HistoryUtil.addActionInHistory;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final HandRepository handRepository;
    private final TableService tableService;

    @Transactional
    public void update(TableSettings tableSettings) {
//        final TableEntity tableEntity = TableSettingsConverter.toEntity(tableSettings);
//        tableService.save(tableEntity);
    }

    public void doAction(
            PlayerEntity player,
            TableSettings tableSettings,
            long removeChips,
            long lastBet,
            Action action
    ) {
        player.getTableSettings().removeChips(removeChips);
        tableSettings.setBank(tableSettings.getBank() + removeChips);
        setLastBet(tableSettings, lastBet);
        addActionInHistory(tableSettings, player);
    }

    public void setActivePlayer(TableSettings tableSettings, PlayerEntity player) {
        player.getTableSettings().setActive(true);
        tableSettings.setActivePlayer(player);
        update(tableSettings);
    }

    public void setInActivePlayer(TableSettings tableSettings, PlayerEntity player) {
        player.getTableSettings().setActive(false);
        tableSettings.setActivePlayer(null);
        update(tableSettings);
    }

    public void setLastBet(TableSettings tableSettings, long count) {
        tableSettings.setLastBet(count);
    }

}
