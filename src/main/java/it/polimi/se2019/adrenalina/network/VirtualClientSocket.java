package it.polimi.se2019.adrenalina.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.se2019.adrenalina.controller.BoardController;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.EventType;
import it.polimi.se2019.adrenalina.controller.event.PlayerConnectEvent;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardViewInterface;
import it.polimi.se2019.adrenalina.view.CharactersViewInterface;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsViewInterface;
import it.polimi.se2019.adrenalina.view.VirtualBoardView;
import it.polimi.se2019.adrenalina.view.VirtualCharactersView;
import it.polimi.se2019.adrenalina.view.VirtualPlayerDashboardsView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;

public class VirtualClientSocket implements ClientInterface, Runnable {

  private final Socket clientSocket;
  private final Server server;

  private String name;
  private PlayerColor playerColor;
  private boolean domination;

  private PrintWriter printWriter;
  private BufferedReader bufferedReader;

  private BoardController game;

  private BoardViewInterface boardView;
  private CharactersViewInterface charactersView;
  private PlayerDashboardsViewInterface playerDashboardsView;

  private static final String UPDATE_EVENT_METHOD = "update";

  public VirtualClientSocket(Server server, Socket clientSocket) {
    this.clientSocket = clientSocket;
    this.server = server;

    boardView = new VirtualBoardView(this);
    charactersView = new VirtualCharactersView(this);
    playerDashboardsView = new VirtualPlayerDashboardsView(this);

    try {
      bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),
          StandardCharsets.UTF_8));
      OutputStream outputStream = clientSocket.getOutputStream();
      printWriter = new PrintWriter(outputStream, true);
      bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),
          StandardCharsets.UTF_8));
    } catch (IOException e) {
      Log.exception(e);
    }
  }

  @Override
  public void run() {
    try {
      while (clientSocket.isConnected()) {
        String message = bufferedReader.readLine();

        Gson gson = new Gson();
        JsonObject json = gson.fromJson(message, JsonObject.class);

        EventType eventType = EventType.valueOf(json.get("eventType").getAsString());
        Event event = gson.fromJson(message, eventType.getEventClass());

        switch (eventType) {
          case PLAYER_CONNECT_EVENT:
            PlayerConnectEvent connectEvent = gson.fromJson(message, PlayerConnectEvent.class);
            name = connectEvent.getPlayerName();
            domination = connectEvent.isDomination();
            server.addClient(this);
            game = server.getGameByClient(this);
            break;
          case PLAYER_ATTACK_EVENT:
          case PLAYER_RELOAD_EVENT:
            playerDashboardsView.getClass()
                .getMethod(UPDATE_EVENT_METHOD, eventType.getEventClass())
                .invoke(playerDashboardsView, event);
            break;
          case SELECT_PLAYER_EVENT:
          case SELECT_SQUARE_EVENT:
          case SELECT_DIRECTION_EVENT:
            boardView.getClass()
                .getMethod(UPDATE_EVENT_METHOD, eventType.getEventClass()).invoke(boardView, event);
            break;
          case PLAYER_MOVE_EVENT:
          case PLAYER_COLLECT_AMMO_EVENT:
          case PLAYER_COLLECT_WEAPON_EVENT:
          case PLAYER_POWERUP_EVENT:
            charactersView.getClass()
                .getMethod(UPDATE_EVENT_METHOD, eventType.getEventClass())
                .invoke(charactersView, event);
            break;
          default:
            Log.severe("Unexpected server event!");
            break;
        }
      }
    } catch (InvalidPlayerException | IOException ignored) {
      server.onClientDisconnect(this);
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
      Log.severe("Unexpected server event!");
    }
  }

  @Override
  public String getName() {
    // TODO: check if connected
    return name;
  }

  @Override
  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public void setPlayerColor(PlayerColor color) {
    playerColor = color;
  }

  @Override
  public boolean isDomination() {
    // TODO: check if connected
    return domination;
  }

  @Override
  public void setDomination(boolean domination) {
    this.domination = domination;
  }

  @Override
  public void showMessage(String text) {
    printWriter.println(text);
    printWriter.flush();
  }

  @Override
  public void ping() {
    if (clientSocket.isClosed() || !clientSocket.isConnected()) {
      server.onClientDisconnect(this);
    }
  }

  @Override
  public void disconnect() {
    try {
      bufferedReader.close();
      printWriter.close();
      clientSocket.close();
    } catch (IOException e) {
      Log.exception(e);
    }
  }

  public void setGame(BoardController game) {
    this.game = game;
  }

  public void setBoardView(BoardViewInterface boardView) {
    this.boardView = boardView;
  }

  public void setCharactersView(CharactersViewInterface charactersView) {
    this.charactersView = charactersView;
  }

  public void setPlayerDashboardsView(
      PlayerDashboardsViewInterface playerDashboardsView) {
    this.playerDashboardsView = playerDashboardsView;
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

  public void sendEvent(Event event) {
    printWriter.println(event.serialize());
    printWriter.flush();
  }
}
