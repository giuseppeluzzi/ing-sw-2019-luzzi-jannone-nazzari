package it.polimi.se2019.adrenalina;

import it.polimi.se2019.adrenalina.controller.Configuration;
import it.polimi.se2019.adrenalina.controller.event.WeaponUpdateEvent;
import it.polimi.se2019.adrenalina.network.Client;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.network.ClientSocket;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import it.polimi.se2019.adrenalina.view.CharactersView;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsView;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class AppClient {
  public static void main(String... args) {
    Log.setName("ClientRMI");

    // Verify if the configuration exists
    Configuration.getInstance();

    String name;
    char connectionMode;
    boolean domination = false;

    if (args.length < 3) {
      Scanner scanner = new Scanner(System.in, "utf-8");
      Log.info("Please select a connection mode");
      Log.info("  (1) RMI");
      Log.info("  (2) Socket");
      Log.info("");
      connectionMode = scanner.nextLine().charAt(0);

      Log.info("Please enter your name");
      name = scanner.nextLine();

      Log.info("Please select a game mode");
      Log.info("  (1) Classic");
      Log.info("  (2) Domination");
      char gameMode = scanner.nextLine().charAt(0);

      if (gameMode == '2') {
        domination = true;
      }
    } else {
      name = args[0];
      connectionMode = args[1].charAt(0);
      if (connectionMode != '1' && connectionMode != '2') {
        Log.severe("Invalid connection mode. Supported: (1) RMI; (2) Socket");
        return;
      }
      if (args[2].charAt(0) == '1') {
        domination = false;
      } else if (args[2].charAt(0) == '2') {
        domination = true;
      } else {
        Log.severe("Invalid game mode. Supported: (1) Classic; (2) Domination");
        return ;
      }
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
        break;
      default:
        Log.severe("Invalid option. Abort.");
        return ;
    }

    if (client != null) {
      try {
        client.ping();
      } catch (IOException e) {
        Log.exception(e);
      }

    }
  }
}
