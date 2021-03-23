package ru.smn.poker.generator;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.smn.poker.entities.ChipsCountEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.entities.PlayerSettingsEntity;
import ru.smn.poker.enums.AccessType;
import ru.smn.poker.enums.PlayerType;
import ru.smn.poker.enums.StateType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Collections.singletonList;

@Service
@RequiredArgsConstructor
public class PlayerGenerator {
    private final PasswordEncoder passwordEncoder;

    public List<PlayerEntity> generate(int countOfPlayers, String tableName) {
        return IntStream.range(0, countOfPlayers).mapToObj(i -> PlayerEntity.builder()
                .name(String.valueOf(i))
                .enable(true)
                .accessType(AccessType.FULL)
                .settings(singletonList(PlayerSettingsEntity.builder()
                        .timeBank(50000L)
                        .stateType(StateType.IN_GAME)
                        .tableName(tableName)
                        .chipsCount(ChipsCountEntity.builder()
                                .count(5000L)
                                .build())
                        .playerType(PlayerType.ORDINARY)
                        .build()))
                .password(passwordEncoder.encode(String.valueOf(i)))
                .build())
                .collect(Collectors.toList());
    }
}
