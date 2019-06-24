package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Weapon;

import java.util.ArrayList;
import java.util.List;

/**
 * Invocation to print the weapon selection on the client.
 */
public class ShowWeaponSelectionInvocation implements Invocation {

  private static final long serialVersionUID = -5167233035070943885L;
  private final List<Weapon> weapons;

  public ShowWeaponSelectionInvocation(
      List<Weapon> weapons) {
    this.weapons = new ArrayList<>(weapons);
  }

  public List<Weapon> getWeapons() {
    return new ArrayList<>(weapons);
  }

  @Override
  public EventType getEventType() {
    return EventType.SHOW_WEAPON_SELECTION_INVOCATION;
  }
}
