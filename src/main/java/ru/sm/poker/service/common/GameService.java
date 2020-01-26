package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.model.Player;

import static ru.sm.poker.util.HistoryUtil.addActionInHistory;

@Service
@RequiredArgsConstructor
public class GameService {

    private final RoundSettingsService roundSettingsService;

    public void removeChipsFromPlayer(Player player, HoldemRoundSettingsDTO holdemRoundSettingsDTO, long removeChips, long lastBet) {
        player.removeChips(removeChips);
        roundSettingsService.addBank(holdemRoundSettingsDTO, removeChips);
        holdemRoundSettingsDTO.setLastBet(lastBet);
        addActionInHistory(holdemRoundSettingsDTO, player);
    }
}
