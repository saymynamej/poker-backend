package ru.sm.poker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sm.poker.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
}
