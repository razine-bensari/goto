package com.interview.game.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.interview.game.domain.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {

	@Id
	private String id = new ObjectId().toString();

	private String username;

	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	@Transient
	private int score;

	private List<Card> cards = new ArrayList<>();

	public Player(String username) {
		this.username = username;
		this.cards = new ArrayList<>();
		this.id = new ObjectId().toString();
	}
}
