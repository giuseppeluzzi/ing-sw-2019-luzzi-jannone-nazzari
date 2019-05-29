package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.invocations.ShowEffectSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowPaymentOptionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowPowerUpSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowTurnActionSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowWeaponSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.SwitchToFinalFrenzyInvocation;
import it.polimi.se2019.adrenalina.event.modelview.CurrentPlayerUpdate;
import it.polimi.se2019.adrenalina.event.modelview.EnemyPowerUpUpdate;
import it.polimi.se2019.adrenalina.event.modelview.EnemyWeaponUpdate;
import it.polimi.se2019.adrenalina.event.modelview.OwnPowerUpUpdate;
import it.polimi.se2019.adrenalina.event.modelview.OwnWeaponUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerAmmoUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerDamagesTagsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerKillScoreUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerScoreUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerStatusUpdate;
import it.polimi.se2019.adrenalina.model.BuyableType;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.VirtualClientSocket;
import it.polimi.se2019.adrenalina.utils.Observable;
import java.lang.invoke.WrongMethodTypeException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VirtualPlayerDashboardsView extends Observable implements
    PlayerDashboardsViewInterface {

  private static final long serialVersionUID = 4893523547038046745L;
  private final transient List<Player> players;
  private final transient VirtualClientSocket clientSocket;

  public VirtualPlayerDashboardsView(VirtualClientSocket clientSocket) {
    this.clientSocket = clientSocket;
    players = new ArrayList<>();
  }

  @Override
  public void addPlayer(Player player) {
    players.add(player);
    player.addObserver(this);
  }

  @Override
  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  @Override
  public void switchToFinalFrenzy(PlayerColor playerColor) {
    clientSocket.sendEvent(new SwitchToFinalFrenzyInvocation(playerColor));
  }

  @Override
  public void showPaymentOption(BuyableType buyableType, Map<AmmoColor, Integer> buyableCost,
      List<PowerUp> budgetPowerUp,
      Map<AmmoColor, Integer> budgetAmmo) {
    clientSocket.sendEvent(
        new ShowPaymentOptionInvocation(buyableType, buyableCost, budgetPowerUp, budgetAmmo));
  }

  @Override
  public void showWeaponSelection(List<Weapon> weapons) {
    clientSocket.sendEvent(new ShowWeaponSelectionInvocation(weapons));
  }

  @Override
  public void showEffectSelection(Weapon weapon) {
    clientSocket.sendEvent(new ShowEffectSelectionInvocation(weapon));
  }

  @Override
  public void showPowerUpSelection(List<PowerUp> powerUps) {
    HashMap<String, List<AmmoColor>> powerUpsMap = new HashMap<>();
    for (PowerUp powerUp : powerUps) {
      if (powerUpsMap.containsKey(powerUp.getName())) {
        List<AmmoColor> elems = new ArrayList<>(powerUpsMap.get(powerUp.getName()));
        elems.add(powerUp.getColor());
        powerUpsMap.put(powerUp.getName(), elems);
      } else {
        List<AmmoColor> elems = new ArrayList<>();
        elems.add(powerUp.getColor());
        powerUpsMap.put(powerUp.getName(), elems);
      }
    }
    clientSocket.sendEvent(new ShowPowerUpSelectionInvocation(powerUpsMap));
  }

  @Override
  public void showTurnActionSelection(List<TurnAction> actions) {
    clientSocket.sendEvent(new ShowTurnActionSelectionInvocation(actions));
  }

  @Override
  public void update(PlayerDamagesTagsUpdate event) throws RemoteException {
    clientSocket.sendEvent(event);
  }

  @Override
  public void update(PlayerScoreUpdate event) throws RemoteException {
    clientSocket.sendEvent(event);
  }

  @Override
  public void update(PlayerKillScoreUpdate event) throws RemoteException {
    clientSocket.sendEvent(event);
  }

  @Override
  public void update(PlayerStatusUpdate event) throws RemoteException {
    clientSocket.sendEvent(event);
  }

  @Override
  public void update(PlayerAmmoUpdate event) throws RemoteException {
    clientSocket.sendEvent(event);
  }

  @Override
  public void update(OwnWeaponUpdate event) throws RemoteException {
    if (clientSocket.getPlayerColor() == event.getPlayerColor()) {
      clientSocket.sendEvent(event);
    }
  }

  @Override
  public void update(EnemyWeaponUpdate event) throws RemoteException {
    if (clientSocket.getPlayerColor() != event.getPlayerColor()) {
      clientSocket.sendEvent(event);
    }

  }

  @Override
  public void update(EnemyPowerUpUpdate event) throws RemoteException {
    if (clientSocket.getPlayerColor() != event.getPlayerColor()) {
      clientSocket.sendEvent(event);
    }
  }

  @Override
  public void update(OwnPowerUpUpdate event) throws RemoteException {
    if (clientSocket.getPlayerColor() == event.getPlayerColor()) {
      clientSocket.sendEvent(event);
    }
  }

  @Override
  public void update(CurrentPlayerUpdate event) throws RemoteException {
    clientSocket.sendEvent(event);
  }

  @Override
  public void update(Event event) {
    // do nothing
  }
}
