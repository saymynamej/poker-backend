package ru.sm.poker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sm.poker.entities.PlayerEntity;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
}
