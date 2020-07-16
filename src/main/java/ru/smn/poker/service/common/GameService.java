package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smn.poker.converter.RoundSettingsConverter;
import ru.smn.poker.dto.HoldemRoundSettings;
import ru.smn.poker.dto.Player;
import ru.smn.poker.entities.GameEntity;
import ru.smn.poker.repository.GameRepository;
import ru.smn.poker.repository.PlayerRepository;

import java.util.List;

import static ru.smn.poker.util.HistoryUtil.addActionInHistory;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public List<GameEntity> findAll() {
        return gameRepository.findAll();
    }

    @Transactional
    public GameEntity saveGame(GameEntity gameEntity) {
        return gameRepository.save(gameEntity);
    }

    @Transactional
    public void update(HoldemRoundSettings holdemRoundSettings) {
        final GameEntity gameEntity = RoundSettingsConverter.toEntity(holdemRoundSettings);
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

    public void doAction(Player player, HoldemRoundSettings holdemRoundSettings, long removeChips, long lastBet) {
        player.removeChips(removeChips);
        addBank(holdemRoundSettings, removeChips);
        setLastBet(holdemRoundSettings, lastBet);
        addActionInHistory(holdemRoundSettings, player);
    }

    public void addBank(HoldemRoundSettings holdemRoundSettings, long count) {
        holdemRoundSettings.setBank(holdemRoundSettings.getBank() + count);
    }

    public void setActivePlayer(HoldemRoundSettings holdemRoundSettings, Player player) {
        player.setActive(true);
        holdemRoundSettings.setActivePlayer(player);
    }

    public void setInActivePlayer(HoldemRoundSettings holdemRoundSettings, Player player) {
        player.setActive(false);
        holdemRoundSettings.setActivePlayer(null);
    }

    public void setLastBet(HoldemRoundSettings holdemRoundSettings, long count) {
        if (holdemRoundSettings.getLastBet() < count) {
            log.info("prev last bet less than new last bet");
        }
        holdemRoundSettings.setLastBet(count);
    }

}
