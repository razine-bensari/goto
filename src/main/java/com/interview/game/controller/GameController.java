package com.interview.game.controller;

import static com.interview.game.controller.DeckController.DECK_NOT_FOUND;
import static com.interview.game.controller.PlayerController.PLAYER_NOT_FOUND;
import static com.interview.game.util.GameUtils.gameInProgress;
import static com.interview.game.util.GameUtils.gameIsReady;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import com.interview.game.model.GameModel;
import com.interview.game.service.DeckService;
import com.interview.game.service.GameService;
import com.interview.game.service.PlayerService;
import com.interview.game.service.api.GameOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {

	public static final String GAME_NOT_FOUND = "Game not found";

	private final GameService gameService;
	private final GameOperations gameOperations;
	private final PlayerService playerService;
	private final DeckService deckService;

	@PostMapping
	public ResponseEntity<GameModel> createGame() {
		GameModel game = gameService.createGame(GameModel.builder().build());
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(game);
	}

	@GetMapping("/{gameId}")
	public ResponseEntity<GameModel> getGameById(@PathVariable UUID gameId) {
		return ResponseEntity
				.ok(gameService.getGame(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_FOUND)));
	}

	@GetMapping
	public ResponseEntity<List<GameModel>> getAllGames() {
		return ResponseEntity.ok(gameService.getAllGames());
	}

	@PostMapping("/{gameId}/start")
	public ResponseEntity<GameModel> startGame(@PathVariable UUID gameId) {
		var game = gameService.getGame(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_FOUND));
		if (gameInProgress(game)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Game already started");
		}

		game.setStartTime(Instant.now());

		return ResponseEntity.ok(gameService.updateGame(game));
	}

	@PostMapping("/{gameId}/end")
	public ResponseEntity<GameModel> endGame(@PathVariable UUID gameId) {
		var game = gameService.getGame(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_FOUND));
		if (!gameInProgress(game)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Game has not started");
		}

		game.setEndTime(Instant.now()); // compute a winner if the game ends ?

		return ResponseEntity.ok(gameService.updateGame(game));
	}

	@DeleteMapping("/{gameId}")
	public ResponseEntity<Void> deleteGame(@PathVariable UUID gameId) {
		var game = gameService.getGame(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_FOUND));

		if (gameInProgress(game)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "This game has already started");
		}

		gameService.deleteGame(gameId);
		return ResponseEntity
				.noContent()
				.build();
	}


	/* --------------------------------  GAME OPERATIONS/MECHANICS endpoints  -------------------------------- */

	@PostMapping("/{gameId}/decks/{deckId}")
	public GameModel addDeckToGame(@PathVariable UUID gameId, @PathVariable UUID deckId) {
		var game = gameService.getGame(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_FOUND));
		var deck = deckService.getDeck(deckId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, DECK_NOT_FOUND));

		if (!gameIsReady(game)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot add a player to a game in progress or a completed game");
		}

		var deckIsInUse = gameService.getAllGames().stream().anyMatch(g -> g.getDecks().stream().anyMatch(d -> d.getId().equals(deck.getId())));
		if (deckIsInUse) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "The deck is already being used");
		}

		return gameOperations.addDeck(deck, game);
	}

	@PostMapping("/{gameId}/players/{playerId}")
	public GameModel addPlayerToGame(@PathVariable UUID gameId, @PathVariable UUID playerId) {
		var game = gameService.getGame(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_FOUND));
		var player = playerService.getPlayer(playerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PLAYER_NOT_FOUND));

		if (!gameIsReady(game)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot add a player to a game in progress or a completed game");
		}

		return gameOperations.addPlayer(player, game);
	}

	@DeleteMapping("/{gameId}/players/{playerId}")
	public GameModel removePlayerFromGame(@PathVariable UUID gameId, @PathVariable UUID playerId) {
		var game = gameService.getGame(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_FOUND));
		var player = playerService.getPlayer(playerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PLAYER_NOT_FOUND));

		if (!gameIsReady(game)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot remove a player from a game in progress or a completed game");
		}

		return gameOperations.removePlayer(player, game);
	}

	@PostMapping("/{gameId}/deal")
	public GameModel dealToPlayer(@PathVariable UUID gameId, @RequestParam UUID playerId, @RequestParam UUID deck) {

	}




}

