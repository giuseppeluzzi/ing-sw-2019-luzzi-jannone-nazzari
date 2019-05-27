package it.polimi.se2019.adrenalina.ui.text;


import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.controller.event.viewcontroller.PlayerActionSelectionEvent;
import it.polimi.se2019.adrenalina.controller.event.viewcontroller.PlayerCollectWeaponEvent;
import it.polimi.se2019.adrenalina.controller.event.viewcontroller.PlayerDiscardPowerUpEvent;
import it.polimi.se2019.adrenalina.controller.event.viewcontroller.PlayerPaymentEvent;
import it.polimi.se2019.adrenalina.controller.event.viewcontroller.PlayerSelectWeaponEffectEvent;
import it.polimi.se2019.adrenalina.model.Buyable;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Spendable;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.ANSIColor;
import it.polimi.se2019.adrenalina.utils.Log;
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

  public TUIPlayerDashboardsView(ClientInterface client) {
    this.client = client;
  }

  private List<Spendable> setSpendable(Player player) {
    List<Spendable> spendables = new ArrayList<>();
    List<PowerUp> powerUps = player.getPowerUps();
    int index = 0;
    int redAmmo = player.getAmmo(AmmoColor.RED);
    int blueAmmo = player.getAmmo(AmmoColor.BLUE);
    int yellowAmmo = player.getAmmo(AmmoColor.YELLOW);

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
  public void showPaymentOption(Buyable item) {
    Player player;
    try {
      player = getPlayerByColor(client.getPlayerColor());
    } catch (RemoteException e) {
      Log.exception(e);
      return;
    }

    int answerBlue = 0;
    int answerRed = 0;
    int answerYellow = 0;
    int index = 1;
    String inputValidationRegex = "/^[0-9,.]*$/";

    Map<AmmoColor, Integer> costs = new EnumMap<>(AmmoColor.class);
    costs.put(AmmoColor.BLUE, item.getCost(AmmoColor.BLUE));
    costs.put(AmmoColor.RED, item.getCost(AmmoColor.RED));
    costs.put(AmmoColor.YELLOW, item.getCost(AmmoColor.YELLOW));
    costs.put(AmmoColor.ANY, item.getCost(AmmoColor.ANY));

    List<Spendable> spendables = setSpendable(player);
    List<PowerUp> answerPowerUp = new ArrayList<>();
    Set<String> answers = null;
    boolean completed = false;

    for (Spendable match : spendables) {
      Log.println(String.format("\t%d) %s", index, match.getSpendableName()));
      index++;
    }

    Log.println(
        String.format("Il costo da pagare è:%n\t%d rosso, %d blu, %d giallo, %d qualsiasi colore%n"
            + "Come preferisci pagare?", item.getCost(AmmoColor.RED), item.getCost(AmmoColor.BLUE), item.getCost(AmmoColor.YELLOW), item.getCost(AmmoColor.ANY)));

    do {
      Log.println("Inserisci i numeri scelti separati da virgole");
      String response = scanner.nextLine();
      if (!response.matches(inputValidationRegex)) {
        continue;
      }

      response = response.replace(" ", "");
      answers = new HashSet<>(Arrays.asList(response.split(",")));
      validatePaymentAnswer(answers, spendables, costs);
      completed = true;
    } while (!completed);

    for (String element : answers) {
      if (spendables.get(Integer.parseInt(element)).isPowerUp()) {
        answerPowerUp.add((PowerUp) spendables.get(Integer.parseInt(element)));
      } else {
        if (spendables.get(Integer.parseInt(element)).getColor() == AmmoColor.RED) {
          answerRed++;
        } else if (spendables.get(Integer.parseInt(element)).getColor() == AmmoColor.BLUE) {
          answerBlue++;
        } else if (spendables.get(Integer.parseInt(element)).getColor() == AmmoColor.YELLOW) {
          answerYellow++;
        }
      }
    }

    try {
      notifyObservers(
          new PlayerPaymentEvent(client.getPlayerColor(), answerRed, answerBlue, answerYellow,
              answerPowerUp, item));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  @Override
  public void showTurnActionSelection(List<TurnAction> actions) {
    int targetIndex = 1;
    int chosenTarget = 0;

    do {
      Log.println("Seleziona un'azione");
      for (TurnAction action : actions) {
        Log.println("\t" + targetIndex + ") " + action.getName() + ": " + action.getDescription());
        targetIndex++;
      }

      chosenTarget = Character.getNumericValue(scanner.nextLine().charAt(0));
    } while (chosenTarget == 0 || chosenTarget >= targetIndex);

    try {
      notifyObservers(new PlayerActionSelectionEvent(client.getPlayerColor(), actions.get(chosenTarget - 1)));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  private void validatePaymentAnswer(Set<String> answers, List<Spendable> matches,
      Map<AmmoColor, Integer> costs) {
    for (AmmoColor ammoColor : AmmoColor.values()) {
      if (answers.stream().filter(x -> matches.get(Integer.parseInt(x)).getColor()
          == ammoColor).count() != costs.get(ammoColor)) {
        break;
      }
    }
  }

  @Override
  public void showWeaponSelection(List<Weapon> weapons) {
    String weapon = TUIUtils.selectWeapon(weapons, "Quale arma vuoi usare?", true);
    try {
      notifyObservers(new PlayerCollectWeaponEvent(client.getPlayerColor(), weapon));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  @Override
  public void showEffectSelection(Weapon weapon) {
    List<Effect> chosenEffects = new ArrayList<>();
    List<String> chosenEffectsNames = new ArrayList<>();
    int targetIndex = 1;
    int subEffectIndex = 1;
    int chosenTarget = 0;

    chosenEffects.add(TUIUtils.showEffectSelection(weapon.getEffects(), false));

    while (!chosenEffects.get(chosenEffects.size() - 1).getSubEffects().isEmpty()) {
      chosenEffects.add(TUIUtils.showEffectSelection(chosenEffects.get(0).getSubEffects(), true));
    }

    for (Effect effect : chosenEffects) {
      chosenEffectsNames.add(effect.getName());
    }

    try {
      notifyObservers(new PlayerSelectWeaponEffectEvent(client.getPlayerColor(), weapon.getName(), chosenEffectsNames));
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
        Log.println("\t" + targetIndex + ") " + powerUp.getColor().getAnsiColor() + powerUp.getName() + ANSIColor.RESET);
        targetIndex++;
      }

      chosenTarget = Character.getNumericValue(scanner.nextLine().charAt(0));
    } while (chosenTarget == 0 || chosenTarget >= targetIndex);

    try {
      notifyObservers(new PlayerDiscardPowerUpEvent(client.getPlayerColor(), powerUps.get(chosenTarget - 1)));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }
}
