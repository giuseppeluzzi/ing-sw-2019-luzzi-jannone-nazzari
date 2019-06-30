package it.polimi.se2019.adrenalina;

import it.polimi.se2019.adrenalina.controller.Configuration;
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

/**
 * Main class of the application running as client.
 */
public class AppClient {

  public AppClient(String... args) {
    Log.setName("ClientRMI");

    String ipAddress;
    Integer port = null;
    String name = "";
    int connectionMode = 0;
    boolean domination = false;

    if (args.length < 5) {
      TUIInputManager inputManager = new TUIInputManager();
      connectionMode = getInteractiveConnectionMode(inputManager);
      ipAddress = getInteractiveIpAddress(inputManager);
      port = getInteractivePort(inputManager, connectionMode);
      name = getInteractivePlayerName(inputManager);
      domination = getInteractiveDomination(inputManager);
    } else {
      ipAddress = args[0];
      port = Integer.parseInt(args[1]);
      name = args[2];
      connectionMode = Integer.parseInt(args[3]);
      if (args[4].charAt(0) == '0') {
        domination = false;
      } else if (args[4].charAt(0) == '1') {
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
          ClientRMI clientRMI = new ClientRMI(ipAddress, port, name, domination, true);
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
        client = new ClientSocket(ipAddress, port, name, domination, true);
        ((Runnable) client).run();
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

  private String getInteractiveIpAddress(TUIInputManager inputManager) {
    while (true) {
      inputManager.input(
              String.format("Inserisci l'indirizzo IP del server (default: %s)", Configuration.getInstance().getServerIP()),
              0,
              Integer.MAX_VALUE);
      try {
        String ipAddress = inputManager.waitForStringResult().trim();
        if (ipAddress.isEmpty()) {
          return null;
        } else if (ipAddress.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$")) {
          return ipAddress;
        } else {
          Log.println("Indirizzo non valido");
        }
      } catch (InputCancelledException ignored) {
        throw new IllegalStateException("Input cancelled during ip address selection");
      }
    }
  }

  private Integer getInteractivePort(TUIInputManager inputManager, int connectionMode) {
    while (true) {
      inputManager.input(
              String.format
                      ("Inserisci la porta da usare (default: %d)",
                       connectionMode == 0 ? Configuration.getInstance().getRmiPort() : Configuration.getInstance().getSocketPort()),
              0,
              Integer.MAX_VALUE);
      try {
        String port = inputManager.waitForStringResult().trim();
        if (port.isEmpty()) {
          return null;
        } else if (port.matches("^\\d{1,5}$")) {
          return Integer.parseInt(port);
        } else {
          Log.println("Porta non valida");
        }
      } catch (InputCancelledException ignored) {
        throw new IllegalStateException("Input cancelled during ip address selection");
      }
    }
  }

  private int getInteractiveConnectionMode(TUIInputManager inputManager) {
    String[] connectionModes = {"RMI", "Socket"};
    inputManager.input("Vuoi giocare tramite RMI o Socket?", Arrays.asList(connectionModes));
    try {
      return inputManager.waitForIntResult();
    } catch (InputCancelledException e) {
      throw new IllegalStateException("Input cancelled during connection mode selection");
    }
  }

  private String getInteractivePlayerName(TUIInputManager inputManager) {
    inputManager.input("Come ti chiami? (max. 12 caratteri)", 1, Constants.MAX_NAME_LENGTH);
    try {
      return inputManager.waitForStringResult();
    } catch (InputCancelledException ignored) {
      throw new IllegalStateException("Input cancelled during name selection");
    }
  }

  private boolean getInteractiveDomination(TUIInputManager inputManager) {
    String[] gameModes = {"Classica", "Dominazione"};
    inputManager.input("In che modalità vuoi giocare?", Arrays.asList(gameModes));
    int gameMode = 0;
    try {
      gameMode = inputManager.waitForIntResult();
    } catch (InputCancelledException ignored) {
      throw new IllegalStateException("Input cancelled during game mode selection");
    }
    return gameMode == 1;
  }
}
