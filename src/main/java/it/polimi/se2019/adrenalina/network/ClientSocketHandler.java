package it.polimi.se2019.adrenalina.network;

import it.polimi.se2019.adrenalina.controller.event.PlayerConnectEvent;
import it.polimi.se2019.adrenalina.utils.Log;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientSocketHandler implements Runnable {
  private final Socket clientSocket;
  private final Server server;

  private Scanner scanner = null;

  public ClientSocketHandler(Server server, Socket clientSocket) {
    this.clientSocket = clientSocket;
    this.server = server;

    try {
      scanner = new Scanner(new DataInputStream(clientSocket.getInputStream()), "utf-8");
    } catch (IOException e) {
      Log.severe("Socket", "IO Error: " + e.getMessage());
    }
  }

  @Override
  public void run() {
    Log.info("New socket client connected");
    DataInputStream stream = null;
    try {
      stream = new DataInputStream(clientSocket.getInputStream());

      while (clientSocket.isConnected()) {
        Log.info("ciaoo");

        //while (scanner.hasNextLine()) {
          Log.info(stream.readUTF());
          PlayerConnectEvent event = PlayerConnectEvent.deserialize(scanner.nextLine());
          Log.info(event.getPlayerName());
          Log.info(""+event.isDomination());

          Log.info("bbbbb");
          Client client = new Client(event.getPlayerName(), event.isDomination());
          client.setConnection(new ConnectionSocket(clientSocket));
          server.addClient(client);
      }
    } catch (RemoteException e) {
      Log.severe("RMI", "Connection error: " + e.getMessage());
    } catch (IOException e) {
      Log.info("errore");
    }
  }
}
