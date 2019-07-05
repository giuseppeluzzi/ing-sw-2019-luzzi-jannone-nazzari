package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Target;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class GUIPlayerTile implements GUITile {

  private final PlayerColor playerColor;
  private Circle playerIcon;
  private Circle playerIconHover;
  private final int x;
  private final int y;

  private EventHandler<MouseEvent> clickHandler;

  public GUIPlayerTile(int x, int y, PlayerColor playerColor, Circle playerIcon,
      Circle playerIconHover) {
    this.x = x;
    this.y = y;
    this.playerColor = playerColor;
    this.playerIcon = playerIcon;
    this.playerIconHover = playerIconHover;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
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

  public Circle getPlayerIconHover() {
    return playerIconHover;
  }

  public void setPlayerIconHover(Circle playerIconHover) {
    this.playerIconHover = playerIconHover;
  }

  public EventHandler<MouseEvent> getClickHandler() {
    return clickHandler;
  }

  public void setClickHandler(EventHandler<MouseEvent> clickHandler) {
    this.clickHandler = clickHandler;
  }

  @Override
  public boolean isTarget(Target target) {
    return target.isPlayer() && ((Player) target).getColor() == playerColor;
  }

  @Override
  public Node getTilePane() {
    return playerIcon;
  }

  @Override
  public Node getHoverPane() {
    return playerIconHover;
  }

  @Override
  public void enableTile() {
    playerIconHover.setVisible(false);
    playerIcon.getParent().getParent().getParent().getChildrenUnmodifiable().get(1)
        .setVisible(false); // hides hover of the square
  }

  @Override
  public void disableTile() {
    playerIconHover.setVisible(true);
  }
}
