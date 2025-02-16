package com.interview.game.dto;

import java.util.List;
import com.interview.game.domain.Card;
import lombok.Getter;

@Getter
public class UpdateDeckRequest {
	private List<Card> cards;
}
