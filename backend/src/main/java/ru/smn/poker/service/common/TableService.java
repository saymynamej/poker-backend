package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smn.poker.entities.HandEntity;
import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.entities.PlayerSettingsEntity;
import ru.smn.poker.entities.TableEntity;
import ru.smn.poker.game.TableSettings;
import ru.smn.poker.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TableService {
    private final TableRepository tableRepository;
    private final CardRepository cardRepository;
    private final ActionRepository actionRepository;
    private final ChipsCountRepository chipsCountRepository;
    private final PlayerRepository playerRepository;

    @Transactional
    public void save(TableEntity tableEntity) {
        tableRepository.save(tableEntity);
    }

    @Transactional
    public void updateInfo(PlayerEntity playerEntity) {
        final PlayerSettingsEntity playerSettings = playerEntity.getTableSettings();
        actionRepository.saveAll(playerSettings.getActions());
        chipsCountRepository.save(playerSettings.getChipsCount());
        playerRepository.save(playerEntity);
    }

    @Transactional
    public void saveCards(List<PlayerEntity> players) {
        cardRepository.saveAll(players.stream()
                .flatMap(player -> player.getTableSettings().getCards().stream())
                .collect(Collectors.toList()));
    }

    @Transactional
    public void updateHand(TableSettings tableSettings) {
        final TableEntity tableEntity = tableRepository.findById(tableSettings.getTableId()).orElseThrow();

        final List<HandEntity> hands = tableEntity.getHands();

        final HandEntity handEntity = hands.stream()
                .filter(hand -> hand.getId().equals(tableSettings.getHandId()))
                .findFirst()
                .orElseThrow();

        handEntity.setActivePlayer(tableSettings.getActivePlayer());
        handEntity.setFinished(tableSettings.isFinished());
        handEntity.setBigBlindBet(tableSettings.getBigBlindBet());
        handEntity.setSmallBlindBet(tableSettings.getSmallBlindBet());
        handEntity.setButton(tableSettings.getButton());
        handEntity.setTable(tableEntity);
        handEntity.setBank(tableSettings.getBank());
        handEntity.setSmallBlind(tableSettings.getSmallBlind());
        handEntity.setBigBlind(tableSettings.getBigBlind());
        handEntity.setStageType(tableSettings.getStageType());
        handEntity.setF1(tableSettings.getFlop() == null ? null : tableSettings.getFlop().get(0));
        handEntity.setF2(tableSettings.getFlop() == null ? null : tableSettings.getFlop().get(1));
        handEntity.setF3(tableSettings.getFlop() == null ? null : tableSettings.getFlop().get(2));
        handEntity.setTern(tableSettings.getTern());
        handEntity.setRiver(tableSettings.getRiver());
        handEntity.setLastBet(tableSettings.getLastBet());
    }


    public Optional<TableEntity> findById(Long id) {
        return tableRepository.findById(id);
    }

    public List<TableEntity> findAll() {
        return tableRepository.findAll();
    }
}
