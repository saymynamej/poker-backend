package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smn.poker.action.Action;
import ru.smn.poker.converter.TableSettingsConverter;
import ru.smn.poker.entities.ActionEntity;
import ru.smn.poker.entities.HandEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.entities.TableEntity;
import ru.smn.poker.enums.StageType;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.repository.HandRepository;
import ru.smn.poker.repository.PlayerRepository;
import ru.smn.poker.repository.TableRepository;

import static ru.smn.poker.util.HistoryUtil.addActionInFullHistory;
import static ru.smn.poker.util.HistoryUtil.addActionInStageHistory;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final HandRepository handRepository;
    private final TableService tableService;
    private final TableRepository tableRepository;
    private final PlayerRepository playerRepository;

    @Transactional
    public void update(TableSettings tableSettings) {
//        final TableEntity tableEntity = TableSettingsConverter.toEntity(tableSettings);
//        tableRepository.save(tableEntity);
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
        addActionInStageHistory(tableSettings, player);
        addActionInFullHistory(tableSettings, player);
    }

    private void updateAction(Long playerId, StageType stageType, long tableId, Action action) {
        final HandEntity handEntity = handRepository.findByTableIdAndFinishedFalse(tableId)
                .orElseThrow();

        handEntity.getActions().add(ActionEntity.builder()
                .actionType(action.getActionType())
                .count(action.getCount())
                .hand(handEntity)
                .player(new PlayerEntity(playerId))
                .stageType(stageType)
                .build());

        handRepository.save(handEntity);

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
