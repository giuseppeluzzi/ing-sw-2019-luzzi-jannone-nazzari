package it.polimi.se2019.adrenalina.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
  String getName() throws RemoteException;
  boolean isDomination() throws RemoteException;
  void connect() throws RemoteException;
}
