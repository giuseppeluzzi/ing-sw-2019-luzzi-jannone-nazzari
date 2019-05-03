package it.polimi.se2019.adrenalina.controller.action;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectAction implements Action {

  private int from;
  private int target;
  private int minDistance;
  private int maxDistance = -1;
  private int[] differentFrom = {};
  private int[] between = {};
  private boolean visible = true;
  private boolean optional;
  private boolean useLastDirection;
  private boolean differentRoom;
  private TargetType selectType = TargetType.BOTH;
  private boolean untilVisible;
  private ActionType type = ActionType.SELECT;

  public SelectAction(int from, int target, int minDistance,
      int maxDistance, int[] differentFrom, int[] between,
      boolean visible, boolean optional, boolean useLastDirection,
      boolean differentRoom, TargetType selectType, boolean untilVisible) {

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
    this.untilVisible = untilVisible;
    type = ActionType.SELECT;
  }

  @Override
  public ActionType getActionType() {
    return type;
  }

  @Override
  public void execute(Weapon weapon) {
    // TODO: show selection
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

  public boolean isVisible() {
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
        && ((SelectAction) object).useLastDirection == useLastDirection;
  }

  @Override
  public int hashCode() {
    return differentFrom.length + Arrays.hashCode(differentFrom) + between.length +
        Arrays.hashCode(between) + from + minDistance + maxDistance + target + type.ordinal();
  }
}
