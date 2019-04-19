package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerAttackEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerReloadEvent;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;

public class AttackController implements Observer {
  private final BoardController boardController;

  AttackController(BoardController boardController) {
    this.boardController = boardController;

    //boardController.getBoard().getWeapons().add
    Weapon weapon = new Weapon(0, 1, 0, AmmoColor.BLUE, "Distruttore");
    Effect base = new Effect("Effetto base", weapon, 0, 0, 0);
    base.addAction(new SelectAction(0, 1, 0, 0, 0, true));
    base.addAction(new ShootAction(1, 2, 1));
    Effect bis = new Effect("Secondo aggancio", weapon, 1, 0, 0);
    bis.addAction(new SelectAction(0, 2, 0, 0, 1, true));
    bis.addAction(new ShootAction(2, 0, 1));
    base.addSubEffect(bis);
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
