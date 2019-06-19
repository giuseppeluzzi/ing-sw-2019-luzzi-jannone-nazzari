package it.polimi.se2019.adrenalina.controller.action.weapon;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.ExecutableObject;
import it.polimi.se2019.adrenalina.model.Player;
import java.util.ArrayList;
import java.util.List;

/**
 * Action used to shoot every player in a room.
 */
public class ShootRoomAction extends ShootAction {

  private static final long serialVersionUID = 3506478747182803402L;

  public ShootRoomAction(int target, int damages, int tag) {
    super(target, damages, tag);
    type = WeaponActionType.SHOOT_ROOM;
  }

  @Override
  public void execute(Board board, ExecutableObject object) {
    List<Player> players = getPlayers(board, object);
    for (Player player : players) {
      player.addDamages(object.getOwner().getColor(), getDamages());
      player.addTags(object.getOwner().getColor(), getTag());
    }
  }

  public List<Player> getPlayers(Board board, ExecutableObject object) {
    SquareColor roomColor = object.getTargetHistory(getTarget()).getSquare().getColor();
    List<Player> players = new ArrayList<>();
    for (Player player : board.getPlayers()) {
      if (player.getSquare().getColor() == roomColor) {
        players.add(player);
      }
    }
    return players;
  }

  @Override
  public String serialize(){
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  public static ShootRoomAction deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, ShootRoomAction.class);
  }


  @Override
  public boolean equals(Object object) {
    return object instanceof WeaponAction && ((WeaponAction) object).getActionType() == WeaponActionType.SHOOT_ROOM
        && ((ShootAction) object).getTarget() == getTarget()
        && ((ShootAction) object).getDamages() == getDamages()
        && ((ShootAction) object).getTag() == getTag();
  }

  @Override
  public int hashCode() {
    return getTarget() + getDamages() + getTag() + type.ordinal();
  }
}
