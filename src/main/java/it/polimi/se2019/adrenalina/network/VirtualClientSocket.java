package it.polimi.se2019.adrenalina.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.EventType;
import it.polimi.se2019.adrenalina.controller.event.PlayerConnectEvent;
import it.polimi.se2019.adrenalina.utils.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;

public class VirtualClientSocket implements ClientInterface, Runnable {
  private String name;
  private boolean domination;

  private final Socket clientSocket;
  private final Server server;

  private PrintWriter printWriter;
  private BufferedReader bufferedReader;


  public VirtualClientSocket(Server server, Socket clientSocket) {
    this.clientSocket = clientSocket;
    this.server = server;

    try {
      bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "utf-8"));
      OutputStream outputStream = clientSocket.getOutputStream();
      printWriter = new PrintWriter(outputStream, true);
      bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "utf-8"));
    } catch (IOException e) {
      Log.severe("Socket", "IO Error: " + e.getMessage());
    }
  }

  @Override
  public void run() {
    try {
      while (clientSocket.isConnected()) {
        String message = bufferedReader.readLine();
        //Log.info(message);

        Gson gson = new Gson();
        JsonObject json = gson.fromJson(message, JsonObject.class);

        switch (EventType.valueOf(json.get("eventType").getAsString())) {
          case PLAYER_CONNECT_EVENT:
            PlayerConnectEvent event = gson.fromJson(message, PlayerConnectEvent.class);
            name = event.getPlayerName();
            domination = event.isDomination();

            server.addClient(this);
            break;

          default:
            Log.severe("Unexpected event!");
            break;
        }
      }
    } catch (RemoteException e) {
      Log.severe("RMI", "Connection error: " + e.getMessage());
    } catch (IOException e) {
      Log.severe("Socket", "IO Error: " + e.getMessage());
    }
  }

  @Override
  public String getName() {
    // TODO: check if connected
    return name;
  }

  @Override
  public boolean isDomination() {
    // TODO: check if connected
    return domination;
  }

  @Override
  public void showMessage(String text) {
    printWriter.println(text);
    printWriter.flush();
  }

  @Override
  public void ping() {
    // useless
  }

  public void sendEvent(Event event){
    printWriter.println(event.serialize());
    printWriter.flush();
  }
}
