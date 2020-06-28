package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sm.poker.action.Action;
import ru.sm.poker.action.ExecutableAction;
import ru.sm.poker.converter.PlayerConverter;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.model.ActionEntity;
import ru.sm.poker.repository.ActionRepository;

@Service
@RequiredArgsConstructor
public class ActionLogService {
    private final ActionRepository actionRepository;

    public void log(PlayerDTO player, Action action) {
        final long count = action instanceof ExecutableAction ? ((ExecutableAction) action).getCount() : 0;
        save(ActionEntity.builder()
                .count(count)
                .actionType(action.getActionType())
                .player(PlayerConverter.toEntity(player))
                .build());

    }

    public void save(ActionEntity actionEntity) {
        actionRepository.save(actionEntity);
    }
}
