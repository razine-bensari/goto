package com.interview.game.model;

import java.util.ArrayList;
import java.util.List;
import com.interview.game.domain.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Player {

	@Id
	private String id;

	private String username;

	@Transient
	private int score;

	private List<Card> cards = new ArrayList<>();

	public Player(String username) {
		this.username = username;
		this.cards = new ArrayList<>();
	}
}
