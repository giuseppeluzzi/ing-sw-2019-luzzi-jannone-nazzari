package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Weapon;
import java.util.ArrayList;
import java.util.List;

public class OwnWeaponUpdate implements Event {

  private static final long serialVersionUID = -4678079189320950629L;
  private final List<Weapon> weapons;
  private final PlayerColor playerColor;

  public OwnWeaponUpdate(PlayerColor playerColor, List<Weapon> weapons) {
    this.playerColor = playerColor;
    this.weapons = new ArrayList<>();
    for (Weapon weapon : weapons) {
      Weapon newWeapon = new Weapon(weapon.getCost(AmmoColor.RED), weapon.getCost(AmmoColor.BLUE),
          weapon.getCost(AmmoColor.YELLOW), weapon.getBaseCost(), weapon.getName(), weapon.getSymbol());
      newWeapon.setLoaded(weapon.isLoaded());
      this.weapons.add(newWeapon);
    }
  }

  public List<Weapon> getWeapons() {
    return new ArrayList<>(weapons);
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_WEAPON_UPDATE;
  }

  @Override
  public PlayerColor getPrivatePlayerColor() {
    return getPlayerColor();
  }
}
