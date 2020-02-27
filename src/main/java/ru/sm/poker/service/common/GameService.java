package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.HoldemRoundSettings;
import ru.sm.poker.dto.Player;

import static ru.sm.poker.util.HistoryUtil.addActionInHistory;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

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
