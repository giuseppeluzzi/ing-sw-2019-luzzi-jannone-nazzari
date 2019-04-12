package it.polimi.se2019.adrenalina.network;

import it.polimi.se2019.adrenalina.controller.BoardController;
import it.polimi.se2019.adrenalina.utils.Log;
import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class ServerSocket implements Runnable {
  private static final int SOCK_PORT = 3069;

  private java.net.ServerSocket serverSocket;
  private final Server server;
  private boolean running = true;

  public ServerSocket(Server server) {
    this.server = server;
    try {
      serverSocket = new java.net.ServerSocket(SOCK_PORT);
    } catch (IOException e) {
      Log.severe("Socket", "IO Error: " + e.getMessage());
    }
  }

  @Override
  public void run() {
    Socket socket = null;
    while (running) {
      try {
        socket = serverSocket.accept();
        new ClientSocketHandler(server, socket).run();
      } catch (IOException e) {
        Log.severe("Socket", "IO Error: " + e.getMessage());
      }
    }
  }

}
