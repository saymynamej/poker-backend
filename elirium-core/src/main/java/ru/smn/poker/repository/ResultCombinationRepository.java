package ru.smn.poker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smn.poker.entities.ResultCombinationEntity;

@Repository
public interface ResultCombinationRepository extends JpaRepository<ResultCombinationEntity, Long> {
}
