package it.polimi.se2019.adrenalina.network;

import static java.lang.Thread.sleep;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import it.polimi.se2019.adrenalina.controller.Configuration;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.event.PingEvent;
import it.polimi.se2019.adrenalina.event.PlayerConnectEvent;
import it.polimi.se2019.adrenalina.event.PlayerDisconnectEvent;
import it.polimi.se2019.adrenalina.event.invocations.ShowBuyableWeaponsInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowDeathInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowEffectSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowMessageInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowPaymentOptionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowPowerUpSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowReloadWeaponSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowSquareSelectInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowSwapWeaponSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowTargetSelectInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowTurnActionSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowWeaponSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.SwitchToFinalFrenzyInvocation;
import it.polimi.se2019.adrenalina.event.invocations.TimerSetEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerSetColorEvent;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.utils.Constants;
import it.polimi.se2019.adrenalina.utils.JsonEffectDeserializer;
import it.polimi.se2019.adrenalina.utils.JsonPowerUpDeserializer;
import it.polimi.se2019.adrenalina.utils.JsonTargetDeserializer;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.NotExposeExclusionStrategy;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Socket version of the client object.
 */
public class ClientSocket extends Client implements Runnable, Observer {

  private static final long serialVersionUID = 5069992236971339205L;
  private boolean running;
  private transient Socket socket;
  private transient PrintWriter printWriter;
  private transient BufferedReader bufferedReader;

