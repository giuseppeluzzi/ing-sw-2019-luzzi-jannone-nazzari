package it.polimi.se2019.adrenalina.ui.text;

import it.polimi.se2019.adrenalina.controller.*;
import it.polimi.se2019.adrenalina.controller.action.game.Payment;
import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.event.viewcontroller.*;
import it.polimi.se2019.adrenalina.exceptions.InputCancelledException;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.BuyableType;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Spendable;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.Client;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.ANSIColor;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Timer;
import it.polimi.se2019.adrenalina.view.BoardView;
import it.polimi.se2019.adrenalina.view.BoardViewInterface;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsView;

import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Text User Interface dashboards view
 */
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

  @Override
  public void switchToFinalFrenzy(PlayerColor playerColor) {
    Log.println("Sei passato in modalità frenesia finale!");
  }

  /**
   * Show payment options to the user.
   * @param buyableType the type of object the user is buying
   * @param buyableCost the cost of the object the user is buying
   * @param budgetPowerUp the user's budget in power ups
   * @param budgetAmmo the user's budget in ammos
   */
  @Override
  public void showPaymentOption(BuyableType buyableType, String prompt, Map<AmmoColor, Integer> buyableCost,
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

    List<Spendable> spendables = Player.setSpendable(budgetPowerUp, budgetAmmo);
    List<PowerUp> answerPowerUp = new ArrayList<>();
    Set<String> answers = null;

    Log.println("Hai a disposizione:");
    for (Spendable match : spendables) {
      Log.println(String
          .format("\t%d) %s%s%s", index, match.getColor().getAnsiColor(), match.getSpendableName(),
              ANSIColor.RESET));
      index++;
    }
    Log.println("");

    Log.println(
        String.format(
            "Devi pagare:\t%s%d blu%s, %s%d rosso%s, %s%d giallo%s, %s%d qualsiasi colore%s%n"
                + "Come preferisci pagare %s?%n",
            AmmoColor.BLUE.getAnsiColor(),
            buyableCost.get(AmmoColor.BLUE),
            ANSIColor.RESET,
            AmmoColor.RED.getAnsiColor(),
            buyableCost.get(AmmoColor.RED),
            ANSIColor.RESET,
            AmmoColor.YELLOW.getAnsiColor(),
            buyableCost.get(AmmoColor.YELLOW),
            ANSIColor.RESET,
            AmmoColor.ANY.getAnsiColor(),
            buyableCost.get(AmmoColor.ANY),
            ANSIColor.RESET,
            prompt)
        );


    timer.start(ClientConfig.getInstance().getTurnTimeout(), () -> inputManager.cancel(
        WAIT_TIMEOUT_MSG));

    String response;
    do {
      inputManager.input("Inserisci i numeri delle opzioni scelte separati da una virgola");
      try {
        response = inputManager.waitForStringResult().replace(" ", "");
      } catch (InputCancelledException e) {
        return;
      }

      answers = new HashSet<>(Arrays.asList(response.split(",")));

      if (!Payment.verifyPaymentAnswers(answers, spendables)) {
        Log.println("Hai selezionato un'opzione non valida!");
      } else {
        if (!Payment.verifyPaymentFullfilled(answers, spendables, costs)) {
          Log.println("Hai selezionato un'opzione non valida!");
        }
      }

    } while (!response.matches(inputValidationRegex)
        || !Payment.verifyPaymentAnswers(answers, spendables)
        || !Payment.verifyPaymentFullfilled(answers, spendables, costs)
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

  /**
   * Show turn action selection prompt to the user.
   * @param actions possible actions
   */
  @Override
  public void showTurnActionSelection(List<TurnAction> actions) {
    boardView.showBoard();
    List<String> choices = new ArrayList<>();
    for (TurnAction action : actions) {
      choices.add(action.getName() + ": " + action.getDescription());
    }
    inputManager.input("Seleziona un'azione:", choices);
    timer.start(ClientConfig.getInstance().getTurnTimeout(), () -> inputManager.cancel(
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

  /**
   * Show weapon selection prompt to the user.
   * @param weapons the list of weapons to show
   */
  @Override
  public void showWeaponSelection(List<Weapon> weapons) {
    boardView.showBoard();
    String weapon = null;
    timer.start(ClientConfig.getInstance().getTurnTimeout(), TUIUtils::cancelInput);
    try {
      weapon = TUIUtils.selectWeapon(weapons, "Quale arma vuoi usare?", true);
    } catch (InputCancelledException e) {
      return;
    }
    timer.stop();
    try {
      notifyObservers(new PlayerSelectWeaponEvent(client.getPlayerColor(), weapon));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Show effect selection prompt to the user, including any nested subeffects.
   * @param weapon the effects' parent weapon
   * @param effects the list of effects to show
   */
  @Override
  public void showEffectSelection(Weapon weapon, List<Effect> effects) {
    List<Effect> buffer;
    List<Effect> chosenEffects;
    Queue<Effect> effectQueue;
    timer.start(ClientConfig.getInstance().getTurnTimeout(), TUIUtils::cancelInput);
    try {
      buffer = new ArrayList<>(TUIUtils.showEffectSelection(effects, false));
      chosenEffects = new ArrayList<>(buffer);
      effectQueue = new LinkedList<>(buffer);
    } catch (InputCancelledException e) {
      return;
    }
    timer.stop();
    Effect currentEffect;
    while (! effectQueue.isEmpty()) {
      currentEffect = effectQueue.remove();
      if (! currentEffect.getSubEffects().isEmpty()) {
        timer.start(ClientConfig.getInstance().getTurnTimeout(), TUIUtils::cancelInput);
        try {
          buffer = new ArrayList<>(TUIUtils.showEffectSelection(currentEffect.getSubEffects(), true));
          chosenEffects.addAll(buffer);
          effectQueue.addAll(buffer);
        } catch (InputCancelledException e) {
          return;
        }
        timer.stop();
      }
    }
    List<Effect> chosenEffectsWithAnyTimes;
    try {
      chosenEffectsWithAnyTimes = handleAnyTimeEffects(chosenEffects);
    } catch (InputCancelledException e) {
      return;
    }
    try {
      if (! confirmAvailableFunds(chosenEffectsWithAnyTimes)) {
        Log.println("Non puoi permetterti di pagare tutti gli effetti scelti!");
        showEffectSelection(weapon, effects);
        return;
      }
    } catch (InvalidPlayerException e) {
      Log.warn(e.toString());
      return;
    } catch (RemoteException e) {
      Log.exception(e);
      return;
    }
    List<String> chosenEffectsNames = new ArrayList<>();
    for (Effect effect : chosenEffectsWithAnyTimes) {
      chosenEffectsNames.add(effect.getName());
    }
    try {
      notifyObservers(new PlayerSelectWeaponEffectEvent(client.getPlayerColor(), weapon.getName(),
          chosenEffectsNames));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Confirms that a player has enough buyables to pay for all of the chosen effects.
   * @param chosenEffects the list of chosen effects
   * @return true if the player can pay for the chosen effects, false otherwise
   * @throws InvalidPlayerException if the selected player is invalid
   * @throws RemoteException if a remote exception occurs
   */
  private boolean confirmAvailableFunds(List<Effect> chosenEffects) throws InvalidPlayerException, RemoteException {
    Map<AmmoColor, Integer> availableFunds = new EnumMap<>(AmmoColor.class);
    availableFunds.putAll(client.getBoardView().getBoard().getPlayerByColor(client.getPlayerColor()).getAmmos());
    for (PowerUp powerUp : client.getBoardView().getBoard().getPlayerByColor(client.getPlayerColor()).getPowerUps()) {
      availableFunds.put(powerUp.getColor(), availableFunds.get(powerUp.getColor()) + 1);
    }
    int anyColorDue = 0;
    for (Effect effect : chosenEffects) {
      for (Map.Entry<AmmoColor, Integer> entry : effect.getCost().entrySet()) {
        if (entry.getKey() == AmmoColor.ANY) {
          anyColorDue += entry.getValue();
        } else {
          availableFunds.put(entry.getKey(), availableFunds.get(entry.getKey()) - entry.getValue());
        }
      }
    }
    int totalRemainingFunds = 0;
    for (Map.Entry<AmmoColor, Integer> entry : availableFunds.entrySet()) {
      if (entry.getValue() < 0) {
        return false;
      }
      totalRemainingFunds += entry.getValue();
    }
    return totalRemainingFunds >= anyColorDue;
  }

  /**
   * Handle anyTime effects by asking the user when to use them.
   * @param chosenEffects the list of chosen effects
   * @return a list of chosen effects with anyTime effects in the right position
   * @throws InputCancelledException if the user input is cancelled
   */
  private List<Effect> handleAnyTimeEffects(List<Effect> chosenEffects) throws InputCancelledException {
    List<Effect> chosenEffectsWithAnyTimes = chosenEffects.stream().filter(x -> ! x.isAnyTime() || x.isIndexConfirmed()).collect(Collectors.toList());
    for (Effect effect : chosenEffects) {
      if (effect.isAnyTime()) {
        timer.start(ClientConfig.getInstance().getTurnTimeout(), TUIUtils::cancelInput);
        int effectIndex = TUIUtils.askAnyTimeIndex(effect, chosenEffectsWithAnyTimes);
        timer.stop();
        List<Effect> temp = new ArrayList<>(chosenEffectsWithAnyTimes);
        chosenEffectsWithAnyTimes.clear();
        effect.setIndexConfirmed(true);
        for (int i = 0; i < temp.size(); i++) {
          if (i == effectIndex) {
            chosenEffectsWithAnyTimes.add(effect);
          }
          chosenEffectsWithAnyTimes.add(temp.get(i));
        }
        if (effectIndex == temp.size()) {
          chosenEffectsWithAnyTimes.add(effect);
        }
      }
    }
    return chosenEffectsWithAnyTimes;
  }

  /**
   * Show weapon selection prompt for the user to select which weapon to swap.
   * @param ownWeapons the list of user's weapons to show
   * @param squareWeapons the list of collectable weapons
   */
  @Override
  public void showSwapWeaponSelection(List<Weapon> ownWeapons, List<Weapon> squareWeapons) {
    boardView.showBoard();
    String ownWeapon;
    String squareWeapon;
    timer.start(ClientConfig.getInstance().getTurnTimeout(), () -> inputManager.cancel(
            WAIT_TIMEOUT_MSG));
    try {
      ownWeapon = TUIUtils
          .selectWeapon(ownWeapons, "Quale arma vuoi scambiare?", true);
      squareWeapon = TUIUtils
          .selectWeapon(squareWeapons, "Quale arma vuoi prendere?", true);
    } catch (InputCancelledException e) {
      return;
    }
    timer.stop();
    try {
      notifyObservers(new PlayerSwapWeaponEvent(client.getPlayerColor(), ownWeapon, squareWeapon));
    } catch (RemoteException e) {
      Log.exception(e);
    }
    boardView.showBoard();
  }

  /**
   * Show weapon reload selection prompt to the user.
   * @param unloadedWeapons the list of unloaded weapons to show
   */
  @Override
  public void showReloadWeaponSelection(List<Weapon> unloadedWeapons) {
    int chosenTarget = 0;
    String prompt;
    List<String> choices = new ArrayList<>();

    for (Weapon weapon : unloadedWeapons) {
      int costRed = weapon.getCost(AmmoColor.RED);
      int costBlue = weapon.getCost(AmmoColor.BLUE);
      int costYellow = weapon.getCost(AmmoColor.YELLOW);

      switch (weapon.getBaseCost()) {
        case BLUE:
          costBlue++;
          break;
        case RED:
          costRed++;
          break;
        case YELLOW:
          costYellow++;
          break;
        default:
          break;
      }

      String current = String.format(weapon.getName() +
              ", costo di ricarica: %s%d blu%s, %s%d rosso%s, %s%d giallo%s",
          AmmoColor.BLUE.getAnsiColor(),
          costBlue,
          ANSIColor.RESET,
          AmmoColor.RED.getAnsiColor(),
          costRed,
          ANSIColor.RESET,
          AmmoColor.YELLOW.getAnsiColor(),
          costYellow,
          ANSIColor.RESET);
      choices.add(current);
    }

    choices.add("Non ricaricare nessun'arma");
    prompt = "Seleziona quali armi vuoi ricaricare";

    inputManager.input(prompt, choices);
    try {
      chosenTarget = inputManager.waitForIntResult();
      timer.stop();
    } catch (InputCancelledException e) {
      return;
    }

    try {
      if (chosenTarget == unloadedWeapons.size()) {
        notifyObservers(new PlayerReloadEvent(getPrivatePlayerColor(), null));
      } else {
        notifyObservers(new PlayerReloadEvent(getPrivatePlayerColor(), unloadedWeapons.get(chosenTarget).getName()));
      }
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Show power up selection prompt to the user.
   * @param powerUps the list of power ups to show
   * @param discard true if the selection is going to discard the power up for a (re)spwan, false otherwise
   */
  @Override
  public void showPowerUpSelection(String targetName, List<PowerUp> powerUps, boolean discard) {
    boardView.showBoard();

    int chosenTarget = 0;
    String prompt;
    List<String> choices = new ArrayList<>();

    if (discard) {
      prompt = "Seleziona quale potenziamento scartare:";
      timer.start(ClientConfig.getInstance().getTurnTimeout(),
          () -> inputManager.cancel("Tempo di attesa scaduto! Verrà scartato un potenziamento a caso"));
    } else {
      if (targetName != null) {
        prompt = String.format("Seleziona quale potenziamento usare contro %s:", targetName);
      } else {
        prompt = "Seleziona quale potenziamento usare:";
      }
      choices.add("Non usare nessun potenziamento");
      timer.start(ClientConfig.getInstance().getTurnTimeout(), () -> inputManager.cancel(
          WAIT_TIMEOUT_MSG));
    }
    for (PowerUp powerUp : powerUps) {
      choices.add(String.format("%s%s%s (%s)",
              powerUp.getColor().getAnsiColor(),
              powerUp.getName(),
              ANSIColor.RESET,
              powerUp.getSymbol()));
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

  /**
   * Show unsuspend prompt to the (newly) suspended user.
   */
  @Override
  public void showUnsuspendPrompt() {
    // If the view's timer has not expired yet, let's stop it manually and cancel user input
    timer.stop();
    inputManager.cancel(WAIT_TIMEOUT_MSG);
    new Thread(() -> {
      TUIInputManager suspInputManager = new TUIInputManager();
      ((Client) client).suspendOutput(true);
      suspInputManager.input("Sei stato sospeso dalla partita. Premi invio per ricominciare a giocare...", 0, Integer.MAX_VALUE);
      try {
        suspInputManager.waitForStringResult();
        ((Client) client).suspendOutput(false);
      } catch (InputCancelledException e) {
        ((Client) client).suspendOutput(false);
        return;
      }
      Log.println("Bentornato!");
      try {
        notifyObservers(new PlayerUnsuspendEvent(client.getPlayerColor()));
      } catch (RemoteException e) {
        Log.exception(e);
      }
    }).start();
  }
}
