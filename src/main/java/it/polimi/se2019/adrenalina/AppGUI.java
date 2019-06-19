package it.polimi.se2019.adrenalina;

import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.network.ClientRMI;
import it.polimi.se2019.adrenalina.network.ClientSocket;
import it.polimi.se2019.adrenalina.ui.graphic.controller.BoardFXController;
import it.polimi.se2019.adrenalina.ui.graphic.controller.LobbyFXController;
import it.polimi.se2019.adrenalina.ui.graphic.controller.StartFXController;
import it.polimi.se2019.adrenalina.utils.Log;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppGUI extends Application {

  private static Stage stage;
  private static ClientInterface client;

  private static StartFXController startFXController = null;
  private static LobbyFXController lobbyFXController = null;
  private static BoardFXController boardFXController = null;

  public static void main(String... args) {
    launch(args);
  }

  public static void startClient(String name, boolean domination, boolean socket) {
    new Thread(() -> {
      if (socket) {
        client = new ClientSocket(name, domination, false);
      } else {
        ClientRMI clientRMI = new ClientRMI(name, domination, false);
        try {
          client = (ClientInterface) UnicastRemoteObject.exportObject(clientRMI, 0);
          clientRMI.getServer().addClient(client);
        } catch (RemoteException e) {
          Log.exception(e);
        } catch (InvalidPlayerException ignored) {
          //
        }
      }

      if (client != null) {
        try {
          client.ping();
        } catch (IOException e) {
          Log.exception(e);
        }
      }
    }).start();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    setStage(primaryStage);

    FXMLLoader loaderStart = new FXMLLoader(AppGUI.class.getClassLoader().getResource("gui/Start.fxml"));
    startFXController = loaderStart.getController();

    Scene startScene = new Scene(loaderStart.load());
    startScene.getStylesheets().addAll(getCSS());

    primaryStage.setResizable(false);
    primaryStage.setTitle("Adrenalina");
    primaryStage.setScene(startScene);
    primaryStage.show();
  }

  public static ClientInterface getClient() {
    return client;
  }

  public static Stage getStage() {
    return stage;
  }

  private static void setStage(Stage stage) {
    AppGUI.stage = stage;
  }

  public static String getCSS() {
    return AppGUI.class
        .getClassLoader().getResource("gui/assets/style.css").toExternalForm();
  }

  public static Scene getLobbyScene() throws IOException {
    FXMLLoader loaderLobby = new FXMLLoader(
        AppGUI.class.getClassLoader().getResource("gui/Lobby.fxml"));
    Scene scene = new Scene(loaderLobby.load());
    lobbyFXController = loaderLobby.getController();
    return scene;
  }

  public static Scene getBoardScene() throws IOException {
    FXMLLoader loaderBoard = new FXMLLoader(
        AppGUI.class.getClassLoader().getResource("gui/Board.fxml"));
    Scene scene = new Scene(loaderBoard.load());
    boardFXController = loaderBoard.getController();
    return scene;
  }

  public static LobbyFXController getLobbyFXController() {
    return lobbyFXController;
  }

  public static BoardFXController getBoardFXController() {
    return boardFXController;
  }

  public static void setLobbyFXController(
      LobbyFXController lobbyFXController) {
    AppGUI.lobbyFXController = lobbyFXController;
  }

  public static void setBoardFXController(
      BoardFXController boardFXController) {
    AppGUI.boardFXController = boardFXController;
  }
}