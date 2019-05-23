package it.polimi.se2019.adrenalina.ui.text;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TUIUtils {

  private static final Scanner scanner = new Scanner(System.in, "utf-8");

  static Scanner getScanner() {
    return scanner;
  }

  static String selectWeapon(List<Weapon> weapons, String prompt,
      boolean showCost) {
    Log.print(prompt);

    int targetIndex;
    int chosenTarget;

    do {
      targetIndex = 1;
      for (Weapon weapon : weapons) {
        if (showCost) {
          Log.print(
              String.format("\t%d) %s%n  Costo: %d rosso, %d blu, %d giallo",
                  targetIndex,
                  weapon.getName(),
                  weapon.getCost(AmmoColor.RED),
                  weapon.getCost(AmmoColor.BLUE),
                  weapon.getCost(AmmoColor.YELLOW)));
        } else {
          Log.print(String.format("\t%d) %s", targetIndex, weapon.getName()));
        }
        targetIndex++;
      }
      chosenTarget = Character.getNumericValue(scanner.nextLine().charAt(0));
    } while (chosenTarget < 1 || chosenTarget >= targetIndex);

    return weapons.get(chosenTarget - 1).getName();
  }

  static AmmoColor showAmmoColorSelection(boolean anyAllowed) {
    int targetIndex;
    int chosenTarget;
    List<AmmoColor> colors;
    if (anyAllowed) {
      colors = Arrays.asList(AmmoColor.values());
    } else {
      colors = AmmoColor.getValidColor();
    }

    do {
      targetIndex = 1;
      Log.print("Seleziona un colore");
      for (AmmoColor color : colors) {
        Log.print("\t" + targetIndex + ") " + color);
        targetIndex++;
      }
      chosenTarget = Character.getNumericValue(scanner.nextLine().charAt(0));
    } while (chosenTarget < 1 || chosenTarget >= targetIndex);
    return colors.get(chosenTarget - 1);
  }

  static Effect showEffectSelection(List<Effect> effects, boolean areSubEffects) {
    Effect chosenEffect = null;
    int targetIndex;
    int chosenIndex;

    do {
      targetIndex = 1;
      if (areSubEffects) {
        Log.print("Ora scegli quali effetti secondari aggiungere");
      } else {
        Log.print("Scegli quale effetto usare");
      }
      for (Effect effect : effects) {
        Log.print("\t" + targetIndex + ") " + effect.getName());
        targetIndex++;
      }
      chosenIndex = scanner.nextLine().charAt(0);
      if (chosenIndex >= 0 && chosenIndex < targetIndex){
        chosenEffect = effects.get(chosenIndex);
      }
    } while (chosenEffect == null);

    return chosenEffect;
  }
}