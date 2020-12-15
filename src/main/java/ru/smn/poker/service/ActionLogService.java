package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smn.poker.action.Action;
import ru.smn.poker.action.ExecutableAction;
import ru.smn.poker.entities.ActionEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.repository.ActionRepository;
import ru.smn.poker.repository.PlayerRepository;
import ru.smn.poker.repository.RoundRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActionLogService {
    private final ActionRepository actionRepository;
    private final PlayerRepository playerRepository;
    private final RoundRepository roundRepository;

    @Transactional
    public void log(PlayerEntity player, Action action, RoundSettings roundSettings) {
        final long count = action instanceof ExecutableAction ? ((ExecutableAction) action).getCount() : 0;

        final Optional<PlayerEntity> byId = playerRepository.findById(player.getId());

        save(ActionEntity.builder()
                .count(count)
                .round(roundRepository.findById(roundSettings.getRoundId()).orElseThrow())
                .actionType(action.getActionType())
                .player(byId.get())
                .build());
    }

    @Transactional
    public void save(ActionEntity actionEntity) {
        actionRepository.save(actionEntity);
    }
}
