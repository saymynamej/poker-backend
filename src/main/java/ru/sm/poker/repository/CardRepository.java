package ru.sm.poker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sm.poker.model.CardEntity;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {}
