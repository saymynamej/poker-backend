package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sm.poker.action.Action;
import ru.sm.poker.action.ExecutableAction;
import ru.sm.poker.dto.Player;
import ru.sm.poker.entities.ActionEntity;
import ru.sm.poker.entities.PlayerEntity;
import ru.sm.poker.repository.ActionRepository;
import ru.sm.poker.repository.PlayerRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActionLogService {
    private final ActionRepository actionRepository;
    private final PlayerRepository playerRepository;

    @Transactional
    public void log(Player player, Action action) {
        final long count = action instanceof ExecutableAction ? ((ExecutableAction) action).getCount() : 0;

        Optional<PlayerEntity> byId = playerRepository.findById(player.getId());
        save(ActionEntity.builder()
                .count(count)
                .actionType(action.getActionType())
                .player(byId.get())
                .build());
    }

    @Transactional
    public void save(ActionEntity actionEntity) {
        actionRepository.save(actionEntity);
    }
}
