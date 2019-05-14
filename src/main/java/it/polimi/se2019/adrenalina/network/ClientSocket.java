package it.polimi.se2019.adrenalina.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.se2019.adrenalina.controller.Configuration;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.EventType;
import it.polimi.se2019.adrenalina.controller.event.PlayerConnectEvent;
import it.polimi.se2019.adrenalina.ui.text.TUIBoardView;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardViewInterface;
import it.polimi.se2019.adrenalina.view.CharactersView;
import it.polimi.se2019.adrenalina.view.CharactersViewInterface;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsView;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsViewInterface;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientSocket implements ClientInterface, Runnable {
  private final String name;
  private final boolean domination;

  private Socket socket;

  private PrintWriter printWriter;
  private BufferedReader bufferedReader;

  private BoardViewInterface boardView;
  private CharactersViewInterface charactersView;
  private PlayerDashboardsViewInterface playerDashboardsView;

  public ClientSocket(String name, boolean domination) {
    this.name = name;
    this.domination = domination;

    boardView = new TUIBoardView();
    charactersView = new CharactersView();
    playerDashboardsView = new PlayerDashboardsView();

    try {
      socket = new Socket(Configuration.getInstance().getServerIP(), Configuration.getInstance().getSocketPort());
      OutputStream outputStream = socket.getOutputStream();
      printWriter = new PrintWriter(outputStream, true);
      bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),
          StandardCharsets.UTF_8));
    } catch (IOException e) {
      Log.exception(e);
      return ;
    }

    sendEvent(new PlayerConnectEvent(name, domination));
    run();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isDomination() {
    return domination;
  }

  @Override
  public void showMessage(String text) {
    Log.info(text);
  }

  @Override
  public void ping() {
    // useless
  }

  @Override
  public void disconnect()  {
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
            case TIMER_SET_EVENT:
            case WEAPON_UPDATE_EVENT:
            case AMMO_CARD_UPDATE_EVENT:
            case KILLSHOT_EVENT:
            case DOUBLE_KILL_EVENT:
            case SPAWN_POINT_DAMAGE_EVENT:
              boardView.getClass().
                  getMethod("update", eventType.getEventClass()).invoke(boardView, event);
              break;

            case PLAYER_DEATH_EVENT:
            case PLAYER_SPAWN_EVENT:
            case PLAYER_MOVE_EVENT:
              charactersView.getClass().
                  getMethod("update", eventType.getEventClass()).invoke(charactersView, event);
              break;

            default:
              Log.severe("Unexpected client event!");
              break;
          }
        }
      } catch (IOException e) {
        Log.exception(e);
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
        Log.severe("Unexpected client event!");
      }

    }
  }
}
