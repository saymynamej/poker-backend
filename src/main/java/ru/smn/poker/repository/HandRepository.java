package ru.smn.poker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smn.poker.entities.HandEntity;

@Repository
public interface HandRepository extends JpaRepository<HandEntity, Long> {
}
