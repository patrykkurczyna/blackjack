package pl.edu.agh.sr.client;
 
import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
 
import org.apache.log4j.Logger;
 
import pl.edu.agh.sr.api.Blackjack;
import pl.edu.agh.sr.api.UserToken;
import pl.edu.agh.sr.api.impl.BlackjackListenerImpl;
 
 
public class BlackjackClient {
 
        private static final Logger logger = Logger.getLogger(BlackjackClient.class);
       
        // TODO: Te ustawienia powinny byc konfigurowalne w bardziej
        // elegancki sposob -> argumenty programu, moze properties?
        private static final String BLACKJACK_REMOTE_OBJECT_NAME = "blackjack";
 
        public static void main(String[] args) {
 
                try {
                        if (System.getSecurityManager() == null) {
                                System.setSecurityManager(new SecurityManager());
                        }
                        
                        String RMI_REGISTRY_PORT = args[1];
                       
                        String RMI_REGISTRY_ADDRESS = "rmi://" + args[0] + ":" + args[1];
                        
                        int rmiRegistryPort = Integer.parseInt(RMI_REGISTRY_PORT);
                        
                        System.out.println(RMI_REGISTRY_PORT + " " + rmiRegistryPort + " " +RMI_REGISTRY_ADDRESS);
                        
                        // 1. Odszukujemy referencje do obiektu zdalnego - odpytujemy rejestr pod wskazanym adresem
                        Blackjack blackjack = (Blackjack) Naming
                                        .lookup(RMI_REGISTRY_ADDRESS + "/" + BLACKJACK_REMOTE_OBJECT_NAME);
                        logger.debug("Mam referencje do obiektu zdalnego od serwera!");
                       
                        BlackjackListenerImpl listener = new BlackjackListenerImpl();
                        UnicastRemoteObject.exportObject(listener, 0);
                       
                        String nick = System.getProperty("username");
                        // 2. Wolamy metody zdalne
                        UserToken userToken = blackjack.register(nick, listener);
                        blackjack.enterGame(userToken);
                       
                        Scanner sc = new Scanner(System.in);
                       String input = " ";
                        
                        //Move loop
                        while (blackjack.getGameStatus() > 0) {
                                input = sc.nextLine();
                                switch (input){
                                case "y":
                                        blackjack.makeChoice(userToken, true);
                                        break;
                                case "n":
                                        blackjack.makeChoice(userToken, false);
                                        break;
                                case "s":
                                		blackjack.getTableState(userToken);
                                		break;
                                case "exit":
                                		//blackjack.exitGame(userToken);
                                		break;
                                default:
                                       
                                }
                        }
                        sc.close();

                        blackjack.unregister(userToken);
                } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(-1);
                }
 
                System.exit(0);
        }
 
}
 
;