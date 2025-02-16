package com.interview.game.repository;

import java.util.UUID;
import com.interview.game.model.PlayerModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<PlayerModel, UUID> {
}
