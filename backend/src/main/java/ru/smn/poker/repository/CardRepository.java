package ru.smn.poker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smn.poker.entities.CardEntity;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {}
