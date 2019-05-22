package it.polimi.se2019.adrenalina.ui.text;


import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.controller.event.PlayerActionSelectionEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerPaymentEvent;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Spendable;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsView;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
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
  public void showPaymentOption(int blue, int red, int yellow, int any) {

    Player player;
    try {
      player = getPlayerByColor(client.getPlayerColor());
    } catch (RemoteException e) {
      Log.exception(e);
      return;
    }

    String regex = "/^[0-9,.]*$/";
    Map<AmmoColor, Integer> costs = new EnumMap<>(AmmoColor.class);
    costs.put(AmmoColor.BLUE, blue);
    costs.put(AmmoColor.RED, red);
    costs.put(AmmoColor.YELLOW, yellow);
    costs.put(AmmoColor.ANY, any);
    int answerBlue = 0;
    int answerRed = 0;
    int answerYellow = 0;
    int index = 1;
    List<PowerUp> answerPowerUp = new ArrayList<>();
    Set<String> answers = null;

    boolean completed = false;

    List<Spendable> spendables = setSpendable(player);

    for (Spendable match : spendables) {
      Log.print(String.format("\t%d) %s", index, match.getSpendableName()));
      index++;
    }

    Log.print(
        String.format("Devi pagare questa quantità:\nq blu: %d, rosso: %d, giallo: %d, generico: %d"
            + "Come preferisci pagare?", blue, red, yellow, any));

    do {
      Log.print("Inserisci i numeri scelti separati da virgole");
      String response = scanner.nextLine();
      if (!response.matches(regex)) {
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
              answerPowerUp));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  @Override
  public void showTurnActionSelection(List<TurnAction> actions) {
    int targetIndex = 0;
    int choosenTarget = 0;

    do {
      Log.print("Seleziona un'azione");
      for (TurnAction action : actions) {
        Log.print("\t" + targetIndex + ") " + action.getName() + ": " + action.getDescription());
        targetIndex++;
      }

      choosenTarget = Character.getNumericValue(scanner.nextLine().charAt(0));
    } while (choosenTarget == 0 || choosenTarget >= targetIndex);

    try {
      notifyObservers(new PlayerActionSelectionEvent(client.getPlayerColor(), actions.get(choosenTarget)));
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
}
