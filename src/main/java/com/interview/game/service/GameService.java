package com.interview.game.service;

import java.util.List;
import java.util.Optional;
import com.interview.game.model.Game;
import com.interview.game.repository.GameRepository;
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

	public Game createGame(Game game) {
		return gameRepository.save(game);
	}

	public Optional<Game> getGame(String id) {
		return gameRepository.findById(id);
	}

	public List<Game> getAllGames() {
		return gameRepository.findAll();
	}

	public Game updateGame(Game game) {
		return gameRepository.save(game);
	}

	public void deleteGame(String id) {
		gameRepository.deleteById(id);
	}
}
