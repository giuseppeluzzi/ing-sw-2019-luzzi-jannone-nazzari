package it.polimi.se2019.adrenalina.ui.graphic;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.model.BuyableType;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.view.BoardView;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsView;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class GUIPlayerDashboardsView extends PlayerDashboardsView {

  private static final long serialVersionUID = -1044436470709908758L;

  protected GUIPlayerDashboardsView(BoardView boardView) {
    super(boardView);
  }

  @Override
  public void switchToFinalFrenzy(PlayerColor playerColor) {

  }

  @Override
  public void showPaymentOption(BuyableType buyableType, Map<AmmoColor, Integer> buyableCost,
      List<PowerUp> budgetPowerUp, Map<AmmoColor, Integer> budgetAmmo) {

  }

  @Override
  public void showTurnActionSelection(List<TurnAction> actions) {

  }

  @Override
  public void showWeaponSelection(List<Weapon> weapons) {

  }

  @Override
  public void showEffectSelection(Weapon weapon, List<Effect> effects) {

  }

  @Override
  public void showUnsuspendPrompt() {

  }

  @Override
  public void showPowerUpSelection(List<PowerUp> powerUps, boolean discard) {

  }

  @Override
  public void showSwapWeaponSelection(List<Weapon> ownWeapons, List<Weapon> squareWeapons) {

  }

  @Override
  public void showReloadWeaponSelection(List<Weapon> unloadedWeapons) {

  }
}
