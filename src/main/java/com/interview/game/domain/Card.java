package com.interview.game.domain;

import java.util.Comparator;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

/**
 * Represents a card in the game.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card implements Comparable<Card> {
	private Rank rank;
	private Suit suit;
	@Transient
	private int count;

	public Card(Rank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}

	@Override
	public int compareTo(Card other) {
		return Comparator
				.comparing(Card::getSuit)
				.thenComparing(Card::getRank).reversed()
				.compare(this, other);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Card otherCard = (Card) obj;
		return this.suit == otherCard.suit && this.rank == otherCard.rank;
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(rank);
		result = 31 * result + Objects.hashCode(suit);
		return result;
	}

	@Override
	public String toString() {
		return "[" + rank + " of " + suit + "]";
	}
}
