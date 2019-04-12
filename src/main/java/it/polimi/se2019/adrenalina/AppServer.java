package it.polimi.se2019.adrenalina;

import it.polimi.se2019.adrenalina.network.Server;
import it.polimi.se2019.adrenalina.network.ServerInterface;
import it.polimi.se2019.adrenalina.network.ServerRMI;
import it.polimi.se2019.adrenalina.network.ServerSocket;
import it.polimi.se2019.adrenalina.utils.Log;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class AppServer {

  private static final int RMI_PORT = 1099;
  private static final int SOCK_PORT = 3069;

  public static void main(String... args) {
    Log.setName("ServerRMI");
    Server server = new Server();

    try {
      ServerInterface serverRMI = new ServerRMI(server);
      LocateRegistry.createRegistry(RMI_PORT);
      Naming.rebind("//localhost/MyServer", serverRMI);
    } catch (MalformedURLException e) {
      Log.severe("RMI incorrect URL!");
    } catch (RemoteException e) {
      Log.severe("RMI connection error: " + e.getMessage());
    }

    ServerSocket serverSocket = new ServerSocket(server);
    ((Runnable) serverSocket).run();
  }
}
