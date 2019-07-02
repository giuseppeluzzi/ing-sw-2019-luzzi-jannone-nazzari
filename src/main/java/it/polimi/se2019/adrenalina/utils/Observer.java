package it.polimi.se2019.adrenalina.utils;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;

import java.lang.reflect.InvocationTargetException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Any object that observes an Observable.
 */
public interface Observer extends Remote {

  default PlayerColor getPrivatePlayerColor() throws RemoteException {
    return null;
  }

  static void invokeEventHandler(Observer object, Event event) throws RemoteException {
    try {
      object.getClass().getMethod("update", event.getEventType().getEventClass())
              .invoke(object, event);
    } catch (NoSuchMethodException | IllegalAccessException ignored) {
      //
    } catch (InvocationTargetException e) {
      Log.exception(e);
    }
  }


  void update(Event event) throws RemoteException;
}
