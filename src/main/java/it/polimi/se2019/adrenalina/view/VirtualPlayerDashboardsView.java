package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.network.VirtualClientSocket;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;
import java.util.ArrayList;
import java.util.List;

public class VirtualPlayerDashboardsView extends Observable implements PlayerDashboardsViewInterface, Observer {
  private final List<Player> players;
  private final VirtualClientSocket clientSocket;

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
