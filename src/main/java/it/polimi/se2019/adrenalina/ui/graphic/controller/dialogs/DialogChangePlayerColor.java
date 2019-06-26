package it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerColorSelectionEvent;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import javafx.css.Styleable;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DialogChangePlayerColor extends Dialog {

  private ToggleGroup colorToggleGroup;

  @FXML
  private HBox charactersHBox;
  @FXML
  private Button buttonContinue;

  public DialogChangePlayerColor() {
    super("Seleziona un personaggio", true);
  }

  public void initialize() {
    buttonContinue.setOnAction(event -> {
      PlayerColor chosenColor = PlayerColor
          .valueOf(((Styleable) colorToggleGroup.getSelectedToggle()).getId().replace("ch_", ""));
      try {
        if (AppGUI.getClient().getBoardView().getBoard().getFreePlayerColors()
            .contains(chosenColor)) {
          build();
        } else {
            ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(
                new PlayerColorSelectionEvent(AppGUI.getClient().getPlayerColor(), chosenColor));

          close();
        }
      } catch (RemoteException e) {
        Log.exception(e);
      }
    });
  }

  @Override
  public void build() {

    if (charactersHBox != null) {
      charactersHBox.getChildren().clear();
    }

    colorToggleGroup = new ToggleGroup();
    List<PlayerColor> colors = new ArrayList<>(Arrays.asList(PlayerColor.values()));

    try {
      for (Player player : AppGUI.getClient().getBoardView().getBoard().getPlayers()) {
        colors.remove(player.getColor());
      }
    } catch (RemoteException e) {
      Log.exception(e);
    }

    boolean first = true;

    for (PlayerColor color : colors) {
      VBox vBox = new VBox();
      vBox.setAlignment(Pos.CENTER);
      vBox.setSpacing(25);
      vBox.setPrefHeight(200);
      vBox.setPrefWidth(100);
      Circle circle = new Circle(100);
      circle.setFill(
          new ImagePattern(new Image("gui/assets/img/character_" + color + ".jpg"), -100, -100, 200,
              200, false));
      circle.setStroke(Color.BLACK);
      circle.setStrokeWidth(5);
      RadioButton radioButton = new RadioButton(color.getCharacterName());
      radioButton.setId("ch_" + color);
      radioButton.setToggleGroup(colorToggleGroup);
      if (first) {
        radioButton.setSelected(true);
        first = false;
      }
      vBox.getChildren().add(circle);
      vBox.getChildren().add(radioButton);
      if (charactersHBox != null) {
        charactersHBox.getChildren().add(vBox);
      }
    }
  }
}
