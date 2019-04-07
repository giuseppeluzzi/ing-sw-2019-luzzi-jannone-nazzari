package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerCollectAmmoEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerCollectWeaponEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerMoveEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerPowerUpEvent;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;

public class PlayerController implements Observer {
  private final BoardController boardController;

  PlayerController(BoardController boardController) {
    this.boardController = boardController;
  }


  public void update(PlayerMoveEvent event) {
    // TODO: invoked when a player wants to go in a square
  }

  public void update(PlayerCollectAmmoEvent event) {
    // TODO: invoked when a player wants to collect an ammocard
  }

  public void update(PlayerCollectWeaponEvent event) {
    // TODO: invoked when a player wants to collect a weapon
  }

  public void update(PlayerPowerUpEvent event) {
    // TODO: invoked when a player uses a powerup
  }

  @Override
  public void update(Event event) {
    throw new WrongMethodTypeException();
  }
}
