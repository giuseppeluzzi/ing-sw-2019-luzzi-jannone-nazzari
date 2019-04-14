package it.polimi.se2019.adrenalina;

import it.polimi.se2019.adrenalina.controller.Configuration;
import it.polimi.se2019.adrenalina.network.Server;
import it.polimi.se2019.adrenalina.network.ServerSocket;
import it.polimi.se2019.adrenalina.utils.Log;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class AppServer {
  public static void main(String... args) {
    Log.setName("ServerRMI");

    // Verify if the configuration exists
    Configuration.getInstance();

    try {
      Server server = new Server();

      LocateRegistry.createRegistry(Configuration.getInstance().getRmiPort());
      Naming.rebind("//localhost/MyServer", server);

      ServerSocket serverSocket = new ServerSocket(server);
      serverSocket.run();
    } catch (MalformedURLException e) {
      Log.severe("RMI incorrect URL!");
    } catch (RemoteException e) {
      Log.severe("RMI connection error: " + e.getMessage());
    }
  }
}
