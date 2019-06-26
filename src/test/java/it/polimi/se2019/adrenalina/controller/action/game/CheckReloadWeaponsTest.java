package it.polimi.se2019.adrenalina.controller.action.game;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Weapon;
import org.junit.Test;

public class CheckReloadWeaponsTest {

  @Test
  public void testGetReloadableWeapons() {
    Player player = new Player("test", PlayerColor.GREEN, null);
    Weapon weapon = new Weapon(0,0,0, AmmoColor.BLUE,"test","g");
    player.addWeapon(weapon);
    player.addAmmo(AmmoColor.BLUE, 1);
    weapon.setLoaded(true);
    CheckReloadWeapons checkReloadWeapons = new CheckReloadWeapons(null, player);
    assertEquals(0, checkReloadWeapons.getReloadableWeapons().size());
    weapon.setLoaded(false);
    assertEquals(1, checkReloadWeapons.getReloadableWeapons().size());
  }

}