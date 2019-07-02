package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Locale;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public abstract class DashboardFXController {

  private final BoardFXController boardFXController;
  private PlayerColor playerColor;

  protected DashboardFXController(BoardFXController boardFXController, PlayerColor playerColor) {
    this.boardFXController = boardFXController;
    this.playerColor = playerColor;
  }

  abstract HBox getDamagesContainer();
  abstract HBox getTagsContainer();
  abstract HBox getSkullsContainer();
  abstract FlowPane getAmmosContainer();

  abstract HBox getWeaponContainer();
  abstract HBox getPowerUpsContainer();

  abstract Pane generateTag(PlayerColor damage);
  abstract Node generateSkull();

  public void setPlayerColor(PlayerColor color) {
    playerColor = color;
    updateDashboard(color);
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public BoardFXController getBoardFXController() {
    return boardFXController;
  }

  public Pane generateAmmo(AmmoColor color) {
    Pane pane = new Pane();
    pane.getStyleClass().add("ammo");
    pane.getStyleClass().add("ammo-" + color.toString().toLowerCase(Locale.US));
    return pane;
  }

  public void updateDamages(List<PlayerColor> damages) {
    Platform.runLater(() -> {
      getDamagesContainer().getChildren().clear();
      for (PlayerColor damage : damages) {
        getDamagesContainer().getChildren().add(generateTag(damage));
      }
    });
  }

  public void updateTags(List<PlayerColor> tags) {
    Platform.runLater(() -> {
      getTagsContainer().getChildren().clear();
      for (PlayerColor tag : tags) {
        getTagsContainer().getChildren().add(generateTag(tag));
      }
    });
  }

  public void updateSkulls(int killScore) {
    Platform.runLater(() -> {
      getSkullsContainer().getChildren().clear();
      try {
        if (false/*AppGUI.getClient().getBoardView().getBoard().isFinalFrenzyActive()*/) {
          AppGUI.getClient().getBoardView();
          Node placeholder = generateSkull();
          placeholder.setOpacity(0);
          Node placeholder2 = generateSkull();
          placeholder2.setOpacity(0);
          getSkullsContainer().getChildren().add(placeholder);
          getSkullsContainer().getChildren().add(placeholder2);
          switch (killScore) {
            case -1:
              getSkullsContainer().getChildren().add(generateSkull());
              getSkullsContainer().getChildren().add(generateSkull());
              getSkullsContainer().getChildren().add(generateSkull());
              break;
            case 0:
              getSkullsContainer().getChildren().add(generateSkull());
              getSkullsContainer().getChildren().add(generateSkull());
              break;
            case 1:
              getSkullsContainer().getChildren().add(generateSkull());
              break;
            default:
              break;
          }
        } else {
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
      } catch (RemoteException e) {
        Log.exception(e);
      }
    });
  }

  public void updateAmmos(int red, int blue, int yellow) {
    Platform.runLater(() -> {
      getAmmosContainer().getChildren().clear();
      for (int i = 0; i < red; i++) {
        getAmmosContainer().getChildren().add(generateAmmo(AmmoColor.RED));
      }

      for (int i = 0; i < blue; i++) {
        getAmmosContainer().getChildren().add(generateAmmo(AmmoColor.BLUE));
      }

      for (int i = 0; i < yellow; i++) {
        getAmmosContainer().getChildren().add(generateAmmo(AmmoColor.YELLOW));
      }
    });
  }

  public abstract void updateWeapons(List<Weapon> weapons, int weaponsNum);
  public abstract void updatePowerUps(List<PowerUp> powerUps);
  public abstract void updateDashboard(PlayerColor color);

  public abstract void setFrenzy();
}
