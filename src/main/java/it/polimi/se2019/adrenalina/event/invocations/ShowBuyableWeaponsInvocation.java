package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Weapon;
import java.util.ArrayList;
import java.util.List;

/**
 * Invocation to show a list of buyable weapons on the client.
 */
public class ShowBuyableWeaponsInvocation implements Invocation {

  private static final long serialVersionUID = -3599685014999260921L;
  private final List<Weapon> weaponList;

  public ShowBuyableWeaponsInvocation(
      List<Weapon> weaponList) {
    this.weaponList = new ArrayList<>(weaponList);
  }

  public List<Weapon> getWeaponList() {
    return new ArrayList<>(weaponList);
  }

  @Override
  public EventType getEventType() {
    return EventType.SHOW_BUYABLE_WEAPONS_INVOCATION;
  }
}
