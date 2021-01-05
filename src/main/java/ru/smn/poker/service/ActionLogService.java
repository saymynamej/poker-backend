package ru.smn.poker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smn.poker.action.Action;
import ru.smn.poker.action.ExecutableAction;
import ru.smn.poker.action.holdem.Bet;
import ru.smn.poker.entities.ActionEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.entities.RoundEntity;
import ru.smn.poker.enums.ActionType;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.repository.ActionRepository;
import ru.smn.poker.repository.PlayerRepository;
import ru.smn.poker.repository.RoundRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActionLogService {
    private final ActionRepository actionRepository;
    private final PlayerRepository playerRepository;
    private final RoundRepository roundRepository;

    public void logBlinds(RoundSettings roundSettings) {
        final RoundEntity roundEntity = roundRepository.findById(roundSettings.getRoundId()).orElseThrow();

        final boolean isLoggedBlind = roundEntity.getActions().stream()
                .anyMatch(actionEntity -> actionEntity.getActionType() == ActionType.BB_BET);

        if (!isLoggedBlind) {
            final ActionEntity sbAction = ActionEntity.builder()
                    .count(roundSettings.getSmallBlindBet())
                    .stageType(roundEntity.getStageType())
                    .player(roundEntity.getSmallBlind() == null ? roundEntity.getButton() : roundEntity.getSmallBlind())
                    .actionType(roundEntity.getSmallBlind() == null ? ActionType.BUTTON_BET : ActionType.SB_BET)
                    .round(roundEntity)
                    .build();

            final ActionEntity bbAction = ActionEntity.builder()
                    .count(roundSettings.getBigBlindBet())
                    .player(roundEntity.getBigBlind())
                    .stageType(roundEntity.getStageType())
                    .actionType(ActionType.BB_BET)
                    .round(roundEntity)
                    .build();

            saveAll(List.of(sbAction, bbAction));
        }
    }

    @Transactional
    public void log(PlayerEntity player, Action action, RoundSettings roundSettings) {
        final long count = action instanceof ExecutableAction ? ((ExecutableAction) action).getCount() : 0;

        final Optional<PlayerEntity> optionalPlayerEntity = playerRepository.findById(player.getId());

        final Optional<RoundEntity> optionalRoundEntity = roundRepository.findById(roundSettings.getRoundId());

        save(ActionEntity.builder()
                .count(count)
                .round(optionalRoundEntity.orElseThrow())
                .actionType(action.getActionType())
                .stageType(roundSettings.getStageType())
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
