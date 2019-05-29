package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Weapon;
import java.util.Map;

public class MoveRollback extends GameAction {

  private final Weapon weapon;

  public MoveRollback(TurnController turnController, Player player, Weapon weapon) {
    super(turnController, player);
    this.weapon = weapon;
  }

  public Weapon getWeapon() {
    return weapon;
  }

  @Override
  public void execute(Board board) {
    for (Map.Entry<Player, Square> entrySet : weapon.getInitialPlayerPositions().entrySet()) {
      entrySet.getKey().setSquare(entrySet.getValue());
    }
    getTurnController().addTurnActions(new ActionSelection(getTurnController(), getPlayer()));
  }

  @Override
  public boolean isSync() {
    return false;
  }
}