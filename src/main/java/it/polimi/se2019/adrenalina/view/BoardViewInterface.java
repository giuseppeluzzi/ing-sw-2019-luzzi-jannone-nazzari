package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.event.AmmoCardUpdateEvent;
import it.polimi.se2019.adrenalina.controller.event.DoubleKillEvent;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.KillShotEvent;
import it.polimi.se2019.adrenalina.controller.event.SpawnPointDamageEvent;
import it.polimi.se2019.adrenalina.controller.event.WeaponUpdateEvent;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.MessageSeverity;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;

public interface BoardViewInterface  {

  public Board getBoard();

  public void setBoard(Board board);

  public void startTimer(int time);

  public void hideTimer(int time);

  public void showMessage(MessageSeverity severity, String title, String message);

  public void reset();

  public void update(WeaponUpdateEvent event);

  public void update(AmmoCardUpdateEvent event);

  public void update(KillShotEvent event);

  public void update(DoubleKillEvent event);

  public void update(SpawnPointDamageEvent event);

  public void update(Event event);
}
