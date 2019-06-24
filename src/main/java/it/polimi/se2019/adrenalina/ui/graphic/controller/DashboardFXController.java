package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import java.util.List;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public abstract class DashboardFXController {

  abstract HBox getDamagesContainer();
  abstract HBox getTagsContainer();
  abstract HBox getSkullsContainer();
  abstract FlowPane getAmmosContainer();

  abstract Pane generateTag(PlayerColor damage);
  abstract Pane generateSkull();

  public void updateDamages(List<PlayerColor> damages) {
    getDamagesContainer().getChildren().clear();
    for (PlayerColor damage : damages) {
      getDamagesContainer().getChildren().add(generateTag(damage));
    }
  }

  public void updateTags(List<PlayerColor> tags) {
    getTagsContainer().getChildren().clear();
    for (PlayerColor tag : tags) {
      getTagsContainer().getChildren().add(generateTag(tag));
    }
  }

  public void updateSkulls(int killScore) {
    getSkullsContainer().getChildren().clear();
    switch (killScore) {
      case 0:
        getSkullsContainer().getChildren().add(generateSkull());
        getSkullsContainer().getChildren().add(generateSkull());
        getSkullsContainer().getChildren().add(generateSkull());
        getSkullsContainer().getChildren().add(generateSkull());
        getSkullsContainer().getChildren().add(generateSkull());
        break;
      case 1:
        getSkullsContainer().getChildren().add(generateSkull());
        getSkullsContainer().getChildren().add(generateSkull());
        getSkullsContainer().getChildren().add(generateSkull());
        getSkullsContainer().getChildren().add(generateSkull());
        break;
      case 2:
        getSkullsContainer().getChildren().add(generateSkull());
        getSkullsContainer().getChildren().add(generateSkull());
        getSkullsContainer().getChildren().add(generateSkull());
        break;
      case 4:
        getSkullsContainer().getChildren().add(generateSkull());
        getSkullsContainer().getChildren().add(generateSkull());
        break;
      case 6:
        getSkullsContainer().getChildren().add(generateSkull());
        break;
      default:
        break;
    }
  }
}
