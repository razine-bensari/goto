package com.interview.game.model;

import java.io.Serializable;
import java.util.UUID;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Represents the composite key of the InGamePlayerCardsModel
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GamePlayerCardsId implements Serializable {
	private UUID gameId;
	private UUID playerId;
}

