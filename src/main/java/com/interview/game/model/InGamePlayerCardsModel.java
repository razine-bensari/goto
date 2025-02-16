package com.interview.game.model;


import java.util.List;
import com.interview.game.domain.Card;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "game_player_cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InGamePlayerCardsModel {

	@EmbeddedId
	private GamePlayerCardsId id;

	@ManyToOne
	@MapsId("gameId")
	@JoinColumn(name = "game_id", nullable = false)
	private GameModel gameModel;

	@ManyToOne
	@MapsId("playerId")
	@JoinColumn(name = "player_id", nullable = false)
	private PlayerModel playerModel;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb", nullable = false)
	private List<Card> cards;
}