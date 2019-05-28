package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.invocations.ShowEffectSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowPaymentOptionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowPowerUpSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowWeaponSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.SwitchToFinalFrenzyInvocation;
import it.polimi.se2019.adrenalina.model.Buyable;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.VirtualClientSocket;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VirtualPlayerDashboardsView extends Observable implements
    PlayerDashboardsViewInterface, Observer {

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
  public void showPaymentOption(Buyable item) {
    clientSocket.sendEvent(new ShowPaymentOptionInvocation(item));
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
    // TODO
  }

  @Override
  public void update(Event event) {
    throw new WrongMethodTypeException();
  }
}
