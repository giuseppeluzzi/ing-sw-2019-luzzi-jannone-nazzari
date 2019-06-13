package it.polimi.se2019.adrenalina.utils;

import it.polimi.se2019.adrenalina.event.Event;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Observable implements RemoteObservable {
  @NotExpose
  private List<Observer> observers = new ArrayList<>();

  @Override
  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(Observer observer) {
    observers.remove(observer);
  }

  @Override
  public final void setObservers(List<Observer> observers) {
    this.observers = new ArrayList<>(observers);
  }

  @Override
  public List<Observer> getObservers() {
    return new ArrayList<>(observers);
  }

  protected void notifyObservers(Event event) throws RemoteException {
    for (Observer observer : new ArrayList<>(observers)) {
      if (event.getPrivatePlayerColor() == null || observer.getPrivatePlayerColor() == event.getPrivatePlayerColor()) {
        try {
          observer.update(event);
        } catch (ConnectException e) {
          observers.remove(observer);
        }
      }
    }
  }
}
