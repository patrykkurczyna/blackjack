package pl.edu.agh.sr.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;

import pl.edu.agh.sr.api.Blackjack;
import pl.edu.agh.sr.server.impl.BlackjackImpl;


public class BlackjackServer {

	private static final Logger logger = Logger.getLogger(BlackjackServer.class);
	
	// TODO: Te ustawienia powinny byc konfigurowalne w bardziej
	// elegancki sposob -> argumenty programu, moze properties?
	
/*	private static final String HOSTNAME = System.getProperty("java.rmi.server.hostname");
	private static final int PORT = Integer.parseInt(System.getProperty("java.rmi.server.port"));
	private static final String RMI_REGISTRY_ADDRESS = "rmi://" + HOSTNAME + ":" + PORT;
	private static final String BLACKJACK_REMOTE_OBJECT_NAME = "blackjack";*/
	
	private static final String RMI_REGISTRY_ADDRESS = "rmi://127.0.0.1:1099";
	private static final String BLACKJACK_REMOTE_OBJECT_NAME = "blackjack";

	public static void main(String[] args) {

		try {
			logger.debug(RMI_REGISTRY_ADDRESS);
			logger.debug("K\u2661");
			// 1. Stworzmy nasz obiekt zdalny
			BlackjackImpl impl = new BlackjackImpl();

			// 2. Wyeksportuj obiekt zdalny, konsekwencje:

			Blackjack blackjack = (Blackjack) UnicastRemoteObject.exportObject(impl, 0);

			// 3a. Stworzmy rejestr RMI w procesie serwera, konsekwencje:
			// - rejestr ma ten sam classpath co serwer ("widzi" te same klasy)
			// - 
			//Registry createRegistry = LocateRegistry.getRegistry(HOSTNAME, PORT);
			Registry createRegistry = LocateRegistry.createRegistry(1099);
			
			// 3b. Rejestrujemy zatem referencje w rejestrze o podanym adresie:
			// "rmi://127.0.0.1:1099/noteboard"
			Naming.rebind(RMI_REGISTRY_ADDRESS + "/" + BLACKJACK_REMOTE_OBJECT_NAME, blackjack);
			
						
			// 5. Od tej chwili kliencie moga odnalezc referencje na obiekt w
			// rejestrze i zaczac go uzywac
			logger.debug("Stół do Blackjacka otwarty!");
			
			blackjack.game();
			
		} catch (Exception e) {
			logger.error(e);
			System.exit(-1);
		}
	}
}
