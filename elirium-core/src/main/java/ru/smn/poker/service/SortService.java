package ru.smn.poker.service;

import ru.smn.poker.entities.PlayerEntity;
import ru.smn.poker.enums.StageType;

import java.util.List;

public interface SortService {
    List<PlayerEntity> sort(List<PlayerEntity> players, StageType stageType);
}
