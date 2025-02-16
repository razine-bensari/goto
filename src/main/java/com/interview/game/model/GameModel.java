package com.interview.game.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE games SET deleted = true WHERE id = ?")
public class GameModel {

	@Id
	@GeneratedValue(generator = "UUID")
	@Column(updatable = false, nullable = false)
	private UUID id;

	@ManyToMany
	@JoinTable(
			name = "game_players",
			joinColumns = @JoinColumn(name = "game_id"),
			inverseJoinColumns = @JoinColumn(name = "player_id")
	)
	private List<PlayerModel> players;

	@OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DeckModel> decks;

	@ManyToOne
	private PlayerModel winner;

	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	@Column
	private Instant updatedAt;

	@Column
	private Instant startTime;

	@Column
	private Instant endTime;

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
