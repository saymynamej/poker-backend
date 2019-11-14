package ru.sm.poker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sm.poker.game.Game;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.Action;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActionService {

    private final Game game;
    private final SecurityService securityService;
    private final BroadCastService broadCastService;

    public void setAction(String name, Action action) {
        final Optional<Player> player = game.getPlayerByName(name);
        if (player.isPresent()) {
            final List<String> errors = securityService.check(player.get());
            if (!errors.isEmpty()) {
                broadCastService.sendErrorToUser(name, errors.toString());
                log.info(format("has some errors: %s", errors));
                return;
            }
            player.get().setAction(action);
        }
    }
}
