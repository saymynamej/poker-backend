package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.dto.PlayerCombination;
import ru.smn.poker.entities.CardEntity;
import ru.smn.poker.enums.CardType;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.service.CombinationService;
import ru.smn.poker.service.WinnerService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.List.of;

@Service
@RequiredArgsConstructor
public class SimpleWinnerService implements WinnerService {
    private final CombinationService combinationService;

    @Override
    public List<PlayerCombination> findWinners(TableSettings tableSettings) {
        final List<CardType> deck = Stream.concat(
                tableSettings.getFlop().stream(),
                of(tableSettings.getTern(), tableSettings.getRiver()).stream()
        ).collect(Collectors.toList());

        List<PlayerCombination> playerCombinations = tableSettings.getPlayersInGame().stream()
                .map(player -> {
                    final List<CardType> concatCards = Stream.concat(
                            deck.stream(),
                            player.getTableSettings().getCards()
                                    .stream()
                                    .map(CardEntity::getCardType)
                    ).collect(Collectors.toList());

                    return PlayerCombination.builder()
                            .combination(combinationService.findCombination(concatCards))
                            .player(player)
                            .build();
                })
                .collect(Collectors.toList());

        final Integer maxPowerOfCombinationType = playerCombinations.stream()
                .map(combo -> combo.getCombination().getCombinationType().getPower())
                .max(Integer::compareTo)
                .orElseThrow(() -> new RuntimeException("cannot find maxPowerOfCombinations"));

        playerCombinations = playerCombinations.stream()
                .filter(playerCombination -> playerCombination.getCombination().getCombinationType().getPower() == maxPowerOfCombinationType)
                .collect(Collectors.toList());

        final Integer maxPowerOfCombination = playerCombinations.stream()
                .map(combo -> combo.getCombination().getPower())
                .max(Integer::compareTo)
                .orElseThrow(() -> new RuntimeException("cannot find maxPowerOfCombinations"));

        playerCombinations = playerCombinations.stream()
                .filter(playerCombination -> playerCombination.getCombination().getPower().equals(maxPowerOfCombination))
                .collect(Collectors.toList());

        return playerCombinations;
    }

}
