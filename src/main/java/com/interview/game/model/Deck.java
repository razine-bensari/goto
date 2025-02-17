package com.interview.game.model;

import java.util.ArrayList;
import java.util.List;
import com.interview.game.domain.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deck {

	@Id
	private String id = new ObjectId().toString();

	private List<Card> cards = new ArrayList<>();

	public Deck(List<Card> cards) {
		this.cards = cards;
		this.id = new ObjectId().toString();
	}
}
