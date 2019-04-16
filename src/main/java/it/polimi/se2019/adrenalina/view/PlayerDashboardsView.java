package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerUpdateEvent;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;
import java.util.ArrayList;
import java.util.List;

public class PlayerDashboardsView extends Observable implements PlayerDashboardsViewInterface, Observer {
  private final List<Player> players;

  public PlayerDashboardsView() {
    players = new ArrayList<>();
  }

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
  public void update(PlayerUpdateEvent event) {
    // TODO: update the status of the player dashboard
  }

  @Override
  public void update(Event event) {
    throw new WrongMethodTypeException();
  }
}
