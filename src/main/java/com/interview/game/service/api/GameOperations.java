package com.interview.game.service.api;


import java.util.List;
import java.util.Map;
import com.interview.game.domain.Card;
import com.interview.game.domain.Suit;
import com.interview.game.model.DeckModel;
import com.interview.game.model.GameModel;
import com.interview.game.model.PlayerModel;

/**
 * Represents all the allowed operations of the game card.
 */
public interface GameOperations {

	/**
	 * Adds a new deck of cards to the given game.
	 *
	 * @param game the game in which we add a new deck of cards.
	 * @param deck the deck to add to the game
	 * @return the updated game with the new deck of cards
	 */
	GameModel addDeck(DeckModel deck, GameModel game);

	/**
	 * Adds a new player to the given game.
	 *
	 * @param player the player to add to the game.
	 * @param game   the game in which we are adding a new player
	 * @return the update game
	 */
	GameModel addPlayer(PlayerModel player, GameModel game);

	/**
	 * Removes a player from the game.
	 *
	 * @param player The player to remove
	 * @param game   the game where the player is removed
	 */
	GameModel removePlayer(PlayerModel player, GameModel game);

	/**
	 * Deals the cards from the deck to the player of the game
	 *
	 * @param player the player that will receive the cards
	 * @param deck   the targeted deck of the game
	 * @param game   the game
	 */
	GameModel dealCards(PlayerModel player, DeckModel deck, GameModel game);

	/**
	 * Lists all the cards of a player in the game
	 *
	 * @param player the player in which we want to get the cards from
	 * @param game   the targeted game
	 * @return the list of cards
	 */
	List<Card> getCards(PlayerModel player, GameModel game);

	/**
	 * Computes the score of each player in the game
	 *
	 * @param game the targeted game
	 * @return a map of player to score
	 */
	Map<PlayerModel, Integer> getPLayersScore(GameModel game);

	/**
	 * Computes the number of cards per suit in the game deck
	 *
	 * @param game the targeted game
	 * @return a map of suit to count
	 */
	Map<Suit, Integer> countDeckCardsPerSuit(GameModel game);

	/**
	 * Shuffles the cards in the game deck
	 *
	 * @param game the targeted game
	 */
	void shuffle(GameModel game);

	/**
	 * Counts the number of cards in the game deck
	 *
	 * @param game the targeted game
	 * @return map of cards to count
	 */
	Map<Card, Integer> countDeckCards(GameModel game);
}
