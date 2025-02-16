package com.interview.game.controller;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import com.interview.game.dto.CreatePlayerRequest;
import com.interview.game.dto.UpdatePlayerRequest;
import com.interview.game.model.PlayerModel;
import com.interview.game.service.GameService;
import com.interview.game.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/players")
@RequiredArgsConstructor
public class PlayerController {

	private static final Pattern PHONE_NUMBER_REGEX = Pattern.compile("^\\+?[1-9]\\d{1,14}$");
	public static final String PLAYER_NOT_FOUND = "Player not found";

	private final PlayerService playerService;
	private final GameService gameService;

	@PostMapping
	public ResponseEntity<PlayerModel> createPlayer(@RequestBody CreatePlayerRequest request) {
		validateCreatePlayerRequest(request);
		PlayerModel player = playerService.createPlayer(PlayerModel.builder()
				.name(request.getName())
				.phoneNumber(request.getPhoneNumber())
				.username(request.getUsername())
				.lastName(request.getLastName())
				.email(request.getEmail())
				.build());
		return ResponseEntity.status(HttpStatus.CREATED).body(player);
	}

	@GetMapping("/{playerId}")
	public ResponseEntity<PlayerModel> getPlayer(@PathVariable UUID playerId) {
		var player = playerService.getPlayer(playerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PLAYER_NOT_FOUND));
		return ResponseEntity.ok(player);
	}

	@GetMapping
	public ResponseEntity<List<PlayerModel>> getAllPlayers() {
		return ResponseEntity.ok(playerService.getAllPlayers());
	}

	@PutMapping("/{playerId}")
	public ResponseEntity<PlayerModel> updatePlayer(@PathVariable UUID playerId, @RequestBody UpdatePlayerRequest request) {
		var player = playerService.getPlayer(playerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PLAYER_NOT_FOUND));
		validateUpdatePlayerRequest(request);
		player.toBuilder()
				.name(request.getName())
				.phoneNumber(request.getPhoneNumber())
				.username(request.getUsername())
				.lastName(request.getLastName())
				.build();
		return ResponseEntity.ok(playerService.updatePlayer(player));
	}

	@DeleteMapping("/{playerId}")
	public ResponseEntity<Void> deletePlayer(@PathVariable UUID playerId) {
		var player = playerService.getPlayer(playerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PLAYER_NOT_FOUND));


		if (!gameService.playerCanBeDeleted(player)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Player can't be deleted. Player is currently playing");
		}

		playerService.deletePlayer(playerId);
		return ResponseEntity.noContent().build();
	}

	private void validateCreatePlayerRequest(CreatePlayerRequest request) {
		if (request == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request cannot be null");
		}

		if (StringUtils.isBlank(request.getUsername()) || request.getUsername().length() > 255) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be blank or longer than 255 characters");
		}

		if (StringUtils.isBlank(request.getName()) || request.getName().length() > 255) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be blank or longer than 255 characters");
		}

		if (StringUtils.isBlank(request.getLastName()) || request.getLastName().length() > 255) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name cannot be blank or longer than 255 characters");
		}

		if (StringUtils.isBlank(request.getPhoneNumber()) || !PHONE_NUMBER_REGEX.matcher(request.getPhoneNumber()).matches()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number is invalid");
		}

		// add validation for email (no duplicate, enforced at DB level as well)... running out of time sorry.
	}

	private void validateUpdatePlayerRequest(UpdatePlayerRequest request) {
		if (request == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request cannot be null");
		}

		if (StringUtils.isBlank(request.getUsername()) || request.getUsername().length() > 255) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be blank or longer than 255 characters");
		}

		if (StringUtils.isBlank(request.getName()) || request.getName().length() > 255) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be blank or longer than 255 characters");
		}

		if (StringUtils.isBlank(request.getLastName()) || request.getLastName().length() > 255) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name cannot be blank or longer than 255 characters");
		}

		if (StringUtils.isBlank(request.getPhoneNumber()) || !PHONE_NUMBER_REGEX.matcher(request.getPhoneNumber()).matches()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number is invalid");
		}
	}
}
