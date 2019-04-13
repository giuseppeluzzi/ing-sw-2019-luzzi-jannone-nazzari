package it.polimi.se2019.adrenalina.network;

import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerConnectEvent;
import it.polimi.se2019.adrenalina.utils.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;

public class ClientSocket implements ClientInterface, Runnable {
  private final String name;
  private final boolean domination;

  private static final boolean running = true;

  private Socket clientSocket;
  private static final int SOCK_PORT = 3069;

  private PrintWriter printWriter = null;
  private BufferedReader bufferedReader;

  public ClientSocket(String name, boolean domination) {
    this.name = name;
    this.domination = domination;

    try {
      clientSocket = new Socket("127.0.0.1", SOCK_PORT);
      OutputStream outputStream = clientSocket.getOutputStream();
      printWriter = new PrintWriter(outputStream, true);
      bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "utf-8"));

    } catch (IOException e) {
      Log.severe("Socket", "IO Error: " + e.getMessage());
      return ;
    }

    sendEvent(new PlayerConnectEvent(name, domination));
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isDomination() {
    return domination;
  }

  @Override
  public void showMessage(String text) {
    Log.info(text);
  }

  @Override
  public void ping() {
    // useless
  }

  public final void sendEvent(Event event) throws IllegalStateException {
    printWriter.println(event.serialize());
    printWriter.flush();
  }

  @Override
  public void run() {
    while (running) {
      if (clientSocket.isConnected()) {
        String message = null;

        try {
          message = bufferedReader.readLine();
        } catch (IOException e) {
          Log.severe("Socket", "IO Error: " + e.getMessage());
        }

        Log.info(message);
      }
    }
  }
}
