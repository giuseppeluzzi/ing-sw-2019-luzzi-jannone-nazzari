package it.polimi.se2019.adrenalina;

import it.polimi.se2019.adrenalina.network.Client;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.network.ClientSocket;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.Scanner;

public class AppClient {
  public static void main(String... args) {
    Log.setName("ClientRMI");

    Scanner scanner = new Scanner(System.in, "utf-8");
    Log.info("Please select a connection mode");
    Log.info("  (1) RMI");
    Log.info("  (2) Socket");
    Log.info("");
    char connectionMode = scanner.nextLine().charAt(0);

    Log.info("Please enter your name");
    String name = scanner.nextLine();

    Log.info("Please select a game mode");
    Log.info("  (1) Classic");
    Log.info("  (2) Domination");
    char gameMode = scanner.nextLine().charAt(0);

    boolean domination = false;
    if (gameMode == '2') {
      domination = true;
    }

    ClientInterface client = null;

    switch (connectionMode) {
      case '1':
        // RMI
        try {
          client = new Client(name, domination);
        } catch (RemoteException e) {
          Log.exception(e);
        }
        break;
      case '2':
        // Socket
        client = new ClientSocket(name, domination);
        ((Runnable) client).run();
        break;
      default:
        Log.severe("Invalid option. Abort.");
        return ;
    }

    if (client != null) {
      try {
        client.ping();
      } catch (RemoteException e) {
        Log.exception(e);
      }
    }
  }
}
