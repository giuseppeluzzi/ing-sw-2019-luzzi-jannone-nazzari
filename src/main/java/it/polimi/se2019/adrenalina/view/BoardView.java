package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.BoardController;
import it.polimi.se2019.adrenalina.controller.events.AmmoCardUpdateEvent;
import it.polimi.se2019.adrenalina.controller.events.DoubleKillEvent;
import it.polimi.se2019.adrenalina.controller.events.Event;
import it.polimi.se2019.adrenalina.controller.events.KillShotEvent;
import it.polimi.se2019.adrenalina.controller.events.SpawnPointDamageEvent;
import it.polimi.se2019.adrenalina.controller.events.WeaponUpdateEvent;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.MessageSeverity;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;

public class BoardView extends Observable implements Observer {
  private Board board;

  public BoardView(Board board) {
    this.board = board;
  }

  public void startTimer(Player player, int time) {

  }

  public void hideTimer(Player player, int time) {

  }

  public void showMessage(MessageSeverity severity, String title, String message) {

  }

  public void reset(Board board) {

  }

  public void update(WeaponUpdateEvent event) {

  }

  public void update(AmmoCardUpdateEvent event) {

  }

  public void update(KillShotEvent event) {

  }

  public void update(DoubleKillEvent event) {

  }

  public void update(SpawnPointDamageEvent event) {

  }

  @Override
  public void update(Event event) throws WrongMethodTypeException {

  }
}
