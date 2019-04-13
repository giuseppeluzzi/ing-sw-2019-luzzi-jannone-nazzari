package it.polimi.se2019.adrenalina.network;

import it.polimi.se2019.adrenalina.utils.Log;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ConnectionRMI implements ClientInterface {
  private final Client client;
  private ServerInterface server;

  public ConnectionRMI(Client client) {
    this.client = client;

    try {
      server = (ServerInterface) Naming.lookup("//localhost/MyServer");
    } catch (MalformedURLException e) {
      Log.severe("RMI", "Incorrect URL!");
    } catch (NotBoundException e) {
      Log.severe("RMI", "Object not bound");
    } catch (RemoteException e) {
      Log.severe("RMI", "Connection error: " + e.getMessage());
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
    try {
      server.addClient(client);
    } catch (RemoteException e) {
      Log.severe("RMI", "Connection error: " + e.getMessage());
    }
  }
}
