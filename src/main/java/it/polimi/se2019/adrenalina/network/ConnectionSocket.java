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

  public ConnectionSocket(Client client) {
    this.client = client;
    try {
      Socket clientSocket = new Socket("127.0.0.1", SOCK_PORT);
      outputStream = clientSocket.getOutputStream();
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
  public void connect() {
    //try {
      PrintWriter printWriter = new PrintWriter(outputStream);
      printWriter.println(new PlayerConnectEvent(client.getName(), client.isDomination()).serialize());
    //} catch (IOException e) {
    //  Log.warn("Invalid encoding" + e.getMessage());
    //}
  }
}
