package ru.smn.poker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smn.poker.entities.ChipsCountEntity;

@Repository
public interface ChipsCountRepository extends JpaRepository<ChipsCountEntity, Long> { }
