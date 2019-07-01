package it.polimi.se2019.adrenalina.ui.graphic;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.event.modelview.EnemyPowerUpUpdate;
import it.polimi.se2019.adrenalina.event.modelview.EnemyWeaponUpdate;
import it.polimi.se2019.adrenalina.event.modelview.OwnPowerUpUpdate;
import it.polimi.se2019.adrenalina.event.modelview.OwnWeaponUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerAmmoUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerDamagesTagsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerFrenzyUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerKillScoreUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerScoreUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerStatusUpdate;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.BuyableType;
import it.polimi.se2019.adrenalina.model.Newton;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.ui.graphic.controller.DashboardFXController;
import it.polimi.se2019.adrenalina.ui.graphic.controller.PlayerDashboardFXController;
import it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs.DialogEffectSelection;
import it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs.DialogReloadWeaponSelection;
import it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs.DialogShowPaymentOption;
import it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs.DialogSwapWeaponSelection;
import it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs.DialogUnsuspend;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import it.polimi.se2019.adrenalina.view.BoardViewInterface;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsView;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;

public class GUIPlayerDashboardsView extends PlayerDashboardsView {

  private static final long serialVersionUID = -1044436470709908758L;

  public GUIPlayerDashboardsView(BoardViewInterface boardView) {
    super((BoardView) boardView);
  }

  @Override
  public void update(PlayerDamagesTagsUpdate event) {
    super.update(event);
    try {
      AppGUI.getBoardFXController().getDashboardController(event.getPlayerColor())
          .updateDamages(event.getDamages());
      AppGUI.getBoardFXController().getDashboardController(event.getPlayerColor())
          .updateTags(event.getTags());
    } catch (InvalidPlayerException ignored) {
      //
    }
  }

  @Override
  public void update(PlayerScoreUpdate event) {
    super.update(event);
    // TODO ?
  }

  @Override
  public void update(PlayerKillScoreUpdate event) {
    super.update(event);
    try {
      AppGUI.getBoardFXController().getDashboardController(event.getPlayerColor())
          .updateSkulls(event.getKillScore());
    } catch (InvalidPlayerException ignored) {
      //
    }
  }

  @Override
  public void update(PlayerStatusUpdate event) {
    super.update(event);
    // TODO ?
  }

  @Override
  public void update(PlayerAmmoUpdate event) {
    super.update(event);
    try {
      AppGUI.getBoardFXController().getDashboardController(event.getPlayerColor())
          .updateAmmos(event.getRed(), event.getBlue(), event.getYellow());
    } catch (InvalidPlayerException ignored) {
      //
    }
  }

  @Override
  public void update(OwnWeaponUpdate event) {
    super.update(event);
    DashboardFXController dashboard = null;

    try {
      dashboard = AppGUI.getBoardFXController().getDashboardController(event.getPlayerColor());
    } catch (InvalidPlayerException ignored) {
      return;
    }

    dashboard.updateWeapons(event.getWeapons(), event.getWeapons().size());
  }

  @Override
  public void update(EnemyWeaponUpdate event) {
    super.update(event);
    DashboardFXController dashboard = null;

    try {
      if (event.getPlayerColor() == AppGUI.getClient().getPlayerColor()) {
        return;
      }
    } catch (RemoteException e) {
      Log.exception(e);
    }

    try {
      dashboard = AppGUI.getBoardFXController().getDashboardController(event.getPlayerColor());
    } catch (InvalidPlayerException ignored) {
      return;
    }

    dashboard.updateWeapons(event.getUnloadedWeapons(), event.getNumWeapons());
  }

  @Override
  public void update(OwnPowerUpUpdate event) {
    super.update(event);
    DashboardFXController dashboard = null;

    try {
      dashboard = AppGUI.getBoardFXController()
          .getDashboardController(event.getPlayerColor());
    } catch (InvalidPlayerException ignored) {
      return;
    }

    dashboard.updatePowerUps(event.getPowerUps());
  }

  @Override
  public void update(EnemyPowerUpUpdate event) {
    super.update(event);
    DashboardFXController dashboard = null;

    try {
      if (event.getPlayerColor() == AppGUI.getClient().getPlayerColor()) {
        return;
      }
    } catch (RemoteException e) {
      Log.exception(e);
    }

    try {
      dashboard = AppGUI.getBoardFXController()
          .getDashboardController(event.getPlayerColor());
    } catch (InvalidPlayerException ignored) {
      return;
    }

    List<PowerUp> fakePowerUps = new ArrayList<>();
    for (int i = 0; i < event.getPowerUpsNum(); i++) {
      fakePowerUps.add(new Newton(AmmoColor.RED));
    }
    dashboard.updatePowerUps(fakePowerUps);
  }

  @Override
  public void switchToFinalFrenzy(PlayerColor playerColor) {
    try {
      AppGUI.getBoardFXController().getDashboardController(playerColor).setFrenzy();
    } catch (InvalidPlayerException ignored) {
      //
    }
  }

  @Override
  public void showPaymentOption(BuyableType buyableType, String prompt,
      Map<AmmoColor, Integer> buyableCost,
      List<PowerUp> budgetPowerUp, Map<AmmoColor, Integer> budgetAmmo) {
    DialogShowPaymentOption dialog = new DialogShowPaymentOption();
    dialog.setBuyableCost(buyableCost);
    dialog.setBudgetPowerUp(budgetPowerUp);
    dialog.setBudgetAmmo(budgetAmmo);
    dialog.show();
  }

  @Override
  public void showTurnActionSelection(List<TurnAction> actions) {
    AppGUI.getBoardFXController().showTurnActions(actions);

  }

  @Override
  public void showWeaponSelection(List<Weapon> weapons) {
    final PlayerDashboardFXController playerDashboardFXController = AppGUI
        .getPlayerDashboardFXController();

    Platform.runLater(playerDashboardFXController::usingWeapon);
  }

  @Override
  public void showEffectSelection(Weapon weapon, List<Effect> effects) {
    final DialogEffectSelection dialogEffectSelection = new DialogEffectSelection();
    dialogEffectSelection.setWeapon(weapon);
    dialogEffectSelection.setEffects(effects);
    dialogEffectSelection.show();
  }

  @Override
  public void showUnsuspendPrompt() {
    AppGUI.getBoardFXController().reset();
    new DialogUnsuspend().show();
  }

  @Override
  public void showPowerUpSelection(List<PowerUp> powerUps, boolean discard) {
    final PlayerDashboardFXController playerDashboardFXController = AppGUI
        .getPlayerDashboardFXController();

    Platform.runLater(() -> playerDashboardFXController.usingPowerUp(discard));
  }

  @Override
  public void showSwapWeaponSelection(List<Weapon> ownWeapons, List<Weapon> squareWeapons) {
    final DialogSwapWeaponSelection dialogSwapWeaponSelection = new DialogSwapWeaponSelection();
    dialogSwapWeaponSelection.setSwappableWeapons(ownWeapons);
    dialogSwapWeaponSelection.setPickableWeapons(squareWeapons);
  }

  @Override
  public void showReloadWeaponSelection(List<Weapon> unloadedWeapons) {
    final DialogReloadWeaponSelection dialogReloadWeaponSelection = new DialogReloadWeaponSelection();
    dialogReloadWeaponSelection.setWeapons(unloadedWeapons);
    dialogReloadWeaponSelection.show();
  }
}
