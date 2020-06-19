package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;

import static ru.sm.poker.util.HistoryUtil.addActionInHistory;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    public void doAction(PlayerDTO player, HoldemRoundSettingsDTO holdemRoundSettings, long removeChips, long lastBet) {
        player.removeChips(removeChips);
        addBank(holdemRoundSettings, removeChips);
        setLastBet(holdemRoundSettings, lastBet);
        addActionInHistory(holdemRoundSettings, player);
    }

    public void addBank(HoldemRoundSettingsDTO holdemRoundSettings, long count) {
        holdemRoundSettings.setBank(holdemRoundSettings.getBank() + count);
    }

    public void setActivePlayer(HoldemRoundSettingsDTO holdemRoundSettings, PlayerDTO player) {
        player.setActive(true);
        holdemRoundSettings.setActivePlayer(player);
    }

    public void setInActivePlayer(HoldemRoundSettingsDTO holdemRoundSettings, PlayerDTO player) {
        player.setActive(false);
        holdemRoundSettings.setActivePlayer(null);
    }

    public void setLastBet(HoldemRoundSettingsDTO holdemRoundSettings, long count) {
        if (holdemRoundSettings.getLastBet() < count) {
            log.info("prev last bet less than new last bet");
        }
        holdemRoundSettings.setLastBet(count);
    }

}
