package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smn.poker.converter.RoundSettingsConverter;
import ru.smn.poker.dto.Player;
import ru.smn.poker.entities.GameEntity;
import ru.smn.poker.entities.RoundEntity;
import ru.smn.poker.game.RoundSettings;
import ru.smn.poker.repository.GameRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.smn.poker.util.HistoryUtil.addActionInHistory;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final GameRepository gameRepository;

    public List<GameEntity> findAll() {
        return gameRepository.findAll();
    }

    @Transactional
    public GameEntity saveGame(GameEntity gameEntity) {
        return gameRepository.save(gameEntity);
    }

    @Transactional
    public void update(RoundSettings roundSettings) {
        final long gameId = roundSettings.getGameId();
        final Optional<GameEntity> rounds = gameRepository.findById(gameId);

        final GameEntity gameEntity = RoundSettingsConverter.toEntity(
                roundSettings,
                Objects.requireNonNull(rounds.map(GameEntity::getRounds).orElse(null))
        );

        gameRepository.save(gameEntity);
    }

    public long getNextGameId() {
        final GameEntity gameEntityWithMaxId = gameRepository.findGameEntityWithMaxId();
        if (gameEntityWithMaxId == null){
            return 1;
        }
        Long id = gameEntityWithMaxId.getId();
        return ++id;
    }

    public void doAction(Player player, RoundSettings roundSettings, long removeChips, long lastBet) {
        player.removeChips(removeChips);
        addBank(roundSettings, removeChips);
        setLastBet(roundSettings, lastBet);
        addActionInHistory(roundSettings, player);
    }

    public void addBank(RoundSettings roundSettings, long count) {
        roundSettings.setBank(roundSettings.getBank() + count);
    }

    public void setActivePlayer(RoundSettings roundSettings, Player player) {
        player.setActive(true);
        roundSettings.setActivePlayer(player);
    }

    public void setInActivePlayer(RoundSettings roundSettings, Player player) {
        player.setActive(false);
        roundSettings.setActivePlayer(null);
    }

    public void setLastBet(RoundSettings roundSettings, long count) {
        if (roundSettings.getLastBet() < count) {
            log.info("prev last bet less than new last bet");
        }
        roundSettings.setLastBet(count);
    }

}
