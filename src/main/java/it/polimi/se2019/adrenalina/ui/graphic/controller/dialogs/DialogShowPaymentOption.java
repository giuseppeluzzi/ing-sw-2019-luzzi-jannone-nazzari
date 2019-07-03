package it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs;


import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.action.game.Payment;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerPaymentEvent;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Spendable;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class DialogShowPaymentOption extends Dialog {

  @FXML
  private BorderPane borderPane;
  @FXML
  private Button buttonContinue;
  @FXML
  private TextFlow textTop;
  @FXML
  private TextFlow subtitleTop;
  @FXML
  private Text title;

  private Map<AmmoColor, Integer> buyableCost;
  private List<PowerUp> budgetPowerUp;
  private Map<AmmoColor, Integer> budgetAmmo;
  private List<Spendable> spendables;
  private FlowPane flowPane;

  public DialogShowPaymentOption() {
    super("Scegli come pagare", false);
  }

  /**
   * Initilize method, sets the onAction event that is executed when buttonContinue is pressed.
   */
  public void initialize() {
    buttonContinue.setOnAction(event -> {
      Set<String> answers = new HashSet<>();
      for (int i = 0; i < flowPane.getChildren().size(); i++) {
        if (((CheckBox) flowPane.getChildren().get(i)).isSelected()) {
          answers.add(Integer.toString(i + 1));
        }
      }

      spendables = Player.setSpendable(budgetPowerUp, budgetAmmo);

      if (!Payment.verifyPaymentAnswers(answers, spendables) ||
          !Payment.verifyPaymentFullfilled(answers, spendables, buyableCost)) {
        Alert alert = new Alert(AlertType.WARNING, "La selezione non è corretta");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        alert.showAndWait();
      } else {
        handlePaymentFullfilled(answers);
      }
    });
  }

  private void handlePaymentFullfilled(Set<String> answersStrings) {
    int answerRed = 0;
    int answerBlue = 0;
    int answerYellow = 0;
    List<Integer> answers = new ArrayList<>();

    for (String answer : answersStrings) {
      answers.add(Integer.valueOf(answer));
    }

    List<PowerUp> answerPowerUp = new ArrayList<>();
    for (Integer element : answers) {
      if (spendables.get(element).isPowerUp()) {
        answerPowerUp.add((PowerUp) spendables.get(element));
      } else {
        if (spendables.get(element).getColor() == AmmoColor.RED) {
          answerRed++;
        } else if (spendables.get(element).getColor() == AmmoColor.BLUE) {
          answerBlue++;
        } else if (spendables.get(element).getColor() == AmmoColor.YELLOW) {
          answerYellow++;
        }
      }
    }
    try {
      ((BoardView) AppGUI.getClient().getBoardView())
          .sendEvent(new PlayerPaymentEvent(AppGUI.getClient().getPlayerColor(),
              answerRed, answerBlue, answerYellow, answerPowerUp));
      close();
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }


  public void setBudgetAmmo(Map<AmmoColor, Integer> budgetAmmo) {
    this.budgetAmmo = new EnumMap<>(budgetAmmo);
  }

  public void setBudgetPowerUp(List<PowerUp> budgetPowerUp) {
    this.budgetPowerUp = new ArrayList<>(budgetPowerUp);
  }

  public void setBuyableCost(Map<AmmoColor, Integer> buyableCost) {
    this.buyableCost = new EnumMap<>(buyableCost);
  }

  /**
   * Build method that creates a checkbox for every ammo and powerup possesed by the user
   */
  @Override
  public void build() {
    flowPane = new FlowPane();
    flowPane.setOrientation(Orientation.VERTICAL);
    flowPane.setPrefWrapLength(100);
    flowPane.setHgap(50);
    flowPane.setVgap(5);

    if (budgetAmmo.containsKey(AmmoColor.BLUE)) {
      for (int i = 0; i < budgetAmmo.get(AmmoColor.BLUE); i++) {
        CheckBox checkBox = new CheckBox("Munizione blu");
        checkBox.setTextFill(Color.BLUE);
        flowPane.getChildren().add(checkBox);
      }
    }
    if (budgetAmmo.containsKey(AmmoColor.RED)) {
      for (int i = 0; i < budgetAmmo.get(AmmoColor.RED); i++) {
        CheckBox checkBox = new CheckBox("Munizione rossa");
        checkBox.setTextFill(Color.RED);
        flowPane.getChildren().add(checkBox);
      }
    }
    if (budgetAmmo.containsKey(AmmoColor.YELLOW)) {
      for (int i = 0; i < budgetAmmo.get(AmmoColor.YELLOW); i++) {
        CheckBox checkBox = new CheckBox("Munizione gialla");
        checkBox.setTextFill(Color.ORANGE);
        flowPane.getChildren().add(checkBox);
      }
    }

    if (!budgetPowerUp.isEmpty()) {
      buildPowerUps();
    }

    borderPane.setCenter(flowPane);
    Text redText = new Text(buyableCost.get(AmmoColor.RED) + " rosso");
    Text bluText = new Text(buyableCost.get(AmmoColor.BLUE) + " blu");
    Text yellowText = new Text(buyableCost.get(AmmoColor.YELLOW) + " giallo");
    Text anyText = new Text(buyableCost.get(AmmoColor.ANY) + " qualsiasi colore");
    redText.setFill(Color.RED);
    bluText.setFill(Color.BLUE);
    yellowText.setFill(Color.ORANGE);
    subtitleTop.getChildren()
        .addAll(redText, new Text(", "), bluText, new Text(", "), yellowText, new Text(", "),
            anyText);
  }

  private void buildPowerUps() {
    for (PowerUp powerUp : budgetPowerUp) {
      CheckBox checkBox = new CheckBox(powerUp.getName() + " " + powerUp.getColor());
      if (powerUp.getColor() == AmmoColor.BLUE) {
        checkBox.setTextFill(Color.BLUE);
      } else if (powerUp.getColor() == AmmoColor.YELLOW) {
        checkBox.setTextFill(Color.ORANGE);
      } else if (powerUp.getColor() == AmmoColor.RED) {
        checkBox.setTextFill(Color.RED);
      }
      flowPane.getChildren().add(checkBox);
    }
  }
}
