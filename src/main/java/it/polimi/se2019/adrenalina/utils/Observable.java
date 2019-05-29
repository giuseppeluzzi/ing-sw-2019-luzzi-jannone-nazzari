package it.polimi.se2019.adrenalina.utils;

import it.polimi.se2019.adrenalina.event.Event;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Observable implements RemoteObservable {
  @NotExpose
  private final List<Observer> observers = new ArrayList<>();

  @Override
  public void addObserver(Observer observer) {
    synchronized (observers) {
      observers.add(observer);
    }
  }

  @Override
  public void removeObserver(Observer observer) {
    synchronized (observers) {
      observers.remove(observer);
    }
  }

  @Override
  public final void setObservers(List<Observer> observers) {
    this.observers.clear();
    this.observers.addAll(observers);
  }

  @Override
  public List<Observer> getObservers() {
    return new ArrayList<>(observers);
  }

  protected void notifyObservers(Event event) throws RemoteException {
    for (Observer observer : observers){
      observer.update(event);
    }
  }
}
