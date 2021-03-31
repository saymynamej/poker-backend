package ru.smn.poker.visitor;

import ru.smn.poker.game.TableSettingsManager;

public interface ITableSettingsManagerVisitor {
    void accept(TableSettingsManager manager);
}
