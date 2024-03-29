package it.polimi.se2019.adrenalina.network;

import it.polimi.se2019.adrenalina.controller.ServerConfig;
import it.polimi.se2019.adrenalina.utils.Log;

import java.io.IOException;
import java.net.Socket;

/**
 * Socket version of the server.
 */
public class ServerSocket implements Runnable {

  private java.net.ServerSocket socket;
  private final Server server;

  private volatile boolean running = true;

  public ServerSocket(Server server) {
    this.server = server;
    try {
      socket = new java.net.ServerSocket(ServerConfig.getInstance().getSocketPort());
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
        new Thread(new VirtualClientSocket(server, clientSocket)).start();
      } catch (IOException e) {
        Log.exception(e);
      }
    }
  }

  public void stop() {
    running = false;
  }

}
