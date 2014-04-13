package pl.edu.agh.sr.api.impl;

import java.rmi.RemoteException;

import pl.edu.agh.sr.api.BlackjackListener;
import pl.edu.agh.sr.api.UserToken;

public class BlackjackListenerImpl implements BlackjackListener{

	public void onPlayersMove(String text)
			throws RemoteException {
		System.out.println(text);
	}

}
