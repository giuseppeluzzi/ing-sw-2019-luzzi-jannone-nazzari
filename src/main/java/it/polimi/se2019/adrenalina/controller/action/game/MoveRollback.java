package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.ExecutableObject;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import java.util.Map;

public class MoveRollback extends GameAction {

  private final ExecutableObject executableObject;

  public MoveRollback(TurnController turnController, Player player, ExecutableObject executableObject) {
    super(turnController, player);
    this.executableObject = executableObject;
  }

  public ExecutableObject getExecutableObject() {
    return executableObject;
  }

  @Override
  public void execute(Board board) {
    for (Map.Entry<Player, Square> entrySet : executableObject.getInitialPlayerPositions().entrySet()) {
      entrySet.getKey().setSquare(entrySet.getValue());
    }
    getTurnController().addTurnActions(new ActionSelection(getTurnController(), getPlayer()));
  }

  @Override
  public boolean isSync() {
    return false;
  }
}