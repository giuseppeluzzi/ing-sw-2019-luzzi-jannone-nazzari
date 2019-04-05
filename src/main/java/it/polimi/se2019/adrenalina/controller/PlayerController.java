package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.events.Event;
import it.polimi.se2019.adrenalina.controller.events.PlayerCollectAmmoEvent;
import it.polimi.se2019.adrenalina.controller.events.PlayerCollectWeaponEvent;
import it.polimi.se2019.adrenalina.controller.events.PlayerMoveEvent;
import it.polimi.se2019.adrenalina.controller.events.PlayerPowerUpEvent;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;

public class PlayerController implements Observer {
  private Board board;

  PlayerController(Board board) {
    this.board = board;
  }


  public void update(PlayerMoveEvent event) {

  }

  public void update(PlayerCollectAmmoEvent event) {

  }

  public void update(PlayerCollectWeaponEvent event) {

  }

  public void update(PlayerPowerUpEvent event) {

  }

  @Override
  public void update(Event event) throws WrongMethodTypeException {

  }
}
