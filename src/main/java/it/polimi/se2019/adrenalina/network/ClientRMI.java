package it.polimi.se2019.adrenalina.network;

import it.polimi.se2019.adrenalina.controller.Configuration;
import it.polimi.se2019.adrenalina.utils.Log;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static java.lang.Thread.sleep;

/**
 * RMI version of the client object.
 */
public class ClientRMI extends Client {

  private static final long serialVersionUID = 5097938777989686167L;
  private final boolean running = true;

  private transient ServerInterface server;
  private transient Thread pooler;

  public ClientRMI(String ipAddress, Integer port, String name, boolean domination, boolean tui) {
    super(name, domination, tui);

    try {
      Registry registry = LocateRegistry.getRegistry(
              ipAddress == null ? Configuration.getInstance().getServerIP() : ipAddress,
              port == null ? Configuration.getInstance().getRmiPort() : port);
      server = (ServerInterface) registry.lookup("MyServer");

      UnicastRemoteObject.exportObject(getBoardView(), 0);
      UnicastRemoteObject.exportObject(getCharactersView(), 0);
      UnicastRemoteObject.exportObject(getPlayerDashboardsView(), 0);

    } catch (NotBoundException e) {
      Log.severe("RMI", "Object not bound");
      Log.critical("Network error");
      return;
    } catch (RemoteException e) {
      Log.exception(e);
      Log.critical("Network error");
      return;
    }

    pooler = new Thread(() -> {
      while (running) {
        try {
          sleep(1000);
          server.ping(this);
        } catch (InterruptedException e) {
          Log.severe("ClientRMI", "Pooling interrupted! Thread stopped.");
          Thread.currentThread().interrupt();
          break;
        } catch (RemoteException e) {
          disconnect("La connessione con il server è stata persa!");
        }
      }
    });

    pooler.start();
  }

  public ServerInterface getServer() {
    return server;
  }

  @Override
  public void disconnect(String message) {
    super.disconnect(message);
    pooler.interrupt();
    System.exit(0);
  }

  @Override
  public boolean equals(Object obj) {
    return obj != null && (obj == this || getClass() == obj.getClass() && ((Client) obj).getName()
        .equals(getName()));
  }

  @Override
  public int hashCode() {
    return getName().hashCode();
  }
}
