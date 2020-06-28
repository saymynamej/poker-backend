package ru.sm.poker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sm.poker.model.ActionEntity;

public interface ActionRepository extends JpaRepository<ActionEntity, Long> {
}
