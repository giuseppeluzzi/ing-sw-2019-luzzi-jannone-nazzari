package it.polimi.se2019.adrenalina.controller.event;

public interface Event {
  String serialize();
  Event deserialize(String json);
}