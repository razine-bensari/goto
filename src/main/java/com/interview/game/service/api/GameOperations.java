package com.interview.game.service.api;


import java.util.List;
import java.util.Map;
import com.interview.game.domain.Card;
import com.interview.game.domain.Suit;
import com.interview.game.model.Deck;
import com.interview.game.model.Game;
import com.interview.game.model.Player;

/**
 * Represents all the allowed operations of the game card.
 */
public interface GameOperations {

	/**
	 * Adds a new deck of cards to the given game.
	 *
	 * @param game the game in which we add a new deck of cards.
	 * @return the updated game with the new deck of cards
	 */
	Game addDeck(Game game);

	/**
	 * Adds a new player to the given game.
	 *
	 * @param player the player to add to the game.
	 * @param game   the game in which we are adding a new player
	 * @return the update game
	 */
	Game addPlayer(Player player, Game game);

	/**
	 * Removes a player from the game.
	 *
	 * @param player The player to remove
	 * @param game   the game where the player is removed
	 */
	Game removePlayer(Player player, Game game);

	/**
	 * Deals the cards from the deck to the player of the game
	 *
	 * @param player the player that will receive the cards
	 * @param deck   the targeted deck of the game
	 * @param game   the game
	 */
	Game dealCards(Player player, Deck deck, Game game);

	/**
	 * Returns the list of players for a given game.
	 *
	 * @param game          the targeted game
	 * @param includeScores boolean indicating if we want the players to have score as well.
	 * @return a list of players in the game sorted by highest score with score populated for each player
	 */
	List<Player> getPlayers(Game game, boolean includeScores);

	/**
	 * Computes the number of cards per suit in the game deck
	 *
	 * @param game the targeted game
	 * @return a map of suit to count
	 */
	Map<Suit, Long> countDeckCardsPerSuit(Game game);

	/**
	 * Counts the number of cards in the game deck
	 *
	 * @param game the targeted game
	 * @return map of cards sorted by suit then rank with the count as value
	 */
	Map<Card, Long> countDeckCardsPerSuitAndRank(Game game);

	/**
	 * Shuffles the cards in the game deck
	 *
	 * @param game the targeted game
	 */
	void shuffle(Game game);
}
