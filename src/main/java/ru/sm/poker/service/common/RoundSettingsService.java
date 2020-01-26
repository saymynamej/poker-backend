package ru.sm.poker.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sm.poker.action.holdem.Fold;
import ru.sm.poker.dto.RoundSettingsDTO;
import ru.sm.poker.enums.ActionType;
import ru.sm.poker.model.Player;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoundSettingsService {

    public void addBank(RoundSettingsDTO roundSettingsDTO, long count) {
        roundSettingsDTO.setBank(roundSettingsDTO.getBank() + count);
    }

    public void setActivePlayer(RoundSettingsDTO roundSettingsDTO, Player player) {
        player.setActive(true);
        roundSettingsDTO.setActivePlayer(player);
    }

    public void setInActivePlayer(RoundSettingsDTO roundSettingsDTO, Player player) {
        player.setActive(false);
        roundSettingsDTO.setActivePlayer(null);
    }

    public void setLastBet(RoundSettingsDTO roundSettingsDTO, long count) {
        if (roundSettingsDTO.getLastBet() < count) {
            log.info("prev last bet less than new last bet");
        }
        roundSettingsDTO.setLastBet(count);
    }

    public boolean checkLastPlayer(RoundSettingsDTO roundSettingsDTO) {
        final List<Player> activePlayers = roundSettingsDTO.getPlayers().stream()
                .filter(pl -> !(pl.getAction().getActionType() ==  ActionType.FOLD))
                .collect(Collectors.toList());

        return activePlayers.size() == 1;
    }

}
