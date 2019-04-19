package it.polimi.se2019.adrenalina.network;

import static java.lang.Thread.sleep;

import it.polimi.se2019.adrenalina.utils.Log;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements ClientInterface {
  private static final long serialVersionUID = -2824559728518448567L;

  private final String name;
  private final boolean domination;
  private volatile boolean running = true;

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
      Log.exception(e);
    }

    final Thread pooling = new Thread(() -> {
      while (running) {
        try {
          sleep(1000);
        } catch (InterruptedException e) {
          Log.severe("Client", "Pooling interrupted! Thread stopped.");
          Thread.currentThread().interrupt();
        }
      }
    });

    pooling.start();
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

  @Override
  public void disconnect() {
    running = false;
  }

  @Override
  public boolean equals(Object obj) {
    return obj != null && (obj == this || getClass() == obj.getClass() && ((Client) obj).name
        .equals(name));
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}
