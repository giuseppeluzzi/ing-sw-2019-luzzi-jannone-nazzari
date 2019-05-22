package it.polimi.se2019.adrenalina.ui.text;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import java.util.List;
import java.util.Scanner;

public class TUIUtils {

  public static String selectWeapon(List<Weapon> weapons, Scanner scanner, String prompt, boolean showCost) {
    Log.print(prompt);

    int targetIndex = 0;
    int chosenTarget = 0;

    do {
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
}
