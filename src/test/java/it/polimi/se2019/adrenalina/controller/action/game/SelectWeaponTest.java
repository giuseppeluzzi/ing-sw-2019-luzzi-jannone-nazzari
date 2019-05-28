package it.polimi.se2019.adrenalina.controller.action.game;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Weapon;
import org.junit.Test;

public class SelectWeaponTest {
  @Test
  public void testGetLoadedWeapons() {
    Player player = new Player("test", PlayerColor.GREEN, null);
    player.addWeapon(new Weapon(0,1,1, AmmoColor.RED, "test1"));
    player.addWeapon(new Weapon(1,1,1, AmmoColor.BLUE, "test2"));
    player.getWeapons().get(1).setLoaded(false);
    player.getWeapons().get(0).setLoaded(true);
    assertEquals(player.getWeapons().get(0), SelectWeapon.getLoadedWeapons(player.getWeapons()).get(0));
  }
}