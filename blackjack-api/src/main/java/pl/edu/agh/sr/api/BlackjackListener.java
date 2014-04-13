package pl.edu.agh.sr.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BlackjackListener extends Remote {
	void onPlayersMove(String text) throws RemoteException;
}