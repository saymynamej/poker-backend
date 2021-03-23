package ru.smn.poker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.smn.poker.entities.GameEntity;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long> {

    @Query("from GameEntity where id = (select max(id) from GameEntity)")
    GameEntity findGameEntityWithMaxId();
}
