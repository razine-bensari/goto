package com.interview.game.service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.interview.game.model.DeckModel;
import com.interview.game.repository.DeckRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeckService {
	private final DeckRepository deckRepository;

	@Autowired
	public DeckService(DeckRepository deckRepository) {
		this.deckRepository = deckRepository;
	}

	public DeckModel createDeck(DeckModel deckModel) {
		log.info("Creating deck");
		return deckRepository.save(deckModel);
	}

	public Optional<DeckModel> getDeck(UUID id) {
		return deckRepository.findById(id);
	}

	public List<DeckModel> getAllDecks() {
		return deckRepository.findAll();
	}

	public DeckModel updateDeck(DeckModel deckModel) {
		log.info("Updating deck with id {}", deckModel.getId());
		return deckRepository.save(deckModel);
	}

	public void deleteDeck(UUID id) {
		log.info("Deleting deck with id {}", id);
		deckRepository.deleteById(id);
	}
}
