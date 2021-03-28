package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smn.poker.action.Action;
import ru.smn.poker.entities.HandEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.entities.TableEntity;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.repository.HandRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.smn.poker.util.HistoryUtil.addActionInFullHistory;
import static ru.smn.poker.util.HistoryUtil.addActionInStageHistory;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final HandRepository handRepository;
    private final TableService tableService;

    @Transactional
    public void update(TableSettings tableSettings) {
        final HandEntity handEntity = handRepository.findByTableId(tableSettings.getTableId())
                .orElseThrow();

        handEntity.setBank(tableSettings.getBank());
        handEntity.setTern(tableSettings.getTern());
        handEntity.setRiver(tableSettings.getRiver());
        handEntity.setActivePlayer(tableSettings.getActivePlayer());
        handEntity.setButton(tableSettings.getButton());
        handEntity.setBigBlind(tableSettings.getBigBlind());
        handEntity.setSmallBlind(tableSettings.getSmallBlind());
        handEntity.setLastBet(tableSettings.getLastBet());
        handEntity.setSmallBlindBet(tableSettings.getSmallBlindBet());
        handEntity.setBigBlindBet(tableSettings.getBigBlindBet());
        handEntity.setStageType(tableSettings.getStageType());

        if (tableSettings.getFlop() != null) {
            handEntity.setF1(tableSettings.getFlop().get(0));
            handEntity.setF2(tableSettings.getFlop().get(1));
            handEntity.setF3(tableSettings.getFlop().get(2));
        }

        tableSettings.getFullHistory().forEach((playerEntity, actions) -> {

            final List<Action> actionsEntity = actions.stream()
                    .map(action -> ActionType.getActionByType(action.getActionType(), 0L))
                    .collect(Collectors.toList());

        });


        handRepository.save(handEntity);
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
        update(tableSettings);
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
