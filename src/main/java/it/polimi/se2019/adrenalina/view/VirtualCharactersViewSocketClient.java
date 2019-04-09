package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerDeathEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerMoveEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerSpawnEvent;
import it.polimi.se2019.adrenalina.model.Player;
import java.lang.invoke.WrongMethodTypeException;

public class VirtualCharactersViewSocketClient extends CharactersView {

  @Override
  public void setSelected(Player player) {
    // TODO:
  }

  @Override
  public void removeSelected() {
    // TODO:
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
  public void update(PlayerMoveEvent event) {
    // TODO: handle the movement of a character on a board
  }

  @Override
  public void update(Event event) {
    throw new WrongMethodTypeException();
  }

}
