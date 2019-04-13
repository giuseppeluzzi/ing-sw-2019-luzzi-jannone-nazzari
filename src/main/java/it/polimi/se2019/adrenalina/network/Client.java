package it.polimi.se2019.adrenalina.network;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements ClientInterface {
  private static final long serialVersionUID = 8646824057830342998L;

  private transient ClientInterface connection;
  private final String name;
  private final boolean domination;

  public Client(String name, boolean domination) throws RemoteException {
    this.name = name;
    this.domination = domination;
  }

  public void setConnection(ClientInterface connection) {
    this.connection = connection;
  }

  @Override
  public void connect() throws RemoteException {
    connection.connect();
  }

  @Override
  public String getName() {
    return name;
  }

  public boolean isDomination() {
    return domination;
  }
}
