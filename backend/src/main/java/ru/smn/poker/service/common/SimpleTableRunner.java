package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.game.Table;
import ru.smn.poker.service.TableRunner;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class SimpleTableRunner implements TableRunner {
    private final ExecutorService tables = Executors.newCachedThreadPool();
    private final Map<String, Table> games;

    @Override
    public void run(Table table) {
        if (games.containsKey(table.getGameName())) {
            throw new RuntimeException("table with name: " + table.getGameName() + " already exist");
        }
        games.put(table.getGameName(), table);
        tables.submit(() -> {
            try {
                table.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
