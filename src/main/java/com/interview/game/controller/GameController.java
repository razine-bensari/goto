package com.interview.game.controller;

import static com.interview.game.util.GameUtils.gameHasEnded;
import static com.interview.game.util.GameUtils.gameHasStarted;
import static com.interview.game.util.GameUtils.gameInProgress;
import static com.interview.game.util.GameUtils.gameIsReady;
import static com.interview.game.util.GameUtils.generateRandomAlphanumericalStr;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.interview.game.domain.Card;
import com.interview.game.domain.Suit;
import com.interview.game.model.Game;
import com.interview.game.model.Player;
import com.interview.game.service.GameService;
import com.interview.game.service.api.GameOperations;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
	public static final String PLAYER_NOT_FOUND = "Player not found";
	public static final String DECK_NOT_FOUND = "Deck not found";

	private final GameService gameService;
	private final GameOperations gameOperations;

	@PostMapping
	public ResponseEntity<Game> createGame() {
		Game game = gameService.createGame(Game.builder().build());
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(game);
	}

	@GetMapping("/{gameId}")
	public ResponseEntity<Game> getGameById(@PathVariable String gameId) {
		return ResponseEntity
				.ok(gameService.getGame(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_FOUND)));
	}

	@GetMapping
	public ResponseEntity<List<Game>> getAllGames() {
		return ResponseEntity.ok(gameService.getAllGames());
	}

	@PostMapping("/{gameId}/start")
	public ResponseEntity<Game> startGame(@PathVariable String gameId) {
		var game = gameService.getGame(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_FOUND));
		if (gameInProgress(game)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Game already started");
		}

		if (game.getPlayers().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot start a game with no players");
		}

		if (game.getDecks().stream().flatMap(d -> d.getCards().stream()).toList().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot start a game with no cards");
		}

		game.setStartTime(Instant.now());

		return ResponseEntity.ok(gameService.updateGame(game));
	}

	@PostMapping("/{gameId}/end")
	public ResponseEntity<Game> endGame(@PathVariable String gameId) {
		var game = gameService.getGame(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_FOUND));
		if (!gameInProgress(game)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Game has not started");
		}

		game.setEndTime(Instant.now()); // compute a winner if the game ends ?

		return ResponseEntity.ok(gameService.updateGame(game));
	}

	@DeleteMapping("/{gameId}")
	public ResponseEntity<Void> deleteGame(@PathVariable String gameId) {
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

	@PostMapping("/{gameId}/decks")
	public Game addDeckToGame(@PathVariable String gameId) {
		var game = gameService.getGame(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_FOUND));

		if (!gameIsReady(game)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot add a deck to a game in progress or a completed");
		}

		return gameOperations.addDeck(game);
	}

	@PostMapping("/{gameId}/players")
	public Game addPlayerToGame(@PathVariable String gameId) {
		var game = gameService.getGame(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_FOUND));

		if (!gameIsReady(game)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot add a player to a game in progress or a completed game");
		}

		var username = generateRandomAlphanumericalStr(8);
		return gameOperations.addPlayer(new Player(username), game);
	}

	@DeleteMapping("/{gameId}/players/{playerId}")
	public Game removePlayerFromGame(@PathVariable String gameId, @PathVariable String playerId) {
		var game = gameService.getGame(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_FOUND));
		var player = game.getPlayers().stream().filter(p -> p.getId().equals(playerId)).findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PLAYER_NOT_FOUND));

		if (!gameIsReady(game)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot remove a player from a game in progress or completed");
		}

		return gameOperations.removePlayer(player, game);
	}

	@PostMapping("/{gameId}/deal-cards")
	public Game dealToPlayer(@PathVariable String gameId, @RequestParam String playerId, @RequestParam String deckId) {
		if (StringUtils.isBlank(deckId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing deckId");
		}

		if (StringUtils.isBlank(playerId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing playerId");
		}

		var game = gameService.getGame(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_FOUND));
		var player = game.getPlayers().stream().filter(p -> p.getId().equals(playerId)).findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PLAYER_NOT_FOUND));
		var deck = game.getDecks().stream().filter(d -> d.getId().equals(deckId)).findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, DECK_NOT_FOUND));

		if (!gameInProgress(game)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot deal cards to player for a game not in progress");
		}

		return gameOperations.dealCards(player, deck, game);
	}

	@GetMapping("/{gameId}/players/{playerId}/cards")
	public List<Card> getPlayerCards(@PathVariable String gameId, @PathVariable String playerId) {
		var game = gameService.getGame(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_FOUND));
		var player = game.getPlayers().stream().filter(p -> p.getId().equals(playerId)).findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PLAYER_NOT_FOUND));

		if (!gameHasStarted(game)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot list cards of a player for a game not in progress");
		}

		return player.getCards();
	}

	@GetMapping("/{gameId}/players")
	public List<Player> getPlayers(@PathVariable String gameId, @RequestParam(required = false) Optional<Boolean> includeScore) {
		var game = gameService.getGame(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_FOUND));

		if (!gameHasStarted(game)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot list players for a game not in progress");
		}

		return gameOperations.getPlayers(game, includeScore.orElse(false));
	}


	@GetMapping("/{gameId}/suit-count")
	public Map<Suit, Long> getCountPerSuit(@PathVariable String gameId) {
		var game = gameService.getGame(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_FOUND));

		if (!gameHasStarted(game)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot get the count per suit for a game not in progress");
		}

		return gameOperations.countDeckCardsPerSuit(game);
	}

	@GetMapping("/{gameId}/card-count")
	public Map<Card, Long> getCardCount(@PathVariable String gameId) {
		var game = gameService.getGame(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_FOUND));

		if (!gameHasStarted(game)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot get the count of cards for a game that has not started yet");
		}

		return gameOperations.countDeckCardsPerSuitAndRank(game);
	}

	@PostMapping("/{gameId}/shuffle")
	public ResponseEntity<Void> shuffle(@PathVariable String gameId) {
		var game = gameService.getGame(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_FOUND));

		if (gameHasEnded(game)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot shuffle a deck of a game that has ended");
		}

		gameOperations.shuffle(game);

		return ResponseEntity
				.noContent()
				.build();
	}
}

