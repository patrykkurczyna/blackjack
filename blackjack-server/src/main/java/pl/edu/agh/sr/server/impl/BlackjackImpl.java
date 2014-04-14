package pl.edu.agh.sr.server.impl;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.sr.api.Blackjack;
import pl.edu.agh.sr.api.BlackjackException;
import pl.edu.agh.sr.api.BlackjackListener;
import pl.edu.agh.sr.api.UserToken;

public class BlackjackImpl implements Blackjack {

	private Table table;
	private static Integer counter = 1;
	private static Integer gameIsOn = 0; // 0 - off, 1 - waiting for opponent, 2
											// - on, 3 - player finishes move
	private static Map<UserToken, BlackjackListener> registeredClients;
	private static UserToken playerOnTurn;
	private static String winner = null;
	private static boolean playerOneChoiceIsNo = false;
	private static boolean opponentChoiceIsNo = false;

	public BlackjackImpl() {
		registeredClients = new HashMap<UserToken, BlackjackListener>();
		this.table = new Table();
	}

	public BlackjackListener getListenerByToken(UserToken ut) {
		BlackjackListener bl = null;
		for (Map.Entry<UserToken, BlackjackListener> registeredClient : registeredClients
				.entrySet()) {
			if (ut.equals(registeredClient.getKey()))
				bl = registeredClient.getValue();
		}
		return bl;
	}

	public void notifyAllClients(String text) throws BlackjackException,
			RemoteException {
		System.out.println(text);
		for (Map.Entry<UserToken, BlackjackListener> registeredClient : registeredClients
				.entrySet()) {
			registeredClient.getValue().onPlayersMove(text);
		}
	}

	public synchronized String getTableState(UserToken userToken)
			throws RemoteException, BlackjackException {
		if (!registeredClients.containsKey(userToken)) {
			throw new BlackjackException();
		} else {
			return this.table.toString();
		}
	}

	public synchronized void makeChoice(UserToken userToken, boolean choice)
			throws RemoteException, BlackjackException {
		if (!registeredClients.containsKey(userToken)) {
			throw new BlackjackException();
		} else if (!userToken.equals(playerOnTurn)) {
			getListenerByToken(userToken).onPlayersMove(
					"It's not your turn, please wait!");
		} else {
			String choiceString;
			Card pickedCard = null;
			if (choice) {
				pickedCard = this.table.pickCard(userToken);
				choiceString = "YES";
				if (userToken.equals(this.table.getPlayer().getUserToken())) {
					playerOneChoiceIsNo = false;
				} else if (userToken.equals(this.table.getOpponent()
						.getUserToken())) {
					opponentChoiceIsNo = false;
				}
			} else {
				choiceString = "NO";
				if (userToken.equals(this.table.getPlayer().getUserToken())) {
					playerOneChoiceIsNo = true;
				} else if (userToken.equals(this.table.getOpponent()
						.getUserToken())) {
					opponentChoiceIsNo = true;
				}
			}
			String saying = "Player with name --" + userToken.getUserName()
					+ "-- says: " + choiceString + "\n";
			if (pickedCard != null)
				saying += ("and gets: " + pickedCard.getLabel() + "\n");
			saying += "Current table state: \n" + this.table.toString();
			notifyAllClients(saying);
			gameIsOn = 3;
		}
	}

	public synchronized UserToken register(String nick,
			BlackjackListener listener) throws RemoteException,
			BlackjackException {
		UserToken token = new UserTokenImpl(nick, counter);
		counter++;
		if (registeredClients.containsKey(token)) {
			throw new BlackjackException();
		} else {
			registeredClients.put(token, listener);
			System.out.println("New client registered: " + token);
		}
		return token;
	}

	public synchronized void unregister(UserToken token)
			throws RemoteException, BlackjackException {
		if (registeredClients.containsKey(token)) {
			throw new BlackjackException();
		} else {
			registeredClients.remove(token);
		}
	}

	public synchronized void enterGame(UserToken token) throws RemoteException,
			BlackjackException {
		if (!registeredClients.containsKey(token)) {
			throw new BlackjackException();
		} else {
			if (this.table.addPlayer(token)) {
				gameIsOn = 1;
				System.out.println("Client: " + token.getUserName()
						+ " enters the game!");
				notifyAllClients("Player with name --" + token.getUserName()
						+ "-- enters the game!\n" + "Current table state: \n"
						+ this.table.toString());
			} else {
				System.out.println("Table is full, cannot join!");
			}
		}
	}
	
	//Server method for main game
	public void game() throws RemoteException, BlackjackException,
			InterruptedException {
		while (this.table.getPlayer() == null
				|| this.table.getOpponent() == null) {
			Thread.sleep(8000);
			System.out.println("Waiting for the game to start...");
			if (this.table.getPlayer() == null
					&& this.table.getOpponent() != null) {
				getListenerByToken(this.table.getOpponent().getUserToken())
						.onPlayersMove("It's not your turn, please wait!");
			} else if (this.table.getPlayer() != null) {
				getListenerByToken(this.table.getPlayer().getUserToken())
						.onPlayersMove("It's not your turn, please wait!");
			}
		}
		startGame();
		while (gameIsOn == 2) {
			// Player One move
			getListenerByToken(this.table.getPlayer().getUserToken())
					.onPlayersMove(
							"It's your turn, do you want another card? (y - YES, n - NO, s - show current table)");

			while (gameIsOn == 2) {
				Thread.sleep(5000);
			}
			gameIsOn = 2;

			playerOnTurn = this.table.getOpponent().getUserToken();
			// Opponent move
			getListenerByToken(this.table.getOpponent().getUserToken())
					.onPlayersMove(
							"It's your turn, do you want another card? (y - YES, n - NO, s - show current table)");

			while (gameIsOn == 2) {
				Thread.sleep(5000);
			}
			gameIsOn = 2;

			playerOnTurn = this.table.getPlayer().getUserToken();

			if (this.table.getPlayer().lost()) {
				winner = this.table.getOpponent().getName();
				stopGame();
			} else if (this.table.getOpponent().lost()) {
				winner = this.table.getPlayer().getName();
				stopGame();
			} else if (playerOneChoiceIsNo && opponentChoiceIsNo) {
				winner = (this.table.getPlayer().getScore() > this.table
						.getOpponent().getScore() ? this.table.getPlayer()
						.getName() : this.table.getOpponent().getName());
				if (this.table.getPlayer().getScore() == this.table
						.getOpponent().getScore())
					winner = "DRAW";
				stopGame();
			}
		}
	}

	public void startGame() throws RemoteException, BlackjackException {
		if (gameIsOn == 2) {
			throw new BlackjackException();
		} else {
			gameIsOn = 2;
			playerOnTurn = this.table.getPlayer().getUserToken();
			notifyAllClients("Ladies and gentlemen THE GAME IS ON!");
			notifyAllClients("Current table state: \n" + this.table.toString());
		}
	}

	public void stopGame() throws RemoteException, BlackjackException {
		if (gameIsOn != 2) {
			throw new BlackjackException();
		} else {
			gameIsOn = 0;
			playerOnTurn = null;
			notifyAllClients("Ladies and gentlemen THE GAME HAS ENDED!");
			notifyAllClients("Current table state: \n" + this.table.toString());
			notifyAllClients("The winners is: " + winner);
			this.table = new Table();
			// exit game
		}
	}

	public synchronized int getGameStatus() throws RemoteException,
			BlackjackException {
		return gameIsOn;
	}

}