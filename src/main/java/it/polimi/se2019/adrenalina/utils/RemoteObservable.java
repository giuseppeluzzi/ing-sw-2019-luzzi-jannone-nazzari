package it.polimi.se2019.adrenalina.utils;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteObservable extends Remote {
  void addObserver(Observer observer) throws RemoteException;
  void removeObserver(Observer observer) throws RemoteException;
  void setObservers(List<Observer> observers) throws RemoteException;
  List<Observer> getObservers() throws RemoteException;
}
