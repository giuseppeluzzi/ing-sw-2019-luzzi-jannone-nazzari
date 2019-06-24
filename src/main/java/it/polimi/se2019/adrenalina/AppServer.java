package it.polimi.se2019.adrenalina;

import it.polimi.se2019.adrenalina.controller.Configuration;
import it.polimi.se2019.adrenalina.network.Server;
import it.polimi.se2019.adrenalina.network.ServerSocket;
import it.polimi.se2019.adrenalina.utils.Log;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Main class of the application running as server.
 */
public class AppServer {
  public AppServer() {
    Log.setName("ServerRMI");
    Log.info("Server started!");

    try {
      Server server = new Server();

      Registry registry = LocateRegistry.createRegistry(Configuration.getInstance().getRmiPort());
      registry.rebind("MyServer", server);

      ServerSocket serverSocket = new ServerSocket(server);
      serverSocket.run();
    } catch (RemoteException e) {
      Log.severe("RMI connection error: " + e.getMessage());
    }
  }
}
