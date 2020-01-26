package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.model.Player;

import static ru.sm.poker.util.HistoryUtil.addActionInHistory;

@Service
@RequiredArgsConstructor
public class GameService {

    private final RoundSettingsService roundSettingsService;

    public void removeChipsFromPlayer(Player player, RoundSettingsDTO roundSettingsDTO, long removeChips, long lastBet) {
        player.removeChips(removeChips);
        roundSettingsService.addBank(roundSettingsDTO, removeChips);
        roundSettingsDTO.setLastBet(lastBet);
        addActionInHistory(roundSettingsDTO, player);
    }
}
