package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sm.poker.dto.HoldemRoundSettingsDTO;
import ru.sm.poker.dto.PlayerDTO;
import ru.sm.poker.enums.ActionType;

import java.util.List;
import java.util.stream.Collectors;

import static ru.sm.poker.util.HistoryUtil.addActionInHistory;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    public void removeChipsFromPlayer(PlayerDTO playerDTO, HoldemRoundSettingsDTO holdemRoundSettingsDTO, long removeChips, long lastBet) {
        playerDTO.removeChips(removeChips);
        addBank(holdemRoundSettingsDTO, removeChips);
        setLastBet(holdemRoundSettingsDTO, lastBet);
        addActionInHistory(holdemRoundSettingsDTO, playerDTO);
    }

    public void addBank(HoldemRoundSettingsDTO holdemRoundSettingsDTO, long count) {
        holdemRoundSettingsDTO.setBank(holdemRoundSettingsDTO.getBank() + count);
    }

    public void setActivePlayer(HoldemRoundSettingsDTO holdemRoundSettingsDTO, PlayerDTO playerDTO) {
        playerDTO.setActive(true);
        holdemRoundSettingsDTO.setActivePlayerDTO(playerDTO);
    }

    public void setInActivePlayer(HoldemRoundSettingsDTO holdemRoundSettingsDTO, PlayerDTO playerDTO) {
        playerDTO.setActive(false);
        holdemRoundSettingsDTO.setActivePlayerDTO(null);
    }

    public void setLastBet(HoldemRoundSettingsDTO holdemRoundSettingsDTO, long count) {
        if (holdemRoundSettingsDTO.getLastBet() < count) {
            log.info("prev last bet less than new last bet");
        }
        holdemRoundSettingsDTO.setLastBet(count);
    }

    public boolean checkLastPlayer(HoldemRoundSettingsDTO holdemRoundSettingsDTO) {
        final List<PlayerDTO> activePlayerDTOS = holdemRoundSettingsDTO.getPlayers().stream()
                .filter(pl -> !(pl.getAction().getActionType() == ActionType.FOLD))
                .collect(Collectors.toList());

        return activePlayerDTOS.size() == 1;
    }
}
