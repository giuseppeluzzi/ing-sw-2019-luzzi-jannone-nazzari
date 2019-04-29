package it.polimi.se2019.adrenalina.utils;

import it.polimi.se2019.adrenalina.controller.event.Event;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Observer extends Remote {
  void update(Event event) throws RemoteException;
}
