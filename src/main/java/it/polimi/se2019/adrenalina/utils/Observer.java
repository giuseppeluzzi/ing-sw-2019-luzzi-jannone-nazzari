package it.polimi.se2019.adrenalina.utils;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Observer extends Remote {

  default PlayerColor getPrivatePlayerColor() throws RemoteException {
    return null;
  }

  void update(Event event) throws RemoteException;
}
