package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerPaymentEvent;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract class PlayerDashboardsView extends Observable implements PlayerDashboardsViewInterface, Observer {

  private static final long serialVersionUID = -6150690431150041388L;
  private final List<Player> players;

  protected PlayerDashboardsView() {
    players = new ArrayList<>();
  }

  public Player getPlayerByColor(PlayerColor playerColor) {
    for (Player player : getPlayers()) {
      if (player.getColor() == playerColor) {
        return player;
      }
    }
    return null;
  }

  @Override
  public abstract void showPaymentOption(int blue, int red, int yellow, int any);

  @Override
  public abstract void showTurnActionSelection(List<TurnAction> actions);

  @Override
  public abstract void showWeaponSelect(List<Weapon> weapons);

  @Override
  public void addPlayer(Player player) {
    players.add(player);
  }

  @Override
  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  @Override
  public void reset(Player player) {
    // TODO: reset the player dashboard
  }

  @Override
  public void switchToFinalFrenzy(Player player) {
    // TODO: change dashboard to final frenzy mode
  }

  @Override
  public void update(Event event) {
    throw new WrongMethodTypeException();
  }
}
