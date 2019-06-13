package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.exceptions.InvalidPowerUpException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.ExecutableObject;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Weapon;

public class AfterUsageExecutable extends GameActionAsync {

  private final ExecutableObject executableObject;

  public AfterUsageExecutable(TurnController turnController,
      Player player, ExecutableObject executableObject) {
    super(turnController, player);
    this.executableObject = executableObject;
  }

  @Override
  public void execute(Board board) {
    try {
      if (executableObject.isWeapon()) {
        ((Weapon) executableObject).setLoaded(false);
      } else {
        getPlayer().removePowerUp((PowerUp) executableObject);
        board.undrawPowerUp((PowerUp) executableObject);
      }
    } catch (InvalidPowerUpException ignore) {
      //
    }
  }
}
