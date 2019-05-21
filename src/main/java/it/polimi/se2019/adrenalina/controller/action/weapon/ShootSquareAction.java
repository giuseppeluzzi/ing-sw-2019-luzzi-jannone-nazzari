package it.polimi.se2019.adrenalina.controller.action.weapon;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import java.util.ArrayList;
import java.util.List;

public class ShootSquareAction extends ShootAction {

  private static final long serialVersionUID = 2321520813151230768L;
  private final int distance;
  private final int[] exclude;

  public ShootSquareAction(int target, int damages, int tag, int distance, int[] exclude) {
    super(target, damages, tag);
    this.distance = distance;
    this.exclude = exclude.clone();
    type = WeaponActionType.SHOOT_SQUARE;
  }


  @Override
  public void execute(Board board, Weapon weapon) {
    List<Player> playersToExclude = new ArrayList<>();
    for (int player : exclude) {
      try {
        playersToExclude.add(weapon.getTargetHistory(player).getPlayer());
      } catch (InvalidSquareException e) {
        Log.severe("ShootSquareAction: one of the targets in exclude[] is not a player; ignoring it");
      }
    }
    for (Square square : board.getSquares()) {
      if (weapon.getTargetHistory(getTarget()).getSquare().getDistance(square) == distance) {
        for (Player player : square.getPlayers()) {
          if (! playersToExclude.contains(player)) {
            player.addDamages(weapon.getOwner().getColor(), getDamages());
            player.addTags(weapon.getOwner().getColor(), getTag());
          }
        }
      }
    }
  }

  @Override
  public String serialize(){
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  public int getDistance() {
    return distance;
  }

  public static ShootSquareAction deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, ShootSquareAction.class);
  }

  public int[] getExclude() {
    return exclude.clone();
  }

  @Override
  public boolean equals(Object object) {
    return object instanceof WeaponAction && ((WeaponAction) object).getActionType() == WeaponActionType.SHOOT_SQUARE
        && ((ShootAction) object).getTarget() == getTarget()
        && ((ShootAction) object).getDamages() == getDamages()
        && ((ShootSquareAction) object).getExclude() == getExclude()
        && ((ShootAction) object).getTag() == getTag();
  }

  @Override
  public int hashCode() {
    return getTarget() + getDamages() + getTag() + type.ordinal() + getExclude().length;
  }
}
