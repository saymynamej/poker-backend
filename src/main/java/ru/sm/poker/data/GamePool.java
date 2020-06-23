package ru.sm.poker.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sm.poker.game.Game;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class GamePool {
    private final ExecutorService executorForListeners = Executors.newFixedThreadPool(1);
    private final ExecutorService executorServiceForGames = Executors.newFixedThreadPool(15);

    public void startGame(Game game){
        executorServiceForGames.submit(game::start);
    }

    public void addListener(Runnable task){
        executorForListeners.submit(task);
    }
}
