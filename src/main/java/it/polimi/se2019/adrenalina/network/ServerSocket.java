package it.polimi.se2019.adrenalina.network;

import it.polimi.se2019.adrenalina.utils.Log;
import java.io.IOException;
import java.net.Socket;

public class ServerSocket implements Runnable {
  private static final int SOCK_PORT = 3069;

  private java.net.ServerSocket socket;
  private final Server server;

  private volatile boolean running = true;

  public ServerSocket(Server server) {
    this.server = server;
    try {
      socket = new java.net.ServerSocket(SOCK_PORT);
    } catch (IOException e) {
      Log.exception(e);
    }
  }

  @Override
  public void run() {
    Socket clientSocket = null;
    while (running) {
      try {
        clientSocket = socket.accept();
        new VirtualClientSocket(server, clientSocket).run();
      } catch (IOException e) {
        Log.exception(e);
      }
    }
  }

  public void stop() {
    running = false;
  }

}
