package ru.sm.poker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sm.poker.entities.ActionEntity;

public interface ActionRepository extends JpaRepository<ActionEntity, Long> {
}
