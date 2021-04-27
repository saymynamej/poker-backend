package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.dto.PlayerCombination;
import ru.smn.poker.entities.ResultCombinationEntity;
import ru.smn.poker.entities.HandEntity;
import ru.smn.poker.repository.ResultCombinationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultCombinationService {
    private final ResultCombinationRepository combinationRepository;

    public void saveCombinations(List<PlayerCombination> playerCombinations, List<PlayerCombination> winners, long handId) {
        combinationRepository.saveAll(playerCombinations.stream().map(playerCombination -> ResultCombinationEntity.builder()
                .cards(playerCombination.getCombination().getCards().toString())
                .combinationType(playerCombination.getCombination().getCombinationType())
                .power(playerCombination.getCombination().getPower())
                .player(playerCombination.getPlayer())
                .winner(winners.contains(playerCombination))
                .handEntity(HandEntity.builder()
                        .id(handId)
                        .build())
                .build())
                .collect(Collectors.toList())
        );
    }
}
