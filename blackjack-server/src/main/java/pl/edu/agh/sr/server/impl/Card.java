package pl.edu.agh.sr.server.impl;

public class Card {
	private int value;
	private String label;
	
	public Card(String label, int value) {
		this.value = value;
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public int geValue() {
		return value;
	}
}
