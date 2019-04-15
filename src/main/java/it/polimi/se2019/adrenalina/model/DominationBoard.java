package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

/**
 * Class describing a Board during a Domination match.
 * Three different List represent the different spawn points damages
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
   * Copy constructor
   * @param dominationBoard DominationBoard object that has to be copied, can't be null
   * @param publicCopy boolean value indicating if the copy should be private or public.
   * If true a public copy will be made containg only public informations
   * @exception IllegalArgumentException thrown if dominationBoard is null
   */
  public DominationBoard(DominationBoard dominationBoard, boolean publicCopy) {
    // TODO: try/catch with specific exception message
    super(dominationBoard, publicCopy);
    if (dominationBoard == null) {
      throw new IllegalArgumentException("Argument dominationBoard can't be null");
    }
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
    Gson gson = new Gson();
    return gson.fromJson(json, DominationBoard.class);
  }
}
