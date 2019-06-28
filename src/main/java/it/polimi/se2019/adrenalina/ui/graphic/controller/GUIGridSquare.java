package it.polimi.se2019.adrenalina.ui.graphic.controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

public class GUIGridSquare {

  private final int x;
  private final int y;

  private TilePane tilePane;
  private Pane hoverPane;

  private EventHandler<MouseEvent> clickHandler;

  public GUIGridSquare(int x, int y, TilePane tilePane, Pane hoverPane) {
    this.x = x;
    this.y = y;
    this.tilePane = tilePane;
    this.hoverPane = hoverPane;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public TilePane getTilePane() {
    return tilePane;
  }

  public void setTilePane(TilePane tilePane) {
    this.tilePane = tilePane;
  }

  public Pane getHoverPane() {
    return hoverPane;
  }

  public void setHoverPane(Pane hoverPane) {
    this.hoverPane = hoverPane;
  }

  public EventHandler<MouseEvent> getClickHandler() {
    return clickHandler;
  }

  public void setClickHandler(EventHandler<MouseEvent> clickHandler) {
    this.clickHandler = clickHandler;
  }
}
