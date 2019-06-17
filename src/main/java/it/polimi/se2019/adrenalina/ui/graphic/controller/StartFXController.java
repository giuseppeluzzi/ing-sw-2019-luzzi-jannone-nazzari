package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.utils.Constants;
import it.polimi.se2019.adrenalina.utils.Log;
import java.io.IOException;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StartFXController {

  private static final int TRANSLATE_OUT = 2000;
  private static final int TRANSLATE_DURATION = 500;

  private boolean choosenDomination;
  private String choosenName;
  private boolean choosenRMI;

  @FXML
  private BorderPane startGameModeSelector;
  @FXML
  private RadioButton gamemodeDomination;

  @FXML
  private BorderPane startNameSelector;
  @FXML
  private Text textNameTitle;
  @FXML
  private TextField textFieldName;
  @FXML
  private CheckBox checkBoxRMI;

  @FXML
  private Button buttonNext1;
  @FXML
  private Button buttonNext2;

  public void initialize() {
    startGameModeSelector.setVisible(true);
    startNameSelector.setVisible(false);

    buttonNext1.requestFocus();
  }

  public void next1(ActionEvent actionEvent) {
    choosenDomination = gamemodeDomination.isSelected();

    TranslateTransition transOut = new TranslateTransition(Duration.millis(TRANSLATE_DURATION),
        startGameModeSelector);
    transOut.setToX(-TRANSLATE_OUT);
    transOut.play();

    double initialX = startNameSelector.getLayoutX();
    startNameSelector.setTranslateX(TRANSLATE_OUT);
    startNameSelector.setVisible(true);
    if (choosenDomination) {
      textNameTitle.setText("Modalità Dominazione");
    } else {
      textNameTitle.setText("Modalità Classica");
    }

    TranslateTransition transIn = new TranslateTransition(Duration.millis(TRANSLATE_DURATION),
        startNameSelector);
    transIn.setToX(initialX);
    transIn.play();

    textFieldName.requestFocus();
  }

  public void next2(ActionEvent actionEvent) {
    choosenName = textFieldName.getText().trim();
    choosenRMI = checkBoxRMI.isSelected();

    if (choosenName.isEmpty() || choosenName.length() >= Constants.MAX_NAME_LENGTH) {
      textFieldName.getStyleClass().add("wrong-field");
      textFieldName.requestFocus();
      return ;
    }

    TranslateTransition transOut = new TranslateTransition(Duration.millis(TRANSLATE_DURATION),
        startNameSelector);
    transOut.setToX(-TRANSLATE_OUT);
    transOut.play();

    transOut.setOnFinished(actionEvent1 -> {
      Scene lobbyScene;

      FXMLLoader loaderLobby = new FXMLLoader(
          AppGUI.class.getClassLoader().getResource("gui/Lobby.fxml"));

      try {
        lobbyScene = new Scene(loaderLobby.load());
      } catch (IOException e) {
        Log.critical("Lobby scene not found");
        return;
      }

      lobbyScene.getStylesheets().add(AppGUI.getCSS());

      ((Stage) startNameSelector.getScene().getWindow()).setScene(lobbyScene);
      AppGUI.startClient(choosenName, choosenDomination, !choosenRMI);
    });

  }
}
