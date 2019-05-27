package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.PlayerDeathEvent;
import it.polimi.se2019.adrenalina.event.PlayerSpawnEvent;
import it.polimi.se2019.adrenalina.event.invocations.ShowDeath;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.network.VirtualClientSocket;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;
import java.util.ArrayList;
import java.util.List;

public class VirtualCharactersView extends Observable implements CharactersViewInterface, Observer {

  private static final long serialVersionUID = -6715889122608916050L;
  private final transient ArrayList<Player> players;
  private final transient VirtualClientSocket clientSocket;

  public VirtualCharactersView(VirtualClientSocket clientSocket) {
    this.clientSocket = clientSocket;
    players = new ArrayList<>();
  }

  @Override
  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  @Override
  public void addPlayer(Player player) {
    players.add(player);
    player.addObserver(this);
  }

  @Override
  public void setSelected(PlayerColor playerColor) {
    // TODO: Show selected player
  }

  @Override
  public void removeSelected() {
    // TODO: Remove selected Player
  }

  @Override
  public void showDeath(PlayerColor playerColor) {
    clientSocket.sendEvent(new ShowDeath(playerColor));
  }

  @Override
  public void update(PlayerDeathEvent event) {
    // TODO: handle the death of a character
    clientSocket.sendEvent(event);
  }

  @Override
  public void update(PlayerSpawnEvent event) {
    // TODO: handle the respawn of a character
    clientSocket.sendEvent(event);
  }

  @Override
  public void update(Event event) {
    throw new WrongMethodTypeException();
  }
}
