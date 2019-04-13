package it.polimi.se2019.adrenalina.network;

import static java.lang.Thread.sleep;

import it.polimi.se2019.adrenalina.utils.Log;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements ClientInterface{
  private static final long serialVersionUID = -2824559728518448567L;
  private final String name;
  private final boolean domination;

  public Client(String name, boolean domination) throws RemoteException {
    this.name = name;
    this.domination = domination;

    try {
      ServerInterface server = (ServerInterface) Naming.lookup("//localhost/MyServer");

      server.addClient(this);
    } catch (MalformedURLException e) {
      Log.severe("RMI", "Incorrect URL!");
    } catch (NotBoundException e) {
      Log.severe("RMI", "Object not bound");
    } catch (RemoteException e) {
      Log.severe("RMI", "Connection error: " + e.getMessage());
    }

    new Thread(() -> {
      while (true) {
        try {
          sleep(1000);
        } catch (InterruptedException e) {
          Log.severe("Stopped!");
        }
      }
    }).start();
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
  public void ping() throws RemoteException {
    // useless
  }
}
