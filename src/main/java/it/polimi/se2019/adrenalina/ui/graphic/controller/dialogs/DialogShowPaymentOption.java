package it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs;


import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerPaymentEvent;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Spendable;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
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
   * Initilize method, sets the onAction event that is executed
   * when buttonContinue is pressed.
   */
  public void initialize() {
    buttonContinue.setOnAction(event -> {
      int answerRed = 0;
      int answerBlue = 0;
      int answerYellow = 0;
      List<PowerUp> answerPowerUp = new ArrayList<>();
      List<Integer> answers = new ArrayList<>();
      for (int i = 0; i < flowPane.getChildren().size(); i++) {
        if (((CheckBox) flowPane.getChildren().get(i)).isSelected()) {
          answers.add(i);
        }
      }

      spendables = setSpendable(budgetPowerUp, budgetAmmo);
      if (verifyPaymentFullfilled(answers, spendables, buyableCost)) {
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
          AppGUI.getClient().getPlayerDashboardsView()
              .update(new PlayerPaymentEvent(AppGUI.getClient().getPlayerColor(),
                  answerRed, answerBlue, answerYellow, answerPowerUp));
        } catch (RemoteException e) {
          Log.exception(e);
        }
      } else {
        Alert alert = new Alert(AlertType.WARNING, "La selezione non è corretta");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        alert.showAndWait();
      }
    });
  }

  private List<Spendable> setSpendable(List<PowerUp> powerUps,
      Map<AmmoColor, Integer> budgetAmmo) {
    List<Spendable> spendables = new ArrayList<>();
    int index = 0;
    int redAmmo = budgetAmmo.get(AmmoColor.RED);
    int blueAmmo = budgetAmmo.get(AmmoColor.BLUE);
    int yellowAmmo = budgetAmmo.get(AmmoColor.YELLOW);

    for (int i = 0; i < blueAmmo; i++) {
      spendables.add(index, AmmoColor.BLUE);
      index++;
    }
    for (int i = 0; i < redAmmo; i++) {
      spendables.add(index, AmmoColor.RED);
      index++;
    }
    for (int i = 0; i < yellowAmmo; i++) {
      spendables.add(index, AmmoColor.YELLOW);
      index++;
    }
    for (PowerUp powerUp : powerUps) {
      spendables.add(index, powerUp);
      index++;
    }
    return spendables;
  }

  private static boolean verifyPaymentFullfilled(List<Integer> answers, List<Spendable> spendables,
      Map<AmmoColor, Integer> costs) {

    int blueCost = costs.get(AmmoColor.BLUE);
    int redCost = costs.get(AmmoColor.RED);
    int yellowCost = costs.get(AmmoColor.YELLOW);
    int anyCost = costs.get(AmmoColor.ANY);

    for (Integer answer : answers) {
      switch (spendables.get(answer).getColor()) {
        case BLUE:
          if (blueCost > 0) {
            blueCost--;
          } else if (anyCost > 0) {
            anyCost--;
          }
          break;
        case RED:
          if (redCost > 0) {
            redCost--;
          } else if (anyCost > 0) {
            anyCost--;
          }
          break;
        case YELLOW:
          if (yellowCost > 0) {
            yellowCost--;
          } else if (anyCost > 0) {
            anyCost--;
          }
          break;
        default:
          throw new IllegalStateException("Illegal spendable color");
      }
    }

    return blueCost + redCost + yellowCost + anyCost == 0;
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
   * Build method that creates a checkbox for every ammo and powerup
   * possesed by the user
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

    if (! budgetPowerUp.isEmpty()) {
      int j = 0;
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
        j++;
      }
    }

    borderPane.setCenter(flowPane);
    Text redText =  new Text(buyableCost.get(AmmoColor.RED) + " rosso");
    Text bluText = new Text(buyableCost.get(AmmoColor.BLUE) + " blu");
    Text yellowText = new Text(buyableCost.get(AmmoColor.YELLOW) + " giallo");
    Text anyText = new Text(buyableCost.get(AmmoColor.ANY) + " qualsiasi colore");
    redText.setFill(Color.RED);
    bluText.setFill(Color.BLUE);
    yellowText.setFill(Color.ORANGE);
    subtitleTop.getChildren().addAll(redText, new Text(", "), bluText, new Text(", "), yellowText, new Text(", "), anyText);
  }
}
