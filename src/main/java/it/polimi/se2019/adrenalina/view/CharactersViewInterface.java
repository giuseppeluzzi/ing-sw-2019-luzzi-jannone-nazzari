package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.event.modelview.PlayerDeathUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerPositionUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerStatusUpdate;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Observer;
import it.polimi.se2019.adrenalina.utils.RemoteObservable;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface CharactersViewInterface extends Observer, RemoteObservable, Serializable {

  default List<EventType> getHandledEvents() {
    List<EventType> registeredEvents = new ArrayList<>();

    registeredEvents.add(EventType.PLAYER_POSITION_UPDATE);
    registeredEvents.add(EventType.PLAYER_STATUS_UPDATE);

    return registeredEvents;
  }

  void setSelected(PlayerColor playerColor) throws RemoteException;

  void removeSelected() throws RemoteException;

  void showDeath(PlayerColor playerColor) throws RemoteException;
}
