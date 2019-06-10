package it.polimi.se2019.adrenalina;

import it.polimi.se2019.adrenalina.exceptions.InputCancelledException;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.network.ClientRMI;
import it.polimi.se2019.adrenalina.network.ClientSocket;
import it.polimi.se2019.adrenalina.ui.text.TUIInputManager;
import it.polimi.se2019.adrenalina.utils.Constants;
import it.polimi.se2019.adrenalina.utils.Log;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

public class AppClient {

  public AppClient(String... args) {
    Log.setName("ClientRMI");

    String name = "";
    int connectionMode = 0;
    int gameMode = 0;
    boolean domination = false;

    if (args.length < 3) {
      TUIInputManager inputManager = new TUIInputManager();

      String[] connectionModes = {"RMI", "Socket"};
      inputManager.input("Vuoi giocare tramite RMI o Socket?", Arrays.asList(connectionModes));
      try {
        connectionMode = inputManager.waitForIntResult();
      } catch (InputCancelledException ignored) {
        inputManager.cancel("");
      }

      inputManager.input("Come ti chiami? (max. 12 caratteri)", Constants.MAX_NAME_LENGTH);
      try {
        name = inputManager.waitForStringResult();
      } catch (InputCancelledException ignored) {
        inputManager.cancel("");
      }

      String[] gameModes = {"Classica", "Dominazione"};
      inputManager.input("In che modalità vuoi giocare?", Arrays.asList(gameModes));
      try {
        gameMode = inputManager.waitForIntResult();
      } catch (InputCancelledException ignored) {
        inputManager.cancel("");
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
        Log.severe("Modalità di gioco non valida. Supportate: (0) Classica; (1) Dominazione");
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
        Log.severe("Modalità di connessione non valida. Supportate: (0) RMI; (1) Socket");
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
