package com.interview.game.repository;

import java.util.List;
import java.util.UUID;
import com.interview.game.model.GameModel;
import com.interview.game.model.PlayerModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<GameModel, UUID> {
	List<GameModel> findGameModelsByPlayers(List<PlayerModel> players);
}
