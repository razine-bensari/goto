package com.interview.game.util;

import java.util.ArrayList;
import java.util.List;
import com.interview.game.domain.Card;
import com.interview.game.domain.Rank;
import com.interview.game.domain.Suit;
import com.interview.game.model.GameModel;

/**
 * Utility class for game service
 */
public final class GameUtils
{
	private GameUtils() {
		// utility class, no instantiation
	}


	/**
	 * Generate a full set of cards from the available suits, number and aces
	 * @return a deck of cards for the given supported Suit and Rank enums.
	 */
	public static List<Card> generateAllCards() {
		List<Card> deck = new ArrayList<>();

		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				deck.add(new Card(rank, suit));
			}
		}

		return deck;
	}

	/**
	 * Determines if a game is in progress or not by using the start time and end time.
	 * @param game the game to check
	 * @return  boolean indicating if it is in progress or not
	 */
	public static boolean gameInProgress(GameModel game) {
		return (game.getStartTime() != null && game.getEndTime() == null);
	}

	/**
	 * Determines if a game has ended or not by using the end time
	 * @param game the game in question
	 * @return boolean indicating if the game ended
	 */
	public static boolean gameHasEnded(GameModel game) {
		return game.getEndTime() == null;
	}

	/**
	 * Determines if a game is ready. By definition, ready implies a had that has not started and has not ended.
	 * @param game the game in question
	 * @return a boolean indicating if the game is ready
	 */
	public static boolean gameIsReady(GameModel game) {
		return !gameInProgress(game) && !gameHasEnded(game);
	}

}
