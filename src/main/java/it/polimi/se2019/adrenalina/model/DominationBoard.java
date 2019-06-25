package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.modelview.DominationBoardDamagesUpdate;
import it.polimi.se2019.adrenalina.utils.Log;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * A Board during a Domination match.
 * The damages to each spawn point are contained in three lists.
 */
public class DominationBoard extends Board {

  private static final long serialVersionUID = -5497312114295479630L;
  private List<PlayerColor> blueDamages;
  private List<PlayerColor> redDamages;
  private List<PlayerColor> yellowDamages;

  public DominationBoard() {
    blueDamages = new ArrayList<>();
    redDamages = new ArrayList<>();
    yellowDamages = new ArrayList<>();
  }

  /**
   * Copy constructor, creates an exact copy of a DominationBoard.
   * @param dominationBoard the DominationBoard to be cloned, has to be not
   * null
   * @param publicCopy if true, a public copy of the DominationBoard will be
   * created instead of a clone. The public copy will not contain players'
   * private information
   */
  public DominationBoard(DominationBoard dominationBoard, boolean publicCopy) {
    // TODO: try/catch with specific exception message
    super(dominationBoard, publicCopy);
    blueDamages = dominationBoard.getBlueDamages();
    redDamages = dominationBoard.getRedDamages();
    yellowDamages = dominationBoard.getYellowDamages();
  }

  /**
   * Adds a blue damage to the Board.
   * @param color the color of the Player who scored the damage
   */
  public void addBlueDamage(PlayerColor color) {
    blueDamages.add(color);
    try {
      notifyObservers(new DominationBoardDamagesUpdate(AmmoColor.BLUE, getBlueDamages()));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Returns a List of Players who scored blue damages.
   * @return a List of Players who scored blue damages
   */
  public List<PlayerColor> getBlueDamages() {
    return new ArrayList<>(blueDamages);
  }

  /**
   * Adds a red damage to the Board.
   * @param color the color of the Player who scored the damage
   */
  public void addRedDamage(PlayerColor color) {
    redDamages.add(color);
    try {
      notifyObservers(new DominationBoardDamagesUpdate(AmmoColor.RED, getRedDamages()));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Returns a List of Players who scored red damages.
   * @return a List of Players who scored red damages
   */
  public List<PlayerColor> getRedDamages() {
    return new ArrayList<>(redDamages);
  }

  /**
   * Adds a yellow damage to the Board.
   * @param color the color of the Player who scored the damage
   */
  public void addYellowDamage(PlayerColor color) {
    yellowDamages.add(color);
    try {
      notifyObservers(new DominationBoardDamagesUpdate(AmmoColor.YELLOW, getYellowDamages()));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Adds damages to the spawnPoint whose color matches the given one
   * @param color AmmoColor for the spawnpoint
   * @param playerColor the color of the player who scored the damage
   */
  public void addDamage(AmmoColor color, PlayerColor playerColor) {
    switch (color) {
      case BLUE:
        addBlueDamage(playerColor);
        break;
      case RED:
        addRedDamage(playerColor);
        break;
      case YELLOW:
        addYellowDamage(playerColor);
        break;
      case ANY:
        break;
    }
  }

  /**
   * Returns a List of Players who scored yellow damages.
   * @return a List of Players who scored yellow damages
   */
  public List<PlayerColor> getYellowDamages() {
    return new ArrayList<>(yellowDamages);
  }

  /**
   * Updates the list of damages of a specified color.
   * @param color the color of the track to update
   * @param damages the new list of damages that will replace the current one
   */
  public void updateDamages(AmmoColor color, List<PlayerColor> damages) {
    if (color == AmmoColor.BLUE) {
      blueDamages = new ArrayList<>(damages);
    } else if (color == AmmoColor.RED) {
      redDamages = new ArrayList<>(damages);
    } else if (color == AmmoColor.YELLOW) {
      yellowDamages = new ArrayList<>(damages);
    }
  }

  /**
   * Returns whether the Board is a DominationBoard. Always true here, always false in the parent
   * class {@code Board}.
   * @return false, since this is a DominationBoard and not a normal Board
   */
  @Override
  public boolean isDominationBoard() {
    return true;
  }

  /**
   * Creates DominationBoard object from a JSON serialized object.
   * @param json JSON input String
   * @return DominationBoard object
   */
  public static DominationBoard deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, DominationBoard.class);
  }
}
