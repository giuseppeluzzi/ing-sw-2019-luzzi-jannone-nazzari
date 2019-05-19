package it.polimi.se2019.adrenalina.controller.action;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SelectAction implements Action {

  private static final long serialVersionUID = -1712202363884739599L;
  private int from;
  private int target;
  private int minDistance;
  private int maxDistance = -1;
  private int[] differentFrom = {};
  private int[] between = {};
  private Boolean visible = null;
  private boolean optional = false;
  private boolean useLastDirection = false;
  private boolean differentRoom = false;
  private TargetType selectType = TargetType.ATTACK_TARGET;
  private ActionType type = ActionType.SELECT;

  public SelectAction(int from, int target, int minDistance,
      int maxDistance, int[] differentFrom, int[] between,
      Boolean visible, boolean optional, boolean useLastDirection,
      boolean differentRoom, TargetType selectType) {

    this.from = from;
    this.target = target;
    this.minDistance = minDistance;
    this.maxDistance = maxDistance;
    this.differentFrom = differentFrom.clone();
    this.between = between.clone();
    this.visible = visible;
    this.optional = optional;
    this.useLastDirection = useLastDirection;
    this.differentRoom = differentRoom;
    this.selectType = selectType;
    type = ActionType.SELECT;
  }

  @Override
  public ActionType getActionType() {
    return type;
  }

  private void blacklist(List<Target> targets, int[] differentFrom, Weapon weapon) {
    if (differentFrom.length > 0) {
      for (int targetIndex : differentFrom) {
        targets.remove(weapon.getTargetHistory(targetIndex));
      }
    }
  }

  private void whitelist(List<Target> targets, int[] between, Weapon weapon) {
    if (between.length > 0) {
      List<Target> allowed = new ArrayList<>();
      for (int targetIndex : between) {
        allowed.add(weapon.getTargetHistory(targetIndex));
      }
      for (Target targ : new ArrayList<>(targets)) {
        if (! allowed.contains(targ)) {
          targets.remove(targ);
        }
      }
    }
  }

  @Override
  public void execute(Board board, Weapon weapon) {
    // TODO: show selection, ignore if target in targethistory is alredy setted
    Player owner = weapon.getOwner();
    List<Target> targets = new ArrayList<>();

    switch (selectType) {
      case ATTACK_TARGET:
        targets.addAll(board.getPlayers());
        if (board.isDominationBoard()) {
          for (Square square: board.getSquares()) {
            if (square.isSpawnPoint()) {
              targets.add(square);
            }
          }
        }
        break;
      case ATTACK_SQUARE:
      case ATTACK_ROOM:
      case MOVE_SQUARE:
        targets.addAll(board.getSquares());
        break;
    }

    targets.addAll(board.getSquares());
    targets.remove(owner);

    Target fromTarget = weapon.getTargetHistory(from);

    // differentFrom
    blacklist(targets, differentFrom, weapon);

    // between
    whitelist(targets, between, weapon);

    Stream<Target> targetStream = targets.stream();

    // minDistance
    targetStream = targetStream.filter(x -> fromTarget.getSquare().getDistance(x.getSquare()) >= minDistance);

    // maxDistance
    if (maxDistance >= 0) {
      targetStream = targetStream.filter(x -> fromTarget.getSquare().getDistance(x.getSquare()) <= maxDistance);
    }

    // visible
    if (visible != null) {
      targetStream = targetStream.filter(x -> fromTarget.getSquare().isVisible(x.getSquare()) == visible);
    }

    // useLastDirection
    if (useLastDirection) {
      targetStream = targetStream.filter(x -> {
        try {
          return
              fromTarget.getSquare().getCardinalDirection(x.getSquare()) == weapon.getLastUsageDirection();
        } catch (InvalidSquareException e) {
          return false;
        }
      });
    }

    // differentRoom
    if (differentRoom) {
      targetStream = targetStream.filter(x -> fromTarget.getSquare().getColor() == x.getSquare().getColor());
    }

    targets = targetStream.collect(Collectors.toList());

    // TODO: show selection to the user
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  public static SelectAction deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, SelectAction.class);
  }

  public List<Target> filter(List<Target> targets) {
    // TODO: implement filter
    return new ArrayList<>();
  }

  public int getFrom() {
    return from;
  }

  public int getTarget() {
    return target;
  }

  public int getMinDistance() {
    return minDistance;
  }

  public int getMaxDistance() {
    return maxDistance;
  }

  public int[] getDifferentFrom() {
    return differentFrom.clone();
  }

  public int[] getBetween() {
    return between.clone();
  }

  public TargetType getSelectType() {
    return selectType;
  }

  public Boolean isVisible() {
    return visible;
  }

  public boolean isOptional() {
    return optional;
  }

  public boolean isUseLastDirection() {
    return useLastDirection;
  }

  public boolean isDifferentRoom() {
    return differentRoom;
  }

  @Override
  public boolean equals(Object object) {
    return object instanceof Action && ((Action) object).getActionType() == ActionType.SELECT
        && ((SelectAction) object).visible == visible
        && Arrays.equals(((SelectAction) object).differentFrom, differentFrom)
        && Arrays.equals(((SelectAction) object).between, between)
        && ((SelectAction) object).from == from
        && ((SelectAction) object).minDistance == minDistance
        && ((SelectAction) object).maxDistance == maxDistance
        && ((SelectAction) object).target == target
        && ((SelectAction) object).differentRoom == differentRoom
        && ((SelectAction) object).selectType == selectType
        && ((SelectAction) object).useLastDirection == useLastDirection;
  }

  @Override
  public int hashCode() {
    return differentFrom.length + Arrays.hashCode(differentFrom) + between.length +
        Arrays.hashCode(between) + from + minDistance + maxDistance + target + type.ordinal();
  }
}
