package com.interview.game.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import com.interview.game.domain.Card;
import com.interview.game.domain.Rank;
import com.interview.game.domain.Suit;
import com.interview.game.model.Game;

/**
 * Utility class for game service
 */
public final class GameUtils {
	private GameUtils() {
		// utility class, no instantiation
	}

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final SecureRandom RANDOM = new SecureRandom();


	/**
	 * Generate a full set of cards from the available suits, number and aces
	 *
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
	 * Generate a random alphanumerical string
	 *
	 * @param length min length required
	 * @return the string
	 */
	public static String generateRandomAlphanumericalStr(int length) {
		if (length < 1) {
			throw new IllegalArgumentException("length must be greater than 0");
		}

		StringBuilder username = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int randomIndex = RANDOM.nextInt(CHARACTERS.length());
			username.append(CHARACTERS.charAt(randomIndex));
		}

		return username.toString();
	}

	/**
	 * Determines if a game is in progress or not by using the start time and end time.
	 *
	 * @param game the game to check
	 * @return boolean indicating if it is in progress or not
	 */
	public static boolean gameInProgress(Game game) {
		return (gameHasStarted(game) && game.getEndTime() == null);
	}

	/**
	 * Determines if a game has started or not by using the start time.
	 *
	 * @param game the game to check
	 * @return boolean indicating if the game has started. Regardless if it has finished or not
	 */
	public static boolean gameHasStarted(Game game) {
		return game.getStartTime() != null;
	}

	/**
	 * Determines if a game has ended or not by using the end time
	 *
	 * @param game the game in question
	 * @return boolean indicating if the game ended
	 */
	public static boolean gameHasEnded(Game game) {
		return game.getEndTime() == null;
	}

	/**
	 * Determines if a game is ready. By definition, ready implies a had that has not started and has not ended.
	 *
	 * @param game the game in question
	 * @return a boolean indicating if the game is ready
	 */
	public static boolean gameIsReady(Game game) {
		return !gameInProgress(game) && !gameHasEnded(game);
	}

}
