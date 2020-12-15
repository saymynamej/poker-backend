package ru.smn.poker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smn.poker.entities.RoundEntity;

@Repository
public interface RoundRepository extends JpaRepository<RoundEntity, Long> {
}
