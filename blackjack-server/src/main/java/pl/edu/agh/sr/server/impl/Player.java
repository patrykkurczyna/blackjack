package pl.edu.agh.sr.server.impl;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.sr.api.UserToken;

public class Player {
	private UserToken userToken;
	private int score;
	private List<Card> cards;
	
	public Player(UserToken userToken) {
		this.score = 0;
		this.userToken = userToken;
		this.cards = new ArrayList<Card>();
	}
	
	public void addCard(Card card) {
		this.cards.add(card);
		this.score += card.geValue();
	}
	
	public void reset() {
		this.cards = new ArrayList<Card>();
		this.score = 0;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public String getName() {
		return this.userToken.getUserName();
	}
	
	public UserToken getUserToken() {
		return this.userToken;
	}
	
	public boolean lost() {
		if (this.score > 22) return true;
		else if (this.score == 22 && this.cards.size() != 2) return true;
		else return false;
	}
	
	public String toString() {
		String res = "[" + this.getName() + "]: " + this.score + "\n";
		for (Card c : cards) {
			res += c.getLabel();
			res += " ";
		}
		return res;
	}
}