package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.Configuration;
import it.polimi.se2019.adrenalina.utils.Constants;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

  public static final String WRONG_FIELD_CSS = "wrong-field";
  private boolean choosenDomination;
  private String chosenIpAddress;
  private int chosenPort;
  private String choosenName;
  private boolean choosenRMI;

  @FXML
  private BorderPane startServerSelection;
  @FXML
  private BorderPane startGameModeSelector;
  @FXML
  private RadioButton gamemodeDomination;

  @FXML
  private BorderPane startNameSelector;
  @FXML
  private Text serverConfigTitle;
  @FXML
  private Text textNameTitle;
  @FXML
  private TextField textFieldName;
  @FXML
  private CheckBox checkBoxRMI;
  @FXML
  private TextField textFieldIpAddress;
  @FXML
  private TextField textFieldPort;

  @FXML
  private Button buttonNext0;
  @FXML
  private Button buttonNext1;
  @FXML
  private Button buttonNext2;

  public void initialize() {
    startGameModeSelector.setVisible(false);
    startNameSelector.setVisible(false);
    startServerSelection.setVisible(true);

    textFieldIpAddress.setText(Configuration.getInstance().getServerIP());
    textFieldPort.setText(Integer.toString(Configuration.getInstance().getSocketPort()));
    buttonNext0.requestFocus();

    checkBoxRMI.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != oldValue) {
        if (newValue) {
          textFieldPort.setText(Integer.toString(Configuration.getInstance().getRmiPort()));
        } else {
          textFieldPort.setText(Integer.toString(Configuration.getInstance().getSocketPort()));
        }
      }
    });
  }

  public void next0(ActionEvent actionEvent) {
    chosenIpAddress = textFieldIpAddress.getText().trim();

    if (! chosenIpAddress.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$")) {
      textFieldIpAddress.getStyleClass().add(WRONG_FIELD_CSS);
      textFieldIpAddress.requestFocus();
      return;
    }

    String chosenPortTxt = textFieldPort.getText().trim();

    if (chosenPortTxt.matches("^\\d{1,5}$")) {
      chosenPort = Integer.parseInt(chosenPortTxt);
    } else {
      textFieldPort.getStyleClass().add(WRONG_FIELD_CSS);
      textFieldPort.requestFocus();
      return;
    }

    choosenRMI = checkBoxRMI.isSelected();

    FXUtils.transition(startServerSelection, startGameModeSelector);

    buttonNext1.requestFocus();
  }

  public void next1(ActionEvent actionEvent) {
    choosenDomination = gamemodeDomination.isSelected();

    if (choosenDomination) {
      textNameTitle.setText("Modalità Dominazione");
    } else {
      textNameTitle.setText("Modalità Classica");
    }

    FXUtils.transition(startGameModeSelector, startNameSelector);

    textFieldName.requestFocus();
  }

  public void next2(ActionEvent actionEvent) {
    choosenName = textFieldName.getText().trim();

    if (choosenName.isEmpty() || choosenName.length() >= Constants.MAX_NAME_LENGTH) {
      textFieldName.getStyleClass().add(WRONG_FIELD_CSS);
      textFieldName.requestFocus();
      return;
    }

    FXUtils.transition(startNameSelector, null);

    TranslateTransition transOut = new TranslateTransition(
        Duration.millis(FXUtils.TRANSLATE_DURATION),
        startNameSelector);
    transOut.setToX(-FXUtils.TRANSLATE_OUT);
    transOut.play();

    transOut.setOnFinished(actionEvent1 -> {
      AppGUI.startClient(chosenIpAddress, chosenPort, choosenName, choosenDomination, !choosenRMI);
      Scene lobbyScene;

      lobbyScene = AppGUI.getLobbyScene();

      lobbyScene.getStylesheets().add(AppGUI.getCSS());

      ((Stage) startNameSelector.getScene().getWindow()).setScene(lobbyScene);
    });

  }
}
