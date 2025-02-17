package com.interview.game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.interview.game.domain.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Player {

	private String id = UUID.randomUUID().toString();

	private String username;

	@Transient
	private int score;

	private List<Card> cards = new ArrayList<>();

	public Player(String username) {
		this.username = username;
		this.cards = new ArrayList<>();
		this.id = UUID.randomUUID().toString();
	}
}
