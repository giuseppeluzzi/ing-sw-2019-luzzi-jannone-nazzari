package it.polimi.se2019.adrenalina.controller.action.weapon;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;
import it.polimi.se2019.adrenalina.exceptions.NoTargetsException;
import it.polimi.se2019.adrenalina.exceptions.NoTargetsExceptionOptional;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.ExecutableObject;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.utils.Log;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Action used to prompt the user for a square or target selection.
 */
public class SelectAction implements WeaponAction {

  private static final long serialVersionUID = -1712202363884739599L;
  private int from;
  private int target;
  private int minDistance;
  private int maxDistance = -1;
  private int[] differentFrom = {};
  private int[] between = {};
  private Boolean visible = null;
  private boolean optional = false;
  private boolean skippable = false;
  private boolean useLastDirection = false;
  private boolean differentRoom = false;
  private boolean stopPropagation;
  private boolean disallowSpawnPoint;
  private TargetType selectType = TargetType.ATTACK_TARGET;
  private WeaponActionType type = WeaponActionType.SELECT;

  public SelectAction(int from, int target, int minDistance,
                      int maxDistance, int[] differentFrom, int[] between,
                      Boolean visible, boolean optional, boolean useLastDirection,
                      boolean differentRoom, TargetType selectType, boolean skippable,
                      boolean stopPropagation, boolean disallowSpawnPoint) {

    this.from = from;
    this.target = target;
    this.minDistance = minDistance;
    this.maxDistance = maxDistance;
    this.differentFrom = differentFrom.clone();
    this.between = between.clone();
    this.visible = visible;
    this.optional = optional;
    this.skippable = skippable;
    this.useLastDirection = useLastDirection;
    this.differentRoom = differentRoom;
    this.selectType = selectType;
    this.stopPropagation = stopPropagation;
    this.disallowSpawnPoint = disallowSpawnPoint;
    type = WeaponActionType.SELECT;
  }

  @Override
  public WeaponActionType getActionType() {
    return type;
  }

  private void blacklist(List<Target> targets, int[] differentFrom, ExecutableObject object) {
    if (differentFrom.length > 0) {
      for (int targetIndex : differentFrom) {
        if (object.targetHistoryContainsKey(targetIndex)) {
            targets.remove(object.getTargetHistory(targetIndex));
        }
      }
    }
  }

  private void whitelist(List<Target> targets, int[] between, ExecutableObject object) {
    if (between.length > 0) {
      List<Target> allowed = new ArrayList<>();
      for (int targetIndex : between) {
        allowed.add(object.getTargetHistory(targetIndex));
      }
      for (Target targ : new ArrayList<>(targets)) {
        if (! allowed.contains(targ)) {
          targets.remove(targ);
        }
      }
    }
  }

  @Override
  public void execute(Board board, ExecutableObject object) throws NoTargetsException, NoTargetsExceptionOptional {


    List<Target> targets = getTargets(board, object);

    if (optional && object.targetHistoryContainsKey(target)) {
      throw new NoTargetsExceptionOptional("Optional SelectAction already used for this target");
    }

    if ((selectType == TargetType.ATTACK_TARGET
        || selectType == TargetType.ATTACK_ROOM
        || selectType == TargetType.ATTACK_SQUARE) && !optional && targets.isEmpty()) {
      throw new NoTargetsException("No targets available", ! object.didShoot());
    }

    if (optional && targets.isEmpty()) {
      throw new NoTargetsExceptionOptional("No targets available in optional SelectAction");
    }
    if (!object.getTargetHistory(from).isPlayer() && stopPropagation) {
      throw new NoTargetsExceptionOptional("No propagation allowed");
    }

    if (!optional || !targets.isEmpty()) {
      object.setCurrentSelectTargetSlot(target);
      try {
        object.getOwner().getClient().getBoardView().showTargetSelect(selectType, targets, skippable);
      } catch (RemoteException e) {
        Log.exception(e);
      }
    }
  }

  private List<Target> loadInitialTargets(Board board) {
    List<Target> targets = new ArrayList<>();
    switch (selectType) {
      case ATTACK_TARGET:
        targets.addAll(board.getShootablePlayers());
        if (board.isDominationBoard() && !disallowSpawnPoint) {
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

    return targets;
  }

  public List<Target> getTargets(Board board, ExecutableObject object) {
    Player owner = object.getOwner();
    List<Target> targets = loadInitialTargets(board);

    targets.remove(owner);

    Target fromTarget = object.getTargetHistory(from);

    // differentFrom
    blacklist(targets, differentFrom, object);

    // between
    whitelist(targets, between, object);
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
              fromTarget.getSquare().getCardinalDirection(x.getSquare()) == object.getLastUsageDirection() ||
                  fromTarget.getSquare().getCardinalDirection(x.getSquare()) == null;
        } catch (InvalidSquareException e) {
          return false;
        }
      });
    }

    // differentRoom
    if (differentRoom) {
      targetStream = targetStream.filter(x -> fromTarget.getSquare().getColor() != x.getSquare().getColor());
    }

    return targetStream.collect(Collectors.toList());
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

  public boolean isStopPropagation() {
    return stopPropagation;
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

  public boolean isSkippable() {
    return skippable;
  }

  public boolean isUseLastDirection() {
    return useLastDirection;
  }

  public boolean isDifferentRoom() {
    return differentRoom;
  }

  @Override
  public boolean isSync() {
    return true;
  }

  @Override
  public boolean equals(Object object) {
    return object instanceof WeaponAction && ((WeaponAction) object).getActionType() == WeaponActionType.SELECT
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
