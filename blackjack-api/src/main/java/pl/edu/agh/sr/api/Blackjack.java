package pl.edu.agh.sr.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Blackjack extends Remote {
	public void makeChoice(UserToken userToken, boolean choice)
			throws RemoteException, BlackjackException;

	public void enterGame(UserToken token)
			throws RemoteException, BlackjackException;
	
	public String getTableState(UserToken token)
			throws RemoteException, BlackjackException;	
	
//	public void exitGame(UserToken userToken) 
//			throws RemoteException, BlackjackException;
	
	public void unregister(UserToken userToken) 
			throws RemoteException, BlackjackException;
	
	public UserToken register(String nick, BlackjackListener listener) 
			throws RemoteException, BlackjackException;
	
	public void game() throws RemoteException, BlackjackException, InterruptedException;
	
	public int getGameStatus() throws RemoteException, BlackjackException;
}
