package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.event.AmmoCardUpdateEvent;
import it.polimi.se2019.adrenalina.controller.event.DoubleKillEvent;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.KillShotEvent;
import it.polimi.se2019.adrenalina.controller.event.SpawnPointDamageEvent;
import it.polimi.se2019.adrenalina.controller.event.WeaponUpdateEvent;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.controller.MessageSeverity;

public interface BoardViewInterface  {

  Board getBoard();

  void setBoard(Board board);

  void startTimer(int time);

  void hideTimer(int time);

  void showMessage(MessageSeverity severity, String title, String message);

  void reset();

  void update(WeaponUpdateEvent event);

  void update(AmmoCardUpdateEvent event);

  void update(KillShotEvent event);

  void update(DoubleKillEvent event);

  void update(SpawnPointDamageEvent event);

  void update(Event event);
}
