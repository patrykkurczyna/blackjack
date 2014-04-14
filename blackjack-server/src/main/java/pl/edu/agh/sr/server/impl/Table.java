package pl.edu.agh.sr.server.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.edu.agh.sr.api.BlackjackException;
import pl.edu.agh.sr.api.UserToken;

public class Table {
	
	private Random randomizer = new Random();
	
	private Player player;
	private Player opponent;
	private List<Card> deck; 
	
	public Table(Player player, Player opponent) {
		this.player = player;
		this.opponent = opponent;
		
		this.deck = new ArrayList<Card>();
		
		deck.add(new Card("A\u2660", 11));
		deck.add(new Card("A\u2661", 11));
		deck.add(new Card("A\u2662", 11));
		deck.add(new Card("A\u2663", 11));
		
		deck.add(new Card("K\u2660", 10));
		deck.add(new Card("K\u2661", 10));
		deck.add(new Card("K\u2662", 10));
		deck.add(new Card("K\u2663", 10));
	
		deck.add(new Card("Q\u2660", 10));
		deck.add(new Card("Q\u2661", 10));
		deck.add(new Card("Q\u2662", 10));
		deck.add(new Card("Q\u2663", 10));
		
		deck.add(new Card("J\u2660", 10));
		deck.add(new Card("J\u2661", 10));
		deck.add(new Card("J\u2662", 10));
		deck.add(new Card("J\u2663", 10));
		
		deck.add(new Card("10\u2660", 10));
		deck.add(new Card("10\u2661", 10));
		deck.add(new Card("10\u2662", 10));
		deck.add(new Card("10\u2663", 10));
		
		deck.add(new Card("9\u2660", 9));
		deck.add(new Card("9\u2661", 9));
		deck.add(new Card("9\u2662", 9));
		deck.add(new Card("9\u2663", 9));
		
		deck.add(new Card("8\u2660", 8));
		deck.add(new Card("8\u2661", 8));
		deck.add(new Card("8\u2662", 8));
		deck.add(new Card("8\u2663", 8));
		
		deck.add(new Card("7\u2660", 7));
		deck.add(new Card("7\u2661", 7));
		deck.add(new Card("7\u2662", 7));
		deck.add(new Card("7\u2663", 7));
		
		deck.add(new Card("6\u2660", 6));
		deck.add(new Card("6\u2661", 6));
		deck.add(new Card("6\u2662", 6));
		deck.add(new Card("6\u2663", 6));
		
		deck.add(new Card("5\u2660", 5));
		deck.add(new Card("5\u2661", 5));
		deck.add(new Card("5\u2662", 5));
		deck.add(new Card("5\u2663", 5));
		
		deck.add(new Card("4\u2660", 4));
		deck.add(new Card("4\u2661", 4));
		deck.add(new Card("4\u2662", 4));
		deck.add(new Card("4\u2663", 4));
		
		deck.add(new Card("3\u2660", 3));
		deck.add(new Card("3\u2661", 3));
		deck.add(new Card("3\u2662", 3));
		deck.add(new Card("3\u2663", 3));
		
		deck.add(new Card("2\u2660", 2));
		deck.add(new Card("2\u2661", 2));
		deck.add(new Card("2\u2662", 2));
		deck.add(new Card("2\u2663", 2));	
	}
	
	public Table() {
		this(null, null);
	}
	
	// adding player to game
	public boolean addPlayer(UserToken ut){
		if (this.player != null && this.opponent != null) {
			return false;
		} else {
			if (this.player == null) {
				this.player = new Player(ut);
			} else {
				this.opponent = new Player(ut);				
			}
			return true;			
		}

	}
	
	// picks card
	public Card pickCard(UserToken ut) throws BlackjackException{
		Player tmpPlayer;
		if (ut.equals(this.player.getUserToken())) {
			tmpPlayer = this.player;
		}
		else if (ut.equals(this.opponent.getUserToken())){
			tmpPlayer = this.opponent;
		} else {
			throw new BlackjackException();
		}
		Card card = deck.remove(randomizer.nextInt(deck.size()));
		tmpPlayer.addCard(card);
		return card;
	}
	
	public String toString() {
		String pString = (player == null ? "" : player.toString());
		String oString = (opponent == null ? "" : opponent.toString());
		String res = pString + "\n" + oString;
		return res;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Player getOpponent() {
		return opponent;
	}
}
