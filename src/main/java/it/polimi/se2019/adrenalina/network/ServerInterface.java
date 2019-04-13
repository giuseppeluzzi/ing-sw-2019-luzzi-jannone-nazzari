package it.polimi.se2019.adrenalina.network;

import it.polimi.se2019.adrenalina.controller.BoardController;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerConnectEvent;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
  void addClient(ClientInterface client) throws RemoteException;
}
