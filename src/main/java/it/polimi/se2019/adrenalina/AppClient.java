package it.polimi.se2019.adrenalina;

import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.network.ClientRMI;
import it.polimi.se2019.adrenalina.network.ClientSocket;
import it.polimi.se2019.adrenalina.ui.text.TUIInputManager;
import it.polimi.se2019.adrenalina.utils.Log;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class AppClient {

  public AppClient(String... args) {
    Log.setName("ClientRMI");

    String name;
    Integer connectionMode;
    boolean domination = false;

    if (args.length < 3) {
      TUIInputManager inputManager = new TUIInputManager();
      inputManager.input("Please select a connection mode:", new ArrayList<>(Arrays.asList("RMI", "Socket")));
      try {
        connectionMode = inputManager.waitForIntResult();
      } catch (InterruptedException e) {
        connectionMode = null;
        Log.severe("Interrupted Exception");
      }

      inputManager.input("Please enter your name");
      try {
        name = inputManager.waitForStringResult();
      } catch (InterruptedException e) {
        name = null;
        Log.severe("Interrupted Exception");
      }

      inputManager.input("Please select a game mode:", new ArrayList<>(Arrays.asList("Classic", "Domination")));
      int gameMode = 0;
      try {
        gameMode = inputManager.waitForIntResult();
      } catch (InterruptedException e) {
        gameMode = -1;
        Log.severe("Interrupted Exception");
      }

      if (gameMode == 1) {
        domination = true;
      }
    } else {
      name = args[0];
      connectionMode = Integer.parseInt(args[1]);
      if (args[2].charAt(0) == '0') {
        domination = false;
      } else if (args[2].charAt(0) == '1') {
        domination = true;
      } else {
        Log.severe("Invalid game mode. Supported: (0) Classic; (1) Domination");
        return;
      }
    }

    ClientInterface client = null;

    switch (connectionMode) {
      case 0:
        // RMI
        try {
          ClientRMI clientRMI = new ClientRMI(name, domination);
          client = (ClientInterface) UnicastRemoteObject.exportObject(clientRMI, 0);
          clientRMI.getServer().addClient(client);
        } catch (RemoteException e) {
          Log.exception(e);
        } catch (InvalidPlayerException ignored) {
          //
        }
        break;
      case 1:
        // Socket
        client = new ClientSocket(name, domination);
        break;
      default:
        Log.severe("Invalid connection mode. Supported: (0) RMI; (1) Socket");
        return;
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
