package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class GUIPlayerTile {

  private final PlayerColor playerColor;
  private Circle playerIcon;

  private EventHandler<MouseEvent> clickHandler;

  public GUIPlayerTile(PlayerColor playerColor, Circle playerIcon) {
    this.playerColor = playerColor;
    this.playerIcon = playerIcon;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public Circle getPlayerIcon() {
    return playerIcon;
  }

  public void setPlayerIcon(Circle playerIcon) {
    this.playerIcon = playerIcon;
  }

  public EventHandler<MouseEvent> getClickHandler() {
    return clickHandler;
  }

  public void setClickHandler(EventHandler<MouseEvent> clickHandler) {
    this.clickHandler = clickHandler;
  }
}
