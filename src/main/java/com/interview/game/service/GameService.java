package com.interview.game.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.interview.game.model.GameModel;
import com.interview.game.model.PlayerModel;
import com.interview.game.repository.GameRepository;
import com.interview.game.util.GameUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameService {

	private final GameRepository gameRepository;

	@Autowired
	public GameService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	public GameModel createGame(GameModel gameModel) {
		return gameRepository.save(gameModel);
	}

	public Optional<GameModel> getGame(UUID id) {
		return gameRepository.findById(id);
	}

	public List<GameModel> getAllGames() {
		return gameRepository.findAll();
	}

	public GameModel updateGame(GameModel gameModel) {
		return gameRepository.save(gameModel);
	}

	public void deleteGame(UUID id) {
		gameRepository.deleteById(id);
	}

	public boolean playerCanBeDeleted(PlayerModel playerModel) {
		return gameRepository.findGameModelsByPlayers(List.of(playerModel))
				.stream()
				.anyMatch(GameUtils::gameInProgress);
	}
}
