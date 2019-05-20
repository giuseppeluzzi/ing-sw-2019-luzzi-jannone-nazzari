package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerDeathEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerSpawnEvent;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;
import java.util.ArrayList;
import java.util.List;

public class CharactersView extends Observable implements CharactersViewInterface, Observer {

  private static final long serialVersionUID = 3820277997554969634L;
  private final ArrayList<Player> players;
  private Player selectedPlayer;

  public CharactersView() {
    players = new ArrayList<>();
  }

  @Override
  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  @Override
  public void addPlayer(Player player) {
    players.add(player);
  }

  @Override
  public void setSelected(Player player) {
    selectedPlayer = player;
  }

  @Override
  public void removeSelected() {
    selectedPlayer = null;
  }

  @Override
  public void update(PlayerDeathEvent event) {
    // TODO: handle the death of a character
  }

  @Override
  public void update(PlayerSpawnEvent event) {
    // TODO: handle the respawn of a character
  }

  @Override
  public void update(Event event) {
    throw new WrongMethodTypeException();
  }
}
