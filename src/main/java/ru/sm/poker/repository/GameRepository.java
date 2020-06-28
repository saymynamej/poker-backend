package ru.sm.poker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sm.poker.model.GameEntity;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long> {
}
