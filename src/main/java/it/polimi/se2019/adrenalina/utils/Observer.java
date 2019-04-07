package it.polimi.se2019.adrenalina.utils;

import it.polimi.se2019.adrenalina.controller.event.Event;

public interface Observer {
  void update(Event event);
}
