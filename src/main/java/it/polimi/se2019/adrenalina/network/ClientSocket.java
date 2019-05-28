package it.polimi.se2019.adrenalina.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.se2019.adrenalina.controller.Configuration;
import it.polimi.se2019.adrenalina.controller.MessageSeverity;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.event.PlayerConnectEvent;
import it.polimi.se2019.adrenalina.event.invocations.ShowBuyableWeaponsInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowDeathInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowDirectionSelectInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowEffectSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowPaymentOptionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowPowerUpSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowSpawnPointTrackSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowSquareSelectInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowTargetSelectInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowTurnActionSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowWeaponSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.SwitchToFinalFrenzyInvocation;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerSetColorEvent;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.ui.text.TUIBoardView;
import it.polimi.se2019.adrenalina.ui.text.TUICharactersView;
import it.polimi.se2019.adrenalina.ui.text.TUIPlayerDashboardsView;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardViewInterface;
import it.polimi.se2019.adrenalina.view.CharactersViewInterface;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsViewInterface;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ClientSocket extends Client implements Runnable {

  private static final long serialVersionUID = 5069992236971339205L;
  private transient Socket socket;
  private transient PrintWriter printWriter;
  private transient BufferedReader bufferedReader;

  private final transient BoardViewInterface boardView;
  private final transient CharactersViewInterface charactersView;
  private final transient PlayerDashboardsViewInterface playerDashboardsView;

  public ClientSocket(String name, boolean domination) {
    super(name, domination);

    boardView = new TUIBoardView(this);
    charactersView = new TUICharactersView(this, boardView);
    playerDashboardsView = new TUIPlayerDashboardsView(this, boardView);

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
  public void showMessage(MessageSeverity severity, String title, String message) {
    Log.println(severity + ": " + title);
    Log.println(message);
  }

  @Override
  public void disconnect() {
    try {
      bufferedReader.close();
      printWriter.close();
      socket.close();
    } catch (IOException e) {
      Log.exception(e);
    }
  }

  @Override
  public BoardViewInterface getBoardView() {
    return boardView;
  }

  @Override
  public CharactersViewInterface getCharactersView() {
    return charactersView;
  }

  @Override
  public PlayerDashboardsViewInterface getPlayerDashboardsView() {
    return playerDashboardsView;
  }

  public final void sendEvent(Event event) {
    printWriter.println(event.serialize());
    printWriter.flush();
  }

  @Override
  public final void run() {
    if (socket != null) {
      try {
        while (socket.isConnected()) {
          String message = bufferedReader.readLine();

          Gson gson = new Gson();
          JsonObject json = gson.fromJson(message, JsonObject.class);

          EventType eventType = EventType.valueOf(json.get("eventType").getAsString());
          Event event = gson.fromJson(message, eventType.getEventClass());

          switch (eventType) {
            case PLAYER_SET_COLOR:
              PlayerSetColorEvent playerSetColorEvent = gson.fromJson(message,
                  PlayerSetColorEvent.class);
              setPlayerColor(playerSetColorEvent.getPlayerColor());
              break;
            case SHOW_BOARD_INVOCATION:
              boardView.showBoard();
              break;
            case SHOW_DEATH_INVOCATION:
              ShowDeathInvocation showDeathInvocation = gson.fromJson(message,
                  ShowDeathInvocation.class);
              charactersView.showDeath(showDeathInvocation.getPlayerColor());
              break;
            case SHOW_SQUARE_SELECT_INVOCATION:
              ShowSquareSelectInvocation showSquareSelectInvocation = gson.fromJson(message,
                  ShowSquareSelectInvocation.class);
              boardView.showSquareSelect(showSquareSelectInvocation.getTargets());
              break;
            case SHOW_TARGET_SELECT_INVOCATION:
              ShowTargetSelectInvocation showTargetSelectInvocation = gson.fromJson(message,
                  ShowTargetSelectInvocation.class);
              boardView.showTargetSelect(showTargetSelectInvocation.getTargetType(),
                  showTargetSelectInvocation.getTargets());
              break;
            case SHOW_PAYMENT_OPTION_INVOCATION:
              ShowPaymentOptionInvocation showPaymentOptionInvocation = gson.fromJson(message,
                  ShowPaymentOptionInvocation.class);
              playerDashboardsView.showPaymentOption(showPaymentOptionInvocation.getItem());
              break;
            case SHOW_BUYABLE_WEAPONS_INVOCATION:
              ShowBuyableWeaponsInvocation showBuyableWeaponsInvocation = gson.fromJson(message,
                  ShowBuyableWeaponsInvocation.class);
              boardView.showBuyableWeapons(showBuyableWeaponsInvocation.getWeaponList());
              break;
            case SHOW_DIRECTION_SELECT_INVOCATION:
              boardView.showDirectionSelect();
              break;
            case SHOW_EFFECT_SELECTION_INVOCATION:
              ShowEffectSelectionInvocation showEffectSelectionInvocation = gson.fromJson(message,
                  ShowEffectSelectionInvocation.class);
              playerDashboardsView.showEffectSelection(showEffectSelectionInvocation.getWeapon());
              break;
            case SHOW_WEAPON_SELECTION_INVOCATION:
              ShowWeaponSelectionInvocation showWeaponSelectionInvocation = gson.fromJson(message,
                  ShowWeaponSelectionInvocation.class);
              playerDashboardsView.showWeaponSelection(showWeaponSelectionInvocation.getWeapons());
              break;
            case SWITCH_TO_FINAL_FRENZY_INVOCATION:
              SwitchToFinalFrenzyInvocation switchToFinalFrenzyInvocation = gson.fromJson(message,
                  SwitchToFinalFrenzyInvocation.class);
              playerDashboardsView.switchToFinalFrenzy(switchToFinalFrenzyInvocation
                  .getPlayerColor());
              break;
            case SHOW_POWER_UP_SELECTION_INVOCATION:
              ShowPowerUpSelectionInvocation showPowerUpSelectionInvocation = gson.fromJson(message,
                  ShowPowerUpSelectionInvocation.class);
              playerDashboardsView.showPowerUpSelection(showPowerUpSelectionInvocation
                  .getPowerUps());
              break;
            case SHOW_TURN_ACTION_SELECTION_INVOCATION:
              ShowTurnActionSelectionInvocation showTurnActionSelectionInvocation = gson
                  .fromJson(message, ShowTurnActionSelectionInvocation.class);
              playerDashboardsView.showTurnActionSelection(showTurnActionSelectionInvocation
                  .getActions());
              break;
            case SHOW_SPAWN_POINT_TRACK_SELECTION_INVOCATION:
              boardView.showSpawnPointTrackSelection();
              break;
            default:
              boardView.update(event);
              charactersView.update(event);
              playerDashboardsView.update(event);
          }
        }
      } catch (IOException e) {
        Log.exception(e);
      }
    }
  }
}
