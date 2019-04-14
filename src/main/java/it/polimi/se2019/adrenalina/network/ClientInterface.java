package it.polimi.se2019.adrenalina.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
  String getName() throws RemoteException;
  boolean isDomination() throws RemoteException;
  void showMessage(String text) throws RemoteException;
  void ping() throws RemoteException;
  void disconnect() throws RemoteException;
}
