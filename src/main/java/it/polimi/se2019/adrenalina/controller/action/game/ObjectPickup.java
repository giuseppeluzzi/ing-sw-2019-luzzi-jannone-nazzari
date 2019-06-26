package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.exceptions.InvalidPowerUpException;
import it.polimi.se2019.adrenalina.model.AmmoCard;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Actino used to pick up ammoCards and weapons.
 */
public class ObjectPickup extends GameAction {

  public ObjectPickup(Player player) {
    super(player);
  }

  @Override
  public void execute(Board board) {
    if (getPlayer().getSquare().isSpawnPoint()) {
      List<Weapon> buyableWeapons = getBuyableWeapons();
      if (buyableWeapons.isEmpty()) {
        return;
      }

      if (getPlayer().getWeapons().size() == 3) {
        try {
          getPlayer().getClient().getPlayerDashboardsView()
              .showSwapWeaponSelection(getPlayer().getWeapons(), buyableWeapons);
        } catch (RemoteException e) {
          Log.exception(e);
        }
      } else {
        try {
          getPlayer().getClient().getBoardView().showBuyableWeapons(buyableWeapons);
        } catch (RemoteException e) {
          Log.exception(e);
        }
      }
    } else {
      fetchAmmoCard(board);
    }
  }

  private void fetchAmmoCard(Board board) {
    AmmoCard ammoCard = getPlayer().getSquare().getAmmoCard();

    getPlayer().addAmmo(AmmoColor.RED, ammoCard.getAmmo(AmmoColor.RED));
    getPlayer().addAmmo(AmmoColor.BLUE, ammoCard.getAmmo(AmmoColor.BLUE));
    getPlayer().addAmmo(AmmoColor.YELLOW, ammoCard.getAmmo(AmmoColor.YELLOW));

    for (int i = 0; i < ammoCard.getPowerUp(); i++) {
      PowerUp powerUp = board.getPowerUps().get(0);
      board.drawPowerUp(powerUp);
      try {
        getPlayer().addPowerUp(powerUp);
      } catch (InvalidPowerUpException e) {
        board.undrawPowerUp(powerUp);
        break;
      }
    }
    getPlayer().getSquare().setAmmoCard(null);
  }

  public List<Weapon> getBuyableWeapons() {
    List<Weapon> buyableWeapons = new ArrayList<>();
    for (Weapon weapon : getPlayer().getSquare().getWeapons()) {
      if (getPlayer().canCollectWeapon(weapon)) {
        buyableWeapons.add(weapon);
      }
    }
    return buyableWeapons;
  }

  @Override
  public boolean isSync() {
    return getPlayer().getSquare().isSpawnPoint() && !getBuyableWeapons().isEmpty();
  }
}