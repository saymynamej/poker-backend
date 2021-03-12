package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smn.poker.action.Action;
import ru.smn.poker.action.ExecutableAction;
import ru.smn.poker.entities.ActionEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.entities.HandEntity;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.repository.ActionRepository;
import ru.smn.poker.repository.PlayerRepository;
import ru.smn.poker.repository.HandRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActionLogService {
    private final ActionRepository actionRepository;
    private final PlayerRepository playerRepository;
    private final HandRepository handRepository;

    @Transactional
    public void log(PlayerEntity player, Action action, TableSettings tableSettings) {
        final long count = action instanceof ExecutableAction ? ((ExecutableAction) action).getCount() : 0;

        final Optional<PlayerEntity> optionalPlayerEntity = playerRepository.findById(player.getId());

        final Optional<HandEntity> optionalRoundEntity = handRepository.findById(tableSettings.getHandId());

        if (optionalRoundEntity.isEmpty()){
            throw new RuntimeException("cannot find");
        }

        save(ActionEntity.builder()
                .count(count)
                .hand(optionalRoundEntity.orElseThrow())
                .actionType(action.getActionType())
                .stageType(tableSettings.getStageType())
                .player(optionalPlayerEntity.orElseThrow())
                .build());
    }

    @Transactional
    public void saveAll(List<ActionEntity> actionEntities) {
        actionRepository.saveAll(actionEntities);
    }

    @Transactional
    public void save(ActionEntity actionEntity) {
        actionRepository.save(actionEntity);
    }
}
