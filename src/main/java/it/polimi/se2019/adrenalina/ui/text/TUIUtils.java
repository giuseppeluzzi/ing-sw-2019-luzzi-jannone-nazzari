package it.polimi.se2019.adrenalina.ui.text;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.exceptions.InputCancelledException;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.ANSIColor;
import it.polimi.se2019.adrenalina.utils.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Additional utils needed by Text User Interface.
 */
public class TUIUtils {

  private TUIUtils() {
    throw new IllegalStateException("Utility class");
  }

  private static final TUIInputManager inputManager = new TUIInputManager();

  /**
   * Weapon selection prompt
   * @param weapons the list of weapons to choose from
   * @param prompt the prompt
   * @param showCost whether to show the cost of each weapon
   * @return the user's answer
   * @throws InputCancelledException thrown if the user's input is cancelled
   */
  static String selectWeapon(List<Weapon> weapons, String prompt,
      boolean showCost) throws InputCancelledException {
    List<String> choices = new ArrayList<>();
    for (Weapon weapon : weapons) {
      if (showCost) {
        choices.add(
            String.format("%s, costo di ricarica: %s%d rosso%s, %s%d blu%s, %s%d giallo%s",
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

  /**
   * AmmoColor selection prompt
   * @param anyAllowed whether "ANY" is an option
   * @return the chosen ammoColor
   * @throws InputCancelledException thrown if the user's input is cancelled
   */
  static AmmoColor showAmmoColorSelection(boolean anyAllowed, Map<AmmoColor, Integer> damages)
      throws InputCancelledException {

    List<AmmoColor> colors;
    if (anyAllowed) {
      colors = Arrays.asList(AmmoColor.values());
    } else {
      colors = AmmoColor.getValidColor();
    }

    List<String> choices = new ArrayList<>();

    for (AmmoColor color : colors) {
      choices.add(color.getAnsiColor() + color.toString() + ANSIColor.RESET + " (danni ricevuti: " + damages.get(color) + ")");
    }

    inputManager.input("Scegli un colore:", choices);
    return colors.get(inputManager.waitForIntResult());
  }

  /**
   * Verify that the combination of selected effects is valid by checking dependencies.
   * @param answers the users selections as string numbers
   * @param choices the list of choices
   * @param effects the list of effects to choose from
   * @return true if the combination of selected effects is valid, false otherwise
   */
  private static boolean verifyValidEffects(List<String> answers, List<String> choices, List<Effect> effects) {
    if (answers.contains(Integer.toString(choices.size())) && answers.size() != 1) {
      Log.println("La combinazione di eventi scelta non è valida");
      return false;
    }
    return true;
  }

  /**
   * Effect selection prompt
   * @param effects list of effects to choose from
   * @param areSubEffects whether the selection is about a subeffect
   * @return the selected effect
   * @throws InputCancelledException thrown if the user's input is cancelled
   */
  static List<Effect> showEffectSelection(List<Effect> effects, boolean areSubEffects) throws InputCancelledException {
    List<String> choices = new ArrayList<>();
    for (Effect effect : effects) {
      choices.add(effect.getName());
    }
    List<Effect> output = new ArrayList<>();
    if (areSubEffects) {
      choices.add("Non aggiungere nessun effetto");
      int index = 1;
      for (String choice : choices) {
        Log.println(String.format("\t%d) %s", index, choice));
        index++;
      }
      String response;
      List<String> answers;
      String inputValidationRegex = "^(\\d+(,\\d+)*)?$";
      do {
        inputManager.input("Inserisci i numeri degli effetti secondari da aggiungere separati da virgola:");
        try {
          response = inputManager.waitForStringResult().replace(" ", "");
        } catch (InputCancelledException e) {
          return new ArrayList<>();
        }

        answers = Arrays.asList(response.split(","));
      } while (!response.matches(inputValidationRegex)
              || !verifyValidEffects(answers, choices, effects)
      );

      if (answers.contains(Integer.toString(index - 1))) {
        return new ArrayList<>();
      }
      for (String element : answers) {
        output.add(effects.get(Integer.parseInt(element) - 1));
      }
      return output;
    } else {
      inputManager.input("Scegli quale effetto usare:", choices);
      int result = inputManager.waitForIntResult();
      output.add(effects.get(result));
      return output;
    }
  }

  /**
   * Asks the user when to use an anyTime effect.
   * @param anyTimeEffect the anyTime effect
   * @param chosenEffects the list of confirmed effects
   * @return the index of when to use the anyTime effect
   * @throws InputCancelledException if the user input is cancelled
   */
  static int askAnyTimeIndex(Effect anyTimeEffect, List<Effect> chosenEffects) throws InputCancelledException {
    List<String> choices = new ArrayList<>();
    for (Effect effect : chosenEffects) {
      choices.add(String.format("Prima di \"%s\"", effect.getName()));
    }
    choices.add("Dopo tutti i precedenti");
    inputManager.input(String.format("Scegli quando vuoi usare l'effetto \"%s\":", anyTimeEffect.getName()), choices);
    return inputManager.waitForIntResult();
  }

  /**
   * Cancel ongoing user input.
   */
  static void cancelInput() {
    inputManager.cancel("Tempo scaduto!");
  }
}