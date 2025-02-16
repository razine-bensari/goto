package com.interview.game.service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.interview.game.exception.PlayerNotFound;
import com.interview.game.model.PlayerModel;
import com.interview.game.repository.PlayerRepository;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PlayerService {

	private final PlayerRepository playerRepository;

	@Autowired
	public PlayerService(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}

	public PlayerModel createPlayer(PlayerModel playerModel) {
		return playerRepository.save(playerModel);
	}

	public Optional<PlayerModel> getPlayer(UUID playerId) {
		return playerRepository.findById(playerId);
	}

	public List<PlayerModel> getAllPlayers() {
		return playerRepository.findAll();
	}

	public PlayerModel updatePlayer(PlayerModel playerModel) {
		return playerRepository.save(playerModel);
	}

	public void deletePlayer(UUID playerId) {
		playerRepository.deleteById(playerId);
	}
}
