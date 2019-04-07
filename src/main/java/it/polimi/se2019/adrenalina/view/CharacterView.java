package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerDeathEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerMoveEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerSpawnEvent;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;

public class CharacterView extends Observable implements Observer {
  private Player player;

  public CharacterView(Player player) {
    this.player = player;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public void setSelected(boolean selected) {
    // TODO: mark the character as selected
  }

  public void update(PlayerDeathEvent event) {
    // TODO:
  }

  public void update(PlayerSpawnEvent event) {
    // TODO:
  }

  public void update(PlayerMoveEvent event) {
    // TODO:
  }

  @Override
  public void update(Event event) {
    throw new WrongMethodTypeException();
  }
}
