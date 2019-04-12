package it.polimi.se2019.adrenalina;

import it.polimi.se2019.adrenalina.network.Client;
import it.polimi.se2019.adrenalina.network.ConnectionRMI;
import it.polimi.se2019.adrenalina.network.ConnectionSocket;
import it.polimi.se2019.adrenalina.network.ServerInterface;
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
    Character connectionMode = scanner.nextLine().charAt(0);

    Log.info("Please enter your name");
    String name = scanner.nextLine();

    Log.info("Please select a game mode");
    Log.info("  (1) Classic");
    Log.info("  (2) Domination");
    Character gameMode = scanner.nextLine().charAt(0);
    boolean domination = false;
    if (gameMode == 2) {
      domination = true;
    }

    Client client = null;
    try {
      client = new Client(name, domination);
    } catch (RemoteException e) {
      Log.severe("RMI", "Connection error: " + e.getMessage());
    }

    switch (connectionMode) {
      case '1':
        // RMI
        client.setConnection(new ConnectionRMI(client));
        break;
      case '2':
        // Socket
        client.setConnection(new ConnectionSocket(client));
        break;
      default:
        Log.severe("Invalid option. Abort.");
        return ;
    }

    try {
      client.connect();
    } catch (RemoteException e) {
      Log.severe("RMI", "Connection error: " + e.getMessage());
    }
  }
}
