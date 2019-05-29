package it.polimi.se2019.adrenalina.ui.text;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerActionSelectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerCollectWeaponEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerDiscardPowerUpEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerPaymentEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerSelectWeaponEffectEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerSelectWeaponEvent;
import it.polimi.se2019.adrenalina.model.BuyableType;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Spendable;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.ANSIColor;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import it.polimi.se2019.adrenalina.view.BoardViewInterface;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsView;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class TUIPlayerDashboardsView extends PlayerDashboardsView {

  private static final long serialVersionUID = 572470044324855920L;
  private final transient ClientInterface client;
  private final transient Scanner scanner = new Scanner(System.in, "utf-8");
  private final BoardViewInterface boardView;

  public TUIPlayerDashboardsView(ClientInterface client, BoardViewInterface boardView) {
    super((BoardView) boardView);
    this.client = client;
    this.boardView = boardView;
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

  @Override
  public void switchToFinalFrenzy(PlayerColor playerColor) {
    // TODO
  }

  @Override
  public void showPaymentOption(BuyableType buyableType, Map<AmmoColor, Integer> buyableCost,
      List<PowerUp> budgetPowerUp, Map<AmmoColor, Integer> budgetAmmo) {

    int answerBlue = 0;
    int answerRed = 0;
    int answerYellow = 0;
    int index = 1;
    String inputValidationRegex = "^[0-9,]*$";

    Map<AmmoColor, Integer> costs = new EnumMap<>(AmmoColor.class);
    costs.put(AmmoColor.BLUE, buyableCost.get(AmmoColor.BLUE));
    costs.put(AmmoColor.RED, buyableCost.get(AmmoColor.RED));
    costs.put(AmmoColor.YELLOW, buyableCost.get(AmmoColor.YELLOW));
    costs.put(AmmoColor.ANY, buyableCost.get(AmmoColor.ANY));

    List<Spendable> spendables = setSpendable(budgetPowerUp, budgetAmmo);
    List<PowerUp> answerPowerUp = new ArrayList<>();
    Set<String> answers = null;

    Log.println("Hai a dispozione:");
    for (Spendable match : spendables) {
      Log.println(String
          .format("\t%d) %s%s%s", index, match.getColor().getAnsiColor(), match.getSpendableName(),
              ANSIColor.RESET));
      index++;
    }
    Log.println("");

    Log.println(
        String.format(
            "Devi pagare:\t%s%d rosso%s, %s%d blu%s, %s%d giallo%s, %s%d qualsiasi colore%s%n"
                + "Come preferisci pagare?%n",
            AmmoColor.RED.getAnsiColor(),
            buyableCost.get(AmmoColor.RED),
            ANSIColor.RESET,
            AmmoColor.BLUE.getAnsiColor(),
            buyableCost.get(AmmoColor.BLUE),
            ANSIColor.RESET,
            AmmoColor.YELLOW.getAnsiColor(),
            buyableCost.get(AmmoColor.YELLOW),
            ANSIColor.RESET,
            AmmoColor.ANY.getAnsiColor(),
            buyableCost.get(AmmoColor.ANY),
            ANSIColor.RESET));

    String response;
    do {
      Log.println("Inserisci i numeri delle opzioni scelte separati da una virgola");
      response = scanner.nextLine().replace(" ", "");

      response = response.replace(" ", "");
      answers = new HashSet<>(Arrays.asList(response.split(",")));
    } while (
        response.isEmpty()
        || !response.matches(inputValidationRegex)
        || !verifyPaymentAnswers(answers, spendables)
        || !verifyPaymentFullfilled(answers, spendables, costs)
    );

    for (String element : answers) {
      if (spendables.get(Integer.parseInt(element) - 1).isPowerUp()) {
        answerPowerUp.add((PowerUp) spendables.get(Integer.parseInt(element) - 1));
      } else {
        if (spendables.get(Integer.parseInt(element) - 1).getColor() == AmmoColor.RED) {
          answerRed++;
        } else if (spendables.get(Integer.parseInt(element) - 1).getColor() == AmmoColor.BLUE) {
          answerBlue++;
        } else if (spendables.get(Integer.parseInt(element) - 1).getColor() == AmmoColor.YELLOW) {
          answerYellow++;
        }
      }
    }

    try {
      notifyObservers(
          new PlayerPaymentEvent(client.getPlayerColor(), answerRed, answerBlue, answerYellow,
              answerPowerUp));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  @Override
  public void showTurnActionSelection(List<TurnAction> actions) {
    int targetIndex = 1;
    int chosenTarget = 0;

    try {
      boardView.showBoard();
    } catch (RemoteException e) {
      Log.exception(e);
    }
    do {
      Log.println("Seleziona un'azione");
      for (TurnAction action : actions) {
        Log.println("\t" + targetIndex + ") " + action.getName() + ": " + action.getDescription());
        targetIndex++;
      }

      chosenTarget = Character.getNumericValue(scanner.nextLine().charAt(0));
    } while (chosenTarget == 0 || chosenTarget >= targetIndex);

    try {
      notifyObservers(
          new PlayerActionSelectionEvent(client.getPlayerColor(), actions.get(chosenTarget - 1)));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  private static boolean verifyPaymentAnswers(Set<String> answers, List<Spendable> spendables) {
    if (answers.isEmpty()) {
      Log.println("Hai selezionato un'opzione non valida!");
      return false;
    }
    for (String answer : answers) {
      if (Integer.parseInt(answer) > spendables.size() - 1) {
        Log.println("Hai selezionato un'opzione non valida!");
        return false;
      }
    }
    return true;
  }

  private static boolean verifyPaymentFullfilled(Set<String> answers, List<Spendable> spendables,
      Map<AmmoColor, Integer> costs) {
    for (AmmoColor ammoColor : AmmoColor.values()) {
      if (answers.stream().filter(x -> spendables.get(Integer.parseInt(x) - 1).getColor()
          == ammoColor).count() != costs.get(ammoColor)) {
        Log.println("Non hai pagato l'intera somma!");
        return false;
      }
    }
    return true;
  }

  @Override
  public void showWeaponSelection(List<Weapon> weapons) {
    try {
      boardView.showBoard();
    } catch (RemoteException e) {
      Log.exception(e);
    }
    String weapon = TUIUtils.selectWeapon(weapons, "Quale arma vuoi usare?", true);
    try {
      notifyObservers(new PlayerSelectWeaponEvent(client.getPlayerColor(), weapon));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  @Override
  public void showEffectSelection(Weapon weapon, List<Effect> effects) {
    List<Effect> chosenEffects = new ArrayList<>();
    List<String> chosenEffectsNames = new ArrayList<>();

    chosenEffects.add(TUIUtils.showEffectSelection(effects, false));

    while (!chosenEffects.get(chosenEffects.size() - 1).getSubEffects().isEmpty()) {
      Log.debug("aa1 " + chosenEffects.get(chosenEffects.size()-1));
      chosenEffects.add(
          TUIUtils.showEffectSelection(chosenEffects.get(chosenEffects.size() - 1).getSubEffects(), true));
    }

    Log.debug("aa2");

    for (Effect effect : chosenEffects) {
      chosenEffectsNames.add(effect.getName());
    }

    Log.debug("aa3");
    try {
      notifyObservers(new PlayerSelectWeaponEffectEvent(client.getPlayerColor(), weapon.getName(),
          chosenEffectsNames));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  @Override
  public void showPowerUpSelection(List<PowerUp> powerUps) {
    int targetIndex;
    int chosenTarget;

    do {
      targetIndex = 1;
      Log.println("Seleziona un PowerUp");
      for (PowerUp powerUp : powerUps) {
        Log.println(
            "\t" + targetIndex + ") " + powerUp.getColor().getAnsiColor() + powerUp.getName()
                + ANSIColor.RESET);
        targetIndex++;
      }

      chosenTarget = Character.getNumericValue(scanner.nextLine().charAt(0));
    } while (chosenTarget == 0 || chosenTarget >= targetIndex);

    try {
      notifyObservers(new PlayerDiscardPowerUpEvent(client.getPlayerColor(), powerUps
          .get(chosenTarget - 1).getType(), powerUps.get(chosenTarget - 1).getColor()));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }
}
