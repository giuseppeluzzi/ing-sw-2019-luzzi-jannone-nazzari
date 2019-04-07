package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerAttackEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerReloadEvent;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;

public class AttackController implements Observer {
  private final BoardController boardController;

  AttackController(BoardController boardController) {
    this.boardController = boardController;
  }

  public void update(PlayerAttackEvent event) {
    // TODO: invoked when a player attacks a target with a weapon-effect; if the target is a spawnpoint, the domination board should be notified
  }

  public void update(PlayerReloadEvent event) {
    // TODO: invoked when a player reloads a weapon
  }

  @Override
  public void update(Event event) {
    throw new WrongMethodTypeException();
  }
}
