package it.polimi.se2019.adrenalina.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
  void addClient(ClientInterface client) throws RemoteException;
}
