package com.interview.game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.interview.game.domain.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Deck {

	private String id = UUID.randomUUID().toString();

	private List<Card> cards = new ArrayList<>();

	public Deck(List<Card> cards) {
		this.cards = cards;
		this.id = UUID.randomUUID().toString();
	}
}
