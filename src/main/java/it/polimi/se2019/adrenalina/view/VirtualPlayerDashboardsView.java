package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.invocations.*;
import it.polimi.se2019.adrenalina.model.BuyableType;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.VirtualClientSocket;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observable;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Virtual player dashboards view, relays invocations over the network to the actual player dashboards view.
 */
public class VirtualPlayerDashboardsView extends Observable implements
    PlayerDashboardsViewInterface {

  private static final long serialVersionUID = 4893523547038046745L;
  private final transient VirtualClientSocket clientSocket;

  public VirtualPlayerDashboardsView(VirtualClientSocket clientSocket) {
    this.clientSocket = clientSocket;
  }

  @Override
  public void switchToFinalFrenzy(PlayerColor playerColor) {
    clientSocket.sendEvent(new SwitchToFinalFrenzyInvocation(playerColor));
  }

  @Override
  public void showPaymentOption(BuyableType buyableType, String prompt, Map<AmmoColor, Integer> buyableCost,
      List<PowerUp> budgetPowerUp,
      Map<AmmoColor, Integer> budgetAmmo) {
    clientSocket.sendEvent(
        new ShowPaymentOptionInvocation(buyableType, prompt, buyableCost, budgetPowerUp, budgetAmmo));
  }

  @Override
  public void showWeaponSelection(List<Weapon> weapons) {
    clientSocket.sendEvent(new ShowWeaponSelectionInvocation(weapons));
  }

  @Override
  public void showEffectSelection(Weapon weapon, List<Effect> effects) {
    List<Effect> strippedEffects = new ArrayList<>();
    for (Effect effect : effects) {
      strippedEffects.add(new Effect(effect, true));
    }
    clientSocket.sendEvent(new ShowEffectSelectionInvocation(weapon, strippedEffects));
  }

  @Override
  public void showPowerUpSelection(List<PowerUp> powerUps, boolean discard) {
    clientSocket.sendEvent(new ShowPowerUpSelectionInvocation(powerUps, discard));
  }

  @Override
  public void showTurnActionSelection(List<TurnAction> actions) {
    clientSocket.sendEvent(new ShowTurnActionSelectionInvocation(actions));
  }

  @Override
  public void showSwapWeaponSelection(List<Weapon> ownWeapons, List<Weapon> squareWeapons) {
    clientSocket.sendEvent(new ShowSwapWeaponSelectionInvocation(ownWeapons, squareWeapons));
  }

  @Override
  public void showReloadWeaponSelection(List<Weapon> unloadedWeapons) {
    clientSocket.sendEvent(new ShowReloadWeaponSelectionInvocation(unloadedWeapons));
  }
    
  public void showUnsuspendPrompt() {
    clientSocket.sendEvent(new ShowUnsuspendPromptInvocation());
  }

  @Override
  public void update(Event event) {
    try {
      if (getHandledEvents().contains(event.getEventType())) {
        clientSocket.sendEvent(event);
      }
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  @Override
  public PlayerColor getPrivatePlayerColor() {
    return clientSocket.getPlayerColor();
  }
}
