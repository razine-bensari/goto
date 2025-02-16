package com.interview.game.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import com.interview.game.domain.Card;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "decks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE decks SET deleted = true WHERE id = ?")
public class DeckModel {

	@Id
	@GeneratedValue(generator = "UUID")
	@Column(updatable = false, nullable = false)
	private UUID id;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private List<Card> cards;

	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	@Column
	private Instant updatedAt;

	@Column(nullable = false)
	private boolean deleted = false;

	@Version
	private int version;

	@PrePersist
	protected void onCreate() {
		createdAt = (createdAt == null) ? Instant.now() : createdAt;
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = Instant.now();
	}
}
