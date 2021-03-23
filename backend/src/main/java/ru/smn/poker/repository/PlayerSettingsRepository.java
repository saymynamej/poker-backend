package ru.smn.poker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smn.poker.entities.PlayerSettingsEntity;

@Repository
public interface PlayerSettingsRepository extends JpaRepository<PlayerSettingsEntity, Long> {
}
