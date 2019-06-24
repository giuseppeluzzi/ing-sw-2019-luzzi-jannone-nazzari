package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Weapon;

import java.util.ArrayList;
import java.util.List;

/**
 * Invocation to show the weapon selection for reloading on the client.
 */
public class ShowReloadWeaponSelectionInvocation implements Invocation {

  private static final long serialVersionUID = 7930756269217335112L;
  private final List<Weapon> unloadedWeapons;

  public ShowReloadWeaponSelectionInvocation(
      List<Weapon> unloadedWeapons) {
    this.unloadedWeapons = new ArrayList<>(unloadedWeapons);
  }

  public List<Weapon> getUnloadedWeapons() {
    return new ArrayList<>(unloadedWeapons);
  }

  @Override
  public EventType getEventType() {
    return EventType.SHOW_RELOAD_WEAPON_SELECTION_INVOCATION;
  }
}
