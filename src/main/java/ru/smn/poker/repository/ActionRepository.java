package ru.smn.poker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.smn.poker.entities.ActionEntity;

public interface ActionRepository extends JpaRepository<ActionEntity, Long> {
}
