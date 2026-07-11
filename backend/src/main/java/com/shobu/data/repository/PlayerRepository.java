package com.shobu.data.repository;

import com.shobu.data.entity.GameResult;
import com.shobu.data.entity.PlayerProfile;
import com.shobu.domain.enums.Stone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerProfile, UUID> {
    Optional<PlayerProfile> findByPlayerName(String playerName);
}