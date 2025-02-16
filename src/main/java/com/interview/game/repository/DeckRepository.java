package com.interview.game.repository;

import java.util.UUID;
import com.interview.game.model.DeckModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository extends JpaRepository<DeckModel, UUID> {
}
