package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;
import java.util.ArrayList;
import java.util.List;

public abstract class CharactersView extends Observable implements CharactersViewInterface, Observer {

  private static final long serialVersionUID = 3820277997554969634L;
  private final ArrayList<Player> players;

  protected CharactersView() {
    players = new ArrayList<>();
  }

  @Override
  public List<Player> getPlayers() {
    return new ArrayList<>(players);
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
  public void addPlayer(Player player) {
    players.add(player);
  }

  @Override
  public void setSelected(PlayerColor playerColor) {
    // TODO: Show selected player
  }

  @Override
  public void removeSelected() {
    // TODO: Remove selected player
  }

  @Override
  public abstract void showDeath(PlayerColor playerColor);

  @Override
  public void update(Event event) {
    throw new WrongMethodTypeException();
  }
}
