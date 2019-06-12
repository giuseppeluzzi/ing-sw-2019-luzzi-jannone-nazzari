package it.polimi.se2019.adrenalina.ui.text;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.exceptions.InputCancelledException;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.ANSIColor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TUIUtils {

  private TUIUtils() {
    throw new IllegalStateException("Utility class");
  }

  private static final TUIInputManager inputManager = new TUIInputManager();

  static String selectWeapon(List<Weapon> weapons, String prompt,
      boolean showCost) throws InputCancelledException {
    List<String> choices = new ArrayList<>();
    for (Weapon weapon : weapons) {
      if (showCost) {
        choices.add(
            String.format("%s%n\t   Costo di ricarica: %s%d rosso%s, %s%d blu%s, %s%d giallo%s",
                weapon.getName(),
                AmmoColor.RED.getAnsiColor(),
                weapon.getCost(AmmoColor.RED),
                ANSIColor.RESET,
                AmmoColor.BLUE.getAnsiColor(),
                weapon.getCost(AmmoColor.BLUE),
                ANSIColor.RESET,
                AmmoColor.YELLOW.getAnsiColor(),
                weapon.getCost(AmmoColor.YELLOW),
                ANSIColor.RESET));
      } else {
        choices.add(String.format("%s", weapon.getName()));
      }
    }
    inputManager.input(prompt, choices);
    return weapons.get(inputManager.waitForIntResult()).getName();
  }

  static AmmoColor showAmmoColorSelection(boolean anyAllowed) throws InputCancelledException {
    List<AmmoColor> colors;
    if (anyAllowed) {
      colors = Arrays.asList(AmmoColor.values());
    } else {
      colors = AmmoColor.getValidColor();
    }
    List<String> choices = new ArrayList<>();
    for (AmmoColor color : colors) {
      choices.add(color.toString());
    }
    inputManager.input("Scegli un colore:", choices);
    return colors.get(inputManager.waitForIntResult());
  }

  static Effect showEffectSelection(List<Effect> effects, boolean areSubEffects) throws InputCancelledException {
    String prompt;
    int result;
    if (areSubEffects) {
      prompt = "Ora scegli quali effetti secondari aggiungere:";
    } else {
      prompt = "Scegli quale effetto usare:";
    }
    List<String> choices = new ArrayList<>();
    for (Effect effect : effects) {
      choices.add(effect.getName());
    }
    if (areSubEffects) {
      choices.add("Non usare nessun powerup");
    }
    inputManager.input(prompt, choices);
    result = inputManager.waitForIntResult();
    if (areSubEffects) {
      if (result >= effects.size()) {
        return null;
      }
    }
    return effects.get(result);
  }
}