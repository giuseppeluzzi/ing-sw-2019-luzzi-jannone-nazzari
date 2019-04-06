package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.events.Event;
import it.polimi.se2019.adrenalina.controller.events.PlayerAttackEvent;
import it.polimi.se2019.adrenalina.controller.events.PlayerReloadEvent;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;

public class AttackController implements Observer {
  private BoardController boardController;

  AttackController(BoardController boardController) {
    this.boardController = boardController;
  }

  public void update(PlayerAttackEvent event) {

  }

  public void update(PlayerReloadEvent event) {

  }

  @Override
  public void update(Event event) throws WrongMethodTypeException {

  }
}
