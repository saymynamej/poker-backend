package ru.smn.poker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.smn.poker.entities.TableEntity;

public interface TableRepository extends JpaRepository<TableEntity, Long> {
}
