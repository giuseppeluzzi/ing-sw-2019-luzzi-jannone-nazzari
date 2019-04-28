package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import java.util.ArrayList;
import java.util.List;

/**
 * A Board during a Domination match.
 * The damages to each spawn point are contained in three lists.
 */
public class DominationBoard extends Board {
  private final List<PlayerColor> blueDamages;
  private final List<PlayerColor> redDamages;
  private final List<PlayerColor> yellowDamages;

  public DominationBoard() {
    blueDamages = new ArrayList<>();
    redDamages = new ArrayList<>();
    yellowDamages = new ArrayList<>();
  }

  /**
   * Copy constructor, creates an exact copy of a DominationBoard.
   * @param dominationBoard the DominationBoard to be cloned, has to be not
   * null.
   * @param publicCopy if true, a public copy of the DominationBoard will be
   * created instead of a clone. The public copy will not contain players'
   * private information.
   */
  public DominationBoard(DominationBoard dominationBoard, boolean publicCopy) {
    // TODO: try/catch with specific exception message
    super(dominationBoard, publicCopy);
    blueDamages = dominationBoard.getBlueDamages();
    redDamages = dominationBoard.getRedDamages();
    yellowDamages = dominationBoard.getYellowDamages();
  }

  public void addBlueDamage(PlayerColor color) {
    blueDamages.add(color);
  }

  public List<PlayerColor> getBlueDamages() {
    return new ArrayList<>(blueDamages);
  }

  public void addRedDamage(PlayerColor color) {
    redDamages.add(color);
  }

  public List<PlayerColor> getRedDamages() {
    return new ArrayList<>(redDamages);
  }

  public void addYellowDamage(PlayerColor color) {
    yellowDamages.add(color);
  }

  public List<PlayerColor> getYellowDamages() {
    return new ArrayList<>(yellowDamages);
  }

  @Override
  public boolean isDominationBoard() {
    return true;
  }

  /**
   * Create DominationBoard object from json formatted String
   * @param json json input String
   * @return DominationBoard
   */
  public static DominationBoard deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, DominationBoard.class);
  }
}
