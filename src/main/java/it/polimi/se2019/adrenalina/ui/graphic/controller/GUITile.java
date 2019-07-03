package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.model.Target;
import javafx.scene.Node;

public interface GUITile {

  boolean isTarget(Target target);

  Node getTilePane();

  Node getHoverPane();

  void enableTile();

  void disableTile();

}
