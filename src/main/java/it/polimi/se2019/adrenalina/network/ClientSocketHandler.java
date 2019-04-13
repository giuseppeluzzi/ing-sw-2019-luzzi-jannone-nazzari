package it.polimi.se2019.adrenalina.network;

import it.polimi.se2019.adrenalina.controller.event.PlayerConnectEvent;
import it.polimi.se2019.adrenalina.utils.Log;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientSocketHandler implements Runnable {
  private final Socket clientSocket;
  private final Server server;
  private BufferedReader bufferedReader = null;


  public ClientSocketHandler(Server server, Socket clientSocket) {
    this.clientSocket = clientSocket;
    this.server = server;

    try {
      bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "utf-8"));
    } catch (IOException e) {
      Log.severe("Socket", "IO Error: " + e.getMessage());
    }
  }

  @Override
  public void run() {
    try {
      while (clientSocket.isConnected()) {

        String message = bufferedReader.readLine();
        PlayerConnectEvent event = PlayerConnectEvent.deserialize(message);

        Client client = new Client(event.getPlayerName(), event.isDomination());
        client.setConnection(new ConnectionSocket(clientSocket));
        server.addClient(client);
      }
    } catch (RemoteException e) {
      Log.severe("RMI", "Connection error: " + e.getMessage());
    } catch (IOException e) {
      Log.severe("Socket", "IO Error: " + e.getMessage());
    }
  }
}
