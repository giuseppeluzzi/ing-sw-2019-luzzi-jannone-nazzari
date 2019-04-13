package it.polimi.se2019.adrenalina.network;

import it.polimi.se2019.adrenalina.controller.event.PlayerConnectEvent;
import it.polimi.se2019.adrenalina.utils.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionSocket implements ClientInterface {
  private static final int SOCK_PORT = 3069;
  private Client client = null;
  private OutputStream outputStream;
  private InputStream inputStream;
  private PrintWriter printWriter;

  public ConnectionSocket(Client client) {
    this.client = client;
    try {
      Socket clientSocket = new Socket("127.0.0.1", SOCK_PORT);
      outputStream = clientSocket.getOutputStream();
      printWriter = new PrintWriter(outputStream, true);
      inputStream = clientSocket.getInputStream();
    } catch (IOException e) {
      Log.severe("Socket", "IO Error: " + e.getMessage());
    }
  }

  public ConnectionSocket(Socket clientSocket) {
    try {
      outputStream = clientSocket.getOutputStream();
      inputStream = clientSocket.getInputStream();
    } catch (IOException e) {
      Log.severe("Socket", "IO Error: " + e.getMessage());
    }
  }

  @Override
  public String getName() {
    return client.getName();
  }

  @Override
  public boolean isDomination() {
    return client.isDomination();
  }

  @Override
  public void connect() {
    printWriter.println(new PlayerConnectEvent(client.getName(), client.isDomination()).serialize());
    printWriter.flush();
  }
}
