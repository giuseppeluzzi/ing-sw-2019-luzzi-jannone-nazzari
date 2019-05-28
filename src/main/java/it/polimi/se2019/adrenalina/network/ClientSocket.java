package it.polimi.se2019.adrenalina.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.se2019.adrenalina.controller.Configuration;
import it.polimi.se2019.adrenalina.controller.MessageSeverity;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.event.PlayerConnectEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerSetColorEvent;
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
    charactersView = new TUICharactersView(this);
    playerDashboardsView = new TUIPlayerDashboardsView(this);

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

          if (eventType == EventType.PLAYER_SET_COLOR) {
            PlayerSetColorEvent playerSetColorEvent = gson.fromJson(message,
                PlayerSetColorEvent.class);
            setPlayerColor(playerSetColorEvent.getPlayerColor());
          } else {
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
