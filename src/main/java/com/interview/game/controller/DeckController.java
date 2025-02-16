package com.interview.game.controller;

import static com.interview.game.util.GameUtils.gameHasEnded;
import static com.interview.game.util.GameUtils.gameInProgress;
import static com.interview.game.util.GameUtils.generateAllCards;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import com.interview.game.dto.UpdateDeckRequest;
import com.interview.game.model.DeckModel;
import com.interview.game.service.DeckService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1/decks")
@RequiredArgsConstructor
public class DeckController {
	public static final String DECK_NOT_FOUND = "Deck not found";

	private final DeckService deckService;

	@PostMapping
	public ResponseEntity<DeckModel> createDeck() {
		DeckModel deck = deckService.createDeck(DeckModel.builder()
				.cards(generateAllCards())
				.build());
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(deck);
	}

	@GetMapping("/{deckId}")
	public ResponseEntity<DeckModel> getDeck(@PathVariable UUID deckId) {
		return ResponseEntity
				.ok(deckService.getDeck(deckId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, DECK_NOT_FOUND)));
	}

	@GetMapping
	public ResponseEntity<List<DeckModel>> getAllDecks() {
		return ResponseEntity.ok(deckService.getAllDecks());
	}

	@PutMapping("/{deckId}")
	public ResponseEntity<DeckModel> updateDeck(@PathVariable UUID deckId, @RequestBody UpdateDeckRequest request) {
		var deck = deckService.getDeck(deckId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, DECK_NOT_FOUND));

		// throw exception if list of cards in invalid (contains duplicates, more than 52 cards)
		if (!new HashSet<>(generateAllCards()).containsAll(request.getCards())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid list of cards.");
		}

		deck.setCards(request.getCards());

		return ResponseEntity.ok(deckService.updateDeck(deck));
	}

	@DeleteMapping("/{deckId}")
	public ResponseEntity<Void> deleteDeck(@PathVariable UUID deckId) {
		var deck = deckService.getDeck(deckId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, DECK_NOT_FOUND));
		var game = deck.getGame();
		if (game != null) {
			if (gameInProgress(game)) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete the deck because it is being used by a game in progress.");
			}

			if (gameHasEnded(game)) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete the deck because the game has ended.");
			}
		}

		deckService.deleteDeck(deckId);
		return ResponseEntity
				.noContent()
				.build();
	}
}
