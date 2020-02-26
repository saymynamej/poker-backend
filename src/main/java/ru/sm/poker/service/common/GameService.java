package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.Player;
import ru.sm.poker.enums.ActionType;

import java.util.List;
import java.util.stream.Collectors;

import static ru.sm.poker.util.HistoryUtil.addActionInHistory;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    public void doAction(Player player, HoldemRoundSettingsDTO holdemRoundSettingsDTO, long removeChips, long lastBet) {
        player.removeChips(removeChips);
        addBank(holdemRoundSettingsDTO, removeChips);
        setLastBet(holdemRoundSettingsDTO, lastBet);
        addActionInHistory(holdemRoundSettingsDTO, player);
    }

    public void addBank(HoldemRoundSettingsDTO holdemRoundSettingsDTO, long count) {
        holdemRoundSettingsDTO.setBank(holdemRoundSettingsDTO.getBank() + count);
    }

    public void setActivePlayer(HoldemRoundSettingsDTO holdemRoundSettingsDTO, Player player) {
        player.setActive(true);
        holdemRoundSettingsDTO.setActivePlayer(player);
    }

    public void setInActivePlayer(HoldemRoundSettingsDTO holdemRoundSettingsDTO, Player player) {
        player.setActive(false);
        holdemRoundSettingsDTO.setActivePlayer(null);
    }

    public void setLastBet(HoldemRoundSettingsDTO holdemRoundSettingsDTO, long count) {
        if (holdemRoundSettingsDTO.getLastBet() < count) {
            log.info("prev last bet less than new last bet");
        }
        holdemRoundSettingsDTO.setLastBet(count);
    }

}
