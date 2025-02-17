package com.interview.game.service.impl;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import com.interview.game.domain.Card;
import com.interview.game.domain.Suit;
import com.interview.game.model.Deck;
import com.interview.game.model.Game;
import com.interview.game.model.Player;
import com.interview.game.repository.GameRepository;
import com.interview.game.service.GameService;
import com.interview.game.service.api.GameOperations;
import com.interview.game.util.GameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameOperationsImpl implements GameOperations {

	private final GameService gameService;
	private final GameRepository gameRepository;

	@Override
	public Game addDeck(Game game) {
		var deck = new Deck(GameUtils.generateAllCards());
		var decks = game.getDecks();
		decks.add(deck);
		game.setDecks(decks);
		return gameRepository.save(game);
	}

	@Override
	public Game addPlayer(Player player, Game game) {
		var playersInGame = game.getPlayers();
		playersInGame.add(player);
		game.setPlayers(playersInGame);
		return gameService.updateGame(game);
	}

	@Override
	public Game removePlayer(Player player, Game game) {
		var playersInGame = game.getPlayers();
		playersInGame.removeIf(p -> p.getId().equals(player.getId()));
		game.setPlayers(playersInGame);
		return gameService.updateGame(game);
	}

	@Override
	public Game dealCards(Player player, Game game) {

		// loop using index to keep order
		for (int i = 0; i < game.getDecks().size(); i++) {

			// if a deck has no card, skip the loop to select the next deck of cards
			if (game.getDecks().get(i).getCards().isEmpty()) {
				continue;
			}

			// 'pop' the card from the deck, add the card to the 'player's hand', persist the new game state
			// optional helps with noop as per requirements
			// break from the loop
			var playerCards = player.getCards();
			var cardOptional = popCardFromDeck(game.getDecks().get(i));
			if (cardOptional.isPresent()) {
				playerCards.add(cardOptional.get());
				player.setCards(playerCards);
				game = gameService.updateGame(game);
			}
			break;
		}
		return game;
	}

	@Override
	public List<Player> getPlayers(Game game, boolean includeScores) {
		game.getPlayers()
				.forEach(player -> {
					if (includeScores) {
						var cards = player.getCards();
						if (cards.isEmpty()) {
							player.setScore(0);
						} else {
							int totalScore = cards.stream().mapToInt(c -> c.getRank().getValue()).sum();
							player.setScore(totalScore);
						}
					}
				});

		return game.getPlayers().stream().sorted(Comparator.comparing(Player::getScore).reversed()).toList();
	}

	@Override
	public Map<Suit, Long> countDeckCardsPerSuit(Game game) {
		return game.getDecks()
				.stream()
				.flatMap(deck -> deck.getCards().stream())
				.collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()));
	}

	@Override
	public Map<Card, Long> countDeckCardsPerSuitAndRank(Game game) {
		var unsortedMap = game.getDecks()
				.stream()
				.flatMap(deck -> deck.getCards().stream())
				.collect(Collectors.groupingBy(card -> card, Collectors.counting()));

		return new TreeMap<>(unsortedMap); // sorts the map using the keys' compareTo method. which follows the requirements of Suit first, then descending by rank (high to low)
	}

	@Override
	public void shuffle(Game game) {
		game.getDecks().forEach(deck -> {
			List<Card> cards = deck.getCards();
			int size = cards.size();

			if (size <= 1) {
				return; // No need to shuffle if there's 0 or 1 card.
			}

			// Here I used google and AI to determine the best way to shuffle. (it was allowed in the description of take home via email)
			// I came with the Fisher-Yates shuffle.
			Random random = ThreadLocalRandom.current(); // just in case this method is called multiple times from multiple threads (since springboot is thread scoped for http requests)
			for (int i = size - 1; i > 0; i--) {
				int j = random.nextInt(i + 1);
				Collections.swap(cards, i, j);
			}
		});

		gameService.updateGame(game);
	}

	private Optional<Card> popCardFromDeck(Deck deck) {
		var deckCards = deck.getCards();
		if (deckCards.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(deckCards.removeLast());
	}
}
