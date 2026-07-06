package com.shobu.data.repository;

import com.shobu.data.entity.GameResult;
import com.shobu.domain.enums.Stone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {
    long countByWinningPlayerId(UUID playerId);
    long countByWinner(Stone winner);
}