package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Weapon;
import java.util.ArrayList;
import java.util.List;

/**
 * Invocation that has the client show the weapon selection for swapping weapons.
 * @see Invocation
 */
public class ShowSwapWeaponSelectionInvocation implements Invocation {

  private static final long serialVersionUID = 3322915967810343204L;
  private final List<Weapon> ownWeapons;
  private final List<Weapon> squareWeapons;

  public ShowSwapWeaponSelectionInvocation(
      List<Weapon> ownWeapons, List<Weapon> squareWeapons) {
    this.ownWeapons = new ArrayList<>(ownWeapons);
    this.squareWeapons = new ArrayList<>(squareWeapons);
  }

  public List<Weapon> getOwnWeapons() {
    return new ArrayList<>(ownWeapons);
  }

  public List<Weapon> getSquareWeapons() {
    return new ArrayList<>(squareWeapons);
  }

  @Override
  public EventType getEventType() {
    return EventType.SHOW_SWAP_WEAPON_SELECTION_INVOCATION;
  }
}
