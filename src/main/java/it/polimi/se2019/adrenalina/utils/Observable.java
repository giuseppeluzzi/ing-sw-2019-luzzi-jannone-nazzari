package it.polimi.se2019.adrenalina.utils;

import it.polimi.se2019.adrenalina.controller.event.Event;
import java.util.ArrayList;
import java.util.List;

public class Observable {
  private final List<Observer> observers = new ArrayList<>();

  public void addObserver(Observer observer) {
    synchronized (observers) {
      observers.add(observer);
    }
  }

  public void removeObserver(Observer observer) {
    synchronized (observers) {
      observers.remove(observer);
    }
  }

  protected void notifyObservers(Event event) {
    synchronized (observers) {
      for (Observer observer : observers){
        observer.update(event);
      }
    }
  }
}
