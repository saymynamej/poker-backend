package ru.sm.poker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sm.poker.game.Game;
import ru.sm.poker.model.Player;
import ru.sm.poker.model.action.*;

import java.util.List;
import java.util.Optional;

import static java.lang.String.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActionService {

    private final Game game;
    private final CheckService checkService;
    private final BroadCastService broadCastService;

    public void setAction(String name, Action action) {
        final Optional<Player> player = game.getPlayerByName(name);
        if (player.isPresent()) {
            final List<String> errors = checkService.check(player.get());

            if (!errors.isEmpty()) {
                broadCastService.sendErrorToUser(player.get().getName(), errors.toString());
                log.info(format("has some errors: %s", errors));
                return;
            }
            player.get().setAction(action);
        }
    }


}
