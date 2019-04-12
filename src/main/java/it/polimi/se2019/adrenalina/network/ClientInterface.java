package it.polimi.se2019.adrenalina.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
  void connect() throws RemoteException;
}
