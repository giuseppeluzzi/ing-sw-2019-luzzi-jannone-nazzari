package it.polimi.se2019.adrenalina.ui.text;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.Configuration;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.event.viewcontroller.*;
import it.polimi.se2019.adrenalina.exceptions.InputCancelledException;
import it.polimi.se2019.adrenalina.model.BuyableType;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Spendable;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.ANSIColor;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Timer;
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
import java.util.Set;

public class TUIPlayerDashboardsView extends PlayerDashboardsView {

  private static final long serialVersionUID = 572470044324855920L;
  private static final String WAIT_TIMEOUT_MSG = "Tempo di attesa scaduto! Salti il turno!";
  private final transient ClientInterface client;
  private final transient TUIInputManager inputManager = new TUIInputManager();
  private final Timer timer = new Timer();
  private final TUIBoardView boardView;

  public TUIPlayerDashboardsView(ClientInterface client, BoardViewInterface boardView) {
    super((BoardView) boardView);
    this.client = client;
    this.boardView = (TUIBoardView) boardView;
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
    String inputValidationRegex = "^(\\d+(,\\d+)*)?$";

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

    timer.start(Configuration.getInstance().getTurnTimeout(), () -> inputManager.cancel(
        WAIT_TIMEOUT_MSG));

    do {
      inputManager.input("Inserisci i numeri delle opzioni scelte separati da una virgola");
      try {
        response = inputManager.waitForStringResult().replace(" ", "");
        timer.stop();
      } catch (InputCancelledException e) {
        return;
      }

      answers = new HashSet<>(Arrays.asList(response.split(",")));
    } while (!response.matches(inputValidationRegex)
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
    boardView.showBoard();
    List<String> choices = new ArrayList<>();
    for (TurnAction action : actions) {
      choices.add(action.getName() + ": " + action.getDescription());
    }
    inputManager.input("Seleziona un'azione:", choices);
    timer.start(Configuration.getInstance().getTurnTimeout(), () -> inputManager.cancel(
        WAIT_TIMEOUT_MSG));
    try {
      notifyObservers(
          new PlayerActionSelectionEvent(client.getPlayerColor(),
              actions.get(inputManager.waitForIntResult())));
      timer.stop();
    } catch (RemoteException e) {
      Log.exception(e);
    } catch (InputCancelledException ignored) {
      // return
    }
  }

  private static boolean verifyPaymentAnswers(Set<String> answers, List<Spendable> spendables) {
    if (answers.isEmpty()) {
      Log.println("Hai selezionato un'opzione non valida!");
      return false;
    }
    for (String answer : answers) {
      if (Integer.parseInt(answer) - 1 > spendables.size()) {
        Log.println("Hai selezionato un'opzione non valida!");
        return false;
      }
    }
    return true;
  }

  private static boolean verifyPaymentFullfilled(Set<String> answers, List<Spendable> spendables,
      Map<AmmoColor, Integer> costs) {

    int blueCost = costs.get(AmmoColor.BLUE);
    int redCost = costs.get(AmmoColor.RED);
    int yellowCost = costs.get(AmmoColor.YELLOW);
    int anyCost = costs.get(AmmoColor.ANY);

    for (String answer : answers) {
      if (spendables.get(Integer.parseInt(answer) - 1).getColor() == AmmoColor.BLUE) {
        if (blueCost > 0) {
          blueCost--;
        } else if (anyCost > 0) {
          anyCost--;
        }
      } else if (spendables.get(Integer.parseInt(answer) - 1).getColor() == AmmoColor.RED) {
        if (redCost > 0) {
          redCost--;
        } else if (anyCost > 0) {
          anyCost--;
        }
      } else if (spendables.get(Integer.parseInt(answer) - 1).getColor() == AmmoColor.YELLOW) {
        if (yellowCost > 0) {
          yellowCost--;
        } else if (anyCost > 0) {
          anyCost--;
        }
      }
    }

    if (blueCost + redCost + yellowCost + anyCost == 0) {
      return true;
    }

    return false;
  }

  @Override
  public void showWeaponSelection(List<Weapon> weapons) {
    boardView.showBoard();
    String weapon = null;
    try {
      weapon = TUIUtils.selectWeapon(weapons, "Quale arma vuoi usare?", true);
    } catch (InputCancelledException e) {
      return;
    }
    try {
      notifyObservers(new PlayerSelectWeaponEvent(client.getPlayerColor(), weapon));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  @Override
  public void showEffectSelection(Weapon weapon, List<Effect> effects) {
    List<Effect> chosenEffects = new ArrayList<>();

    try {
      chosenEffects.add(TUIUtils.showEffectSelection(effects, false));
    } catch (InputCancelledException e) {
      return;
    }
    List<String> chosenEffectsNames = new ArrayList<>();
    while (!chosenEffects.get(chosenEffects.size() - 1).getSubEffects().isEmpty()) {
      Log.debug("aa1 " + chosenEffects.get(chosenEffects.size() - 1));
      try {
        Effect toAdd = TUIUtils
            .showEffectSelection(chosenEffects.get(chosenEffects.size() - 1).getSubEffects(),
                true);
        if (toAdd == null) {
          break;
        }
        chosenEffects.add(toAdd);
      } catch (InputCancelledException ignored) {
        // return
      }
    }

    for (Effect effect : chosenEffects) {
      chosenEffectsNames.add(effect.getName());
    }

    try {
      notifyObservers(new PlayerSelectWeaponEffectEvent(client.getPlayerColor(), weapon.getName(),
          chosenEffectsNames));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  @Override
  public void showSwapWeaponSelection(List<Weapon> ownWeapons, List<Weapon> squareWeapons) {
    boardView.showBoard();
    String ownWeapon;
    String squareWeapon;
    try {
      ownWeapon = TUIUtils
          .selectWeapon(ownWeapons, "Quale arma vuoi scambiare?", true);
      squareWeapon = TUIUtils
          .selectWeapon(squareWeapons, "Quale arma vuoi prendere?", true);
    } catch (InputCancelledException e) {
      return;
    }
    try {
      notifyObservers(new PlayerSwapWeaponEvent(client.getPlayerColor(), ownWeapon, squareWeapon));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  @Override
  public void showPowerUpSelection(List<PowerUp> powerUps, boolean discard) {
    boardView.showBoard();

    int chosenTarget = 0;
    String prompt;
    List<String> choices = new ArrayList<>();

    if (discard) {
      prompt = "Seleziona quale PowerUp scartare:";
      timer.start(Configuration.getInstance().getTurnTimeout(),
          () -> inputManager.cancel("Tempo di attesa scaduto! Verrà scartato un powerUp a caso"));
    } else {
      prompt = "Seleziona quale PowerUp usare:";
      choices.add("Non usare nessun PowerUp");
      timer.start(Configuration.getInstance().getTurnTimeout(), () -> inputManager.cancel(
          WAIT_TIMEOUT_MSG));
    }
    for (PowerUp powerUp : powerUps) {
      choices.add(powerUp.getColor().getAnsiColor() + powerUp.getName() + ANSIColor.RESET);
    }
    inputManager.input(prompt, choices);
    try {
      chosenTarget = inputManager.waitForIntResult();
      timer.stop();
    } catch (InputCancelledException e) {
      return;
    }

    try {
      if (discard) {
        notifyObservers(new PlayerDiscardPowerUpEvent(client.getPlayerColor(), powerUps
            .get(chosenTarget).getType(), powerUps.get(chosenTarget).getColor()));
      } else {
        if (chosenTarget == 0) {
          notifyObservers(new PlayerPowerUpEvent(client.getPlayerColor(), null, null));
        } else {
          notifyObservers(new PlayerPowerUpEvent(client.getPlayerColor(), powerUps
              .get(chosenTarget - 1).getType(), powerUps.get(chosenTarget - 1).getColor()));
        }
      }
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  @Override
  public void showUnsuspendPrompt() {
    inputManager.input("Sei stato sospeso dalla partita. Premi invio per ricominciare a giocare...", 0, Integer.MAX_VALUE);
    try {
      inputManager.waitForStringResult();
    } catch (InputCancelledException e) {
      return;
    }
    try {
      notifyObservers(new PlayerUnsuspendEvent(client.getPlayerColor()));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }
}