  public ClientSocket(String name, boolean domination, boolean tui) {
    super(name, domination, tui);

    running = true;

    try {
      getBoardView().addObserver(this);
      getCharactersView().addObserver(this);
      getPlayerDashboardsView().addObserver(this);
    } catch (RemoteException e) {
      Log.exception(e);
    }

    try {
      socket = new Socket(Configuration.getInstance().getServerIP(),
          Configuration.getInstance().getSocketPort());
      OutputStream outputStream = socket.getOutputStream();
      printWriter = new PrintWriter(outputStream, true);
      bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),
          StandardCharsets.UTF_8));
    } catch (IOException e) {
      Log.exception(e);
      return;
    }

    sendEvent(new PlayerConnectEvent(name, domination));
    run();
  }

  @Override
  public void disconnect(String message) {
    super.disconnect(message);
    running = false;
    try {
      bufferedReader.close();
      printWriter.close();
      socket.close();
    } catch (IOException e) {
      Log.exception(e);
    }
  }

  @Override
  public void update(Event event) {
    sendEvent(event);
  }

  public final void sendEvent(Event event) {
    printWriter.println(event.serialize());
    printWriter.flush();
  }

  @Override
  public final void run() {
    if (socket != null) {
      new Thread(() -> {
        while (running) {
          sendEvent(new PingEvent());
          try {
            sleep(Constants.PING_INTERVAL);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
        }
      }).start();
      try {
        while (socket.isConnected() && running) {
          String message = bufferedReader.readLine();

          GsonBuilder gsonBuilder = new GsonBuilder();
          gsonBuilder.registerTypeAdapter(Effect.class, new JsonEffectDeserializer());
          gsonBuilder.registerTypeAdapter(PowerUp.class, new JsonPowerUpDeserializer());
          gsonBuilder.registerTypeAdapter(Target.class, new JsonTargetDeserializer());
          gsonBuilder.addDeserializationExclusionStrategy(new NotExposeExclusionStrategy());
          Gson gson = gsonBuilder.create();
          JsonObject json = gson.fromJson(message, JsonObject.class);
          Log.debug(message);
          EventType eventType = EventType.valueOf(json.get("eventType").getAsString());
          Log.debug("Received: " + eventType);
          Event event = gson.fromJson(message, eventType.getEventClass());

          switch (eventType) {
            case PLAYER_DISCONNECT_EVENT:
              PlayerDisconnectEvent disconnectEvent = gson
                  .fromJson(message, PlayerDisconnectEvent.class);
              disconnect(disconnectEvent.getMessage());
              break;
            case TIMER_SET_EVENT:
              TimerSetEvent timerSetEvent = gson.fromJson(message, TimerSetEvent.class);
              if (timerSetEvent.getTimer() == 0) {
                getBoardView().hideTimer();
              } else {
                getBoardView().startTimer(timerSetEvent.getTimer());
              }
              break;
            case PLAYER_SET_COLOR:
              PlayerSetColorEvent playerSetColorEvent = gson.fromJson(message,
                  PlayerSetColorEvent.class);
              setPlayerColor(playerSetColorEvent.getPlayerColor());
              break;
            case SHOW_BOARD_INVOCATION:
              getBoardView().showBoard();
              break;
            case SHOW_DEATH_INVOCATION:
              ShowDeathInvocation showDeathInvocation = gson.fromJson(message,
                  ShowDeathInvocation.class);
              getCharactersView().showDeath(showDeathInvocation.getPlayerColor());
              break;
            case SHOW_SQUARE_SELECT_INVOCATION:
              ShowSquareSelectInvocation showSquareSelectInvocation = gson.fromJson(message,
                  ShowSquareSelectInvocation.class);
              getBoardView()
                  .showSquareSelect(new ArrayList<>(showSquareSelectInvocation.getTargets()));
              break;
            case SHOW_TARGET_SELECT_INVOCATION:
              ShowTargetSelectInvocation showTargetSelectInvocation = gson.fromJson(message,
                  ShowTargetSelectInvocation.class);
              getBoardView().showTargetSelect(showTargetSelectInvocation.getTargetType(),
                  showTargetSelectInvocation.getTargets());
              break;
            case SHOW_PAYMENT_OPTION_INVOCATION:
              ShowPaymentOptionInvocation showPaymentOptionInvocation = gson.fromJson(message,
                  ShowPaymentOptionInvocation.class);
              getPlayerDashboardsView().showPaymentOption(
                  showPaymentOptionInvocation.getBuyableType(),
                  showPaymentOptionInvocation.getBuyableCost(),
                  showPaymentOptionInvocation.getBudgetPowerUps(),
                  showPaymentOptionInvocation.getBudgetAmmos());
              break;
            case SHOW_BUYABLE_WEAPONS_INVOCATION:
              ShowBuyableWeaponsInvocation showBuyableWeaponsInvocation = gson.fromJson(message,
                  ShowBuyableWeaponsInvocation.class);
              getBoardView().showBuyableWeapons(showBuyableWeaponsInvocation.getWeaponList());
              break;
            case SHOW_DIRECTION_SELECT_INVOCATION:
              getBoardView().showDirectionSelect();
              break;
            case SHOW_EFFECT_SELECTION_INVOCATION:
              ShowEffectSelectionInvocation showEffectSelectionInvocation = gson.fromJson(message,
                  ShowEffectSelectionInvocation.class);
              getPlayerDashboardsView()
                  .showEffectSelection(showEffectSelectionInvocation.getWeapon(),
                      showEffectSelectionInvocation.getEffects());
              break;
            case SHOW_WEAPON_SELECTION_INVOCATION:
              ShowWeaponSelectionInvocation showWeaponSelectionInvocation = gson.fromJson(message,
                  ShowWeaponSelectionInvocation.class);
              getPlayerDashboardsView()
                  .showWeaponSelection(showWeaponSelectionInvocation.getWeapons());
              break;
            case SWITCH_TO_FINAL_FRENZY_INVOCATION:
              SwitchToFinalFrenzyInvocation switchToFinalFrenzyInvocation = gson.fromJson(message,
                  SwitchToFinalFrenzyInvocation.class);
              getPlayerDashboardsView().switchToFinalFrenzy(switchToFinalFrenzyInvocation
                  .getPlayerColor());
              break;
            case SHOW_POWER_UP_SELECTION_INVOCATION:
              ShowPowerUpSelectionInvocation showPowerUpSelectionInvocation = gson.fromJson(message,
                  ShowPowerUpSelectionInvocation.class);
              getPlayerDashboardsView().showPowerUpSelection(showPowerUpSelectionInvocation
                  .getPowerUps(), showPowerUpSelectionInvocation.isDiscard());
              break;
            case SHOW_TURN_ACTION_SELECTION_INVOCATION:
              ShowTurnActionSelectionInvocation showTurnActionSelectionInvocation = gson
                  .fromJson(message, ShowTurnActionSelectionInvocation.class);
              getPlayerDashboardsView().showTurnActionSelection(showTurnActionSelectionInvocation
                  .getActions());
              break;
            case SHOW_SPAWN_POINT_TRACK_SELECTION_INVOCATION:
              getBoardView().showSpawnPointTrackSelection();
              break;
            case SHOW_SWAP_WEAPON_SELECTION_INVOCATION:
              ShowSwapWeaponSelectionInvocation showSwapWeaponSelectionInvocation = gson
                  .fromJson(message, ShowSwapWeaponSelectionInvocation.class);
              getPlayerDashboardsView().showSwapWeaponSelection(showSwapWeaponSelectionInvocation
                  .getOwnWeapons(), showSwapWeaponSelectionInvocation.getSquareWeapons());
              break;
            case SHOW_MESSAGE_INVOCATION:
              ShowMessageInvocation showMessageInvocation = gson
                  .fromJson(message, ShowMessageInvocation.class);
              showMessage(showMessageInvocation.getSeverity(),
                  showMessageInvocation.getTitle(), showMessageInvocation.getMessage());
              break;
            case SHOW_FINAL_RANKS_INVOCATION:
              getBoardView().showFinalRanks();
              break;
            case SHOW_RELOAD_WEAPON_SELECTION_INVOCATION:
              ShowReloadWeaponSelectionInvocation showReloadWeaponSelectionInvocation = gson
                  .fromJson(message, ShowReloadWeaponSelectionInvocation.class);
              getPlayerDashboardsView().showReloadWeaponSelection(
                  showReloadWeaponSelectionInvocation.getUnloadedWeapons());
              break;
            default:
              getBoardView().update(event);
              getCharactersView().update(event);
              getPlayerDashboardsView().update(event);
          }
        }
      } catch (IOException e) {
        disconnect("La connessione con il server è stata persa!");
      }
    }
  }
}
