package ru.sm.poker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sm.poker.entities.ChipsCountEntity;

@Repository
public interface ChipsCountRepository extends JpaRepository<ChipsCountEntity, Long> { }