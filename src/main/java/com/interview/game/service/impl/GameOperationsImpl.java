package com.interview.game.service.impl;


import java.util.List;
import java.util.Map;
import com.interview.game.domain.Card;
import com.interview.game.domain.Suit;
import com.interview.game.model.DeckModel;
import com.interview.game.model.GameModel;
import com.interview.game.model.PlayerModel;
import com.interview.game.repository.GameRepository;
import com.interview.game.service.GameService;
import com.interview.game.service.api.GameOperations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameOperationsImpl implements GameOperations {

	private final GameService gameService;
	private final GameRepository gameRepository;

	@Override
	public GameModel addDeck(DeckModel deck, GameModel game) {
		var decks = game.getDecks();
		decks.add(deck);
		game.setDecks(decks);
		return gameRepository.save(game);
	}

	@Override
	public GameModel addPlayer(PlayerModel player, GameModel game) {
		var playersInGame = game.getPlayers();
		playersInGame.add(player);
		game.setPlayers(playersInGame);
		return gameService.updateGame(game);
	}

	@Override
	public GameModel removePlayer(PlayerModel player, GameModel game) {
		var playersInGame = game.getPlayers();
		playersInGame.removeIf(p -> p.getId().equals(player.getId()));
		game.setPlayers(playersInGame);
		return gameService.updateGame(game);
	}

	@Override
	public GameModel dealCards(PlayerModel player, DeckModel deck, GameModel game) {
		return null;
	}

	@Override
	public List<Card> getCards(PlayerModel player, GameModel game) {
		return List.of();
	}

	@Override
	public Map<PlayerModel, Integer> getPLayersScore(GameModel game) {
		return Map.of();
	}

	@Override
	public Map<Suit, Integer> countDeckCardsPerSuit(GameModel game) {
		return Map.of();
	}

	@Override
	public void shuffle(GameModel game) {

	}

	@Override
	public Map<Card, Integer> countDeckCards(GameModel game) {
		return Map.of();
	}
}
