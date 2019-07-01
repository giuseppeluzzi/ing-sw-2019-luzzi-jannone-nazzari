package it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerSelectWeaponEffectEvent;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import javafx.css.Styleable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogEffectSelection extends Dialog {

  public static final String END_OF_TURN = "(fine del turno)";
  private ToggleGroup effectSelectionGroup;
  private Weapon weapon;
  private List<Effect> effects;
  private Map<String, Boolean> effectStatus = new HashMap<>();
  private List<String> chosenEffects = new ArrayList<>();
  private ComboBox<String> comboBox;
  private Effect anyTimeEffect;

  @FXML
  private VBox effectsVBox;
  @FXML
  private VBox subEffectsVBox;
  @FXML
  private VBox anyTimeVBox;
  @FXML
  private Button buttonNext;

  public DialogEffectSelection() {
    super("Selezione effetti", false);
  }

  public void setWeapon(Weapon weapon) {
    this.weapon = weapon;
  }

  public void setEffects(List<Effect> effects) {
    this.effects = new ArrayList<>(effects);
  }

  @Override
  public void build() {
    effectSelectionGroup = new ToggleGroup();
    int index = 0;
    for (Effect effect : effects) {
      RadioButton button = new RadioButton();
      button.setId(Integer.toString(index));
      button.setToggleGroup(effectSelectionGroup);
      button.selectedProperty().addListener((observable, oldValue, newValue) -> {
        if (oldValue != newValue) {
          effectStatus.put(effect.getName(), newValue);
          rebuildSubEffects();
        }
      });
      if (index == 0) {
        button.setSelected(true);
        chosenEffects.add(effect.getName());
      }
      HBox hbox = new HBox();
      hbox.getChildren().add(button);
      hbox.getChildren().add(getLabel(effect));
      effectsVBox.getChildren().add(hbox);
      index++;
    }
    rebuildSubEffects();

    buttonNext.setOnAction(event -> handleConfirmation());
  }

  private void handleConfirmation() {
    Integer anyTimeIndex = null;
    if (comboBox != null) {
      String anyTimeEffectBefore = comboBox.getSelectionModel().getSelectedItem();
      if (END_OF_TURN.equals(anyTimeEffectBefore)) {
        anyTimeIndex = chosenEffects.size();
      } else {
        Effect effectBefore = getEffectByName(anyTimeEffectBefore);
        if (effectBefore != null) {
          anyTimeIndex = chosenEffects.indexOf(anyTimeEffectBefore);
        } else {
          Alert alert = new Alert(Alert.AlertType.WARNING, String.format("Scegli quando vuoi usare l'effetto \"%s\"", anyTimeEffect.getName()));
          Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
          stage.setAlwaysOnTop(true);
          alert.showAndWait();
          return;
        }
      }
    }

    List<String> output = new ArrayList<>();
    for (int i = 0; i < chosenEffects.size(); i++) {
      if (anyTimeIndex != null && i == anyTimeIndex) {
        output.add(anyTimeEffect.getName());
      }
      output.add(chosenEffects.get(i));
    }
    if (anyTimeIndex != null && anyTimeIndex == chosenEffects.size()) {
      output.add(anyTimeEffect.getName());
    }

    try {
      ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(
              new PlayerSelectWeaponEffectEvent(AppGUI.getClient().getPlayerColor(), weapon.getName(), output));
    } catch (RemoteException e) {
      Log.exception(e);
    }

    close();
  }

  private Effect getEffectByName(String name) {
    for (Effect effect : effects) {
      if (effect.getName().equals(name)) {
        return effect;
      } else {
        Effect subEffect = effect.getSubEffectByName(name);
        if (subEffect != null) {
          return subEffect;
        }
      }
    }
    return null;
  }

  private TextFlow getLabel(Effect effect) {
    if (effect.getCost().get(AmmoColor.BLUE) == 0 && effect.getCost().get(AmmoColor.RED) == 0 && effect.getCost().get(AmmoColor.YELLOW) == 0) {
      Text text = new Text(effect.getName());
      text.getStyleClass().add("text");
      return new TextFlow(text);
    }
    TextFlow output = new TextFlow(new Text(String.format("%s (costo: ", effect.getName())));
    Text blueText = new Text(String.format("%s blu", effect.getCost(AmmoColor.BLUE)));
    blueText.setFill(Color.BLUE);
    output.getChildren().add(blueText);
    output.getChildren().add(new Text(", "));
    Text redText = new Text(String.format("%s rosso", effect.getCost(AmmoColor.RED)));
    redText.setFill(Color.RED);
    output.getChildren().add(redText);
    output.getChildren().add(new Text(", "));
    Text yellowText = new Text(String.format("%s giallo", effect.getCost(AmmoColor.YELLOW)));
    yellowText.setFill(Color.ORANGE);
    output.getChildren().add(yellowText);
    output.getChildren().add(new Text(")"));
    output.getStyleClass().add("text");
    return output;
  }

  private void addSubEffects(Effect effect) {
    if (effectStatus.get(effect.getName()) == null || ! effectStatus.get(effect.getName())) {
      return;
    }
    for (Effect subEffect : effect.getSubEffects()) {
      CheckBox checkBox = new CheckBox();
      checkBox.setId(subEffect.getName().replace(" ", "_"));
      if (effectStatus.get(subEffect.getName()) != null && effectStatus.get(subEffect.getName())) {
        checkBox.setSelected(true);
      }
      checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
        if (oldValue != newValue) {
          effectStatus.put(subEffect.getName(), newValue);
          rebuildSubEffects();
        }
      });
      HBox hbox = new HBox();
      hbox.getChildren().add(checkBox);
      hbox.getChildren().add(getLabel(subEffect));
      subEffectsVBox.getChildren().add(hbox);
      addSubEffects(subEffect);
    }
  }

  private void rebuildSubEffects() {
    Effect mainEffect =  effects.get(Integer.parseInt(((Styleable) effectSelectionGroup.getSelectedToggle()).getId()));
    subEffectsVBox.getChildren().clear();
    anyTimeVBox.getChildren().clear();
    addSubEffects(mainEffect);
    chosenEffects.clear();
    chosenEffects.add(mainEffect.getName());
    for (Node node : subEffectsVBox.getChildren()) {
      CheckBox checkBox = (CheckBox) ((Pane) node).getChildren().get(0);
      if (checkBox.isSelected()) {
        chosenEffects.add(checkBox.getId().replace("_", " "));
      }
    }
    comboBox = new ComboBox<>();
    comboBox.setMinWidth(250);
    comboBox.setPromptText("Seleziona un effetto...");
    boolean showAnyTime = false;
    for (String effectName : new ArrayList<>(chosenEffects)) {
      Effect effect = getEffectByName(effectName);
      if (effect != null && effect.isAnyTime()) {
        showAnyTime = true;
        anyTimeEffect = effect;
        chosenEffects.remove(effect.getName());
      } else if (effect != null) {
        comboBox.getItems().add(effectName);
      }
    }
    if (showAnyTime) {
      comboBox.getItems().add(END_OF_TURN);
      VBox vbox = new VBox();
      vbox.setSpacing(7);
      vbox.getChildren().add(new Label(String.format("Usa \"%s\" prima di:", anyTimeEffect.getName())));
      vbox.getChildren().add(comboBox);
      anyTimeVBox.getChildren().add(vbox);
    } else {
      anyTimeVBox.getChildren().clear();
      comboBox = null;
    }
  }
}

