package ru.smn.poker.visitor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.entities.CardEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.game.TableSettingsManager;
import ru.smn.poker.repository.CardRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TableSettingsManagerVisitorImpl implements ITableSettingsManagerVisitor {
    private final CardRepository cardRepository;

    @Override
    public void accept(TableSettingsManager manager) {
        final TableSettings currentSettings = manager.getCurrentSettings();

        final List<PlayerEntity> players = currentSettings.getPlayers();

        players.forEach(playerEntity -> {
            final List<CardEntity> cards = playerEntity.getTableSettings().getCards().stream()
                    .map(cardEntity -> CardEntity.builder()
                            .cardType(cardEntity.getCardType())
                            .player(playerEntity)
                            .settings(playerEntity.getTableSettings())
                            .build())
                    .collect(Collectors.toList());

            cardRepository.saveAll(cards);
        });
    }
}
