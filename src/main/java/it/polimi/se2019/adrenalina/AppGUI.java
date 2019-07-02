package it.polimi.se2019.adrenalina;

import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.network.ClientRMI;
import it.polimi.se2019.adrenalina.network.ClientSocket;
import it.polimi.se2019.adrenalina.ui.graphic.controller.BoardFXController;
import it.polimi.se2019.adrenalina.ui.graphic.controller.LobbyFXController;
import it.polimi.se2019.adrenalina.ui.graphic.controller.PlayerDashboardFXController;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class of the application running as GUI client.
 */
public class AppGUI extends Application {

  private static Stage stage = null;
  private static ClientInterface client = null;

  private static Scene lobbyScene;
  private static Scene boardScene;

  private static LobbyFXController lobbyFXController = null;
  private static BoardFXController boardFXController = null;

  @Override
  public void start(Stage primaryStage) throws Exception {
    setStage(primaryStage);
    Log.setName("ClientGUI");

    primaryStage.setOnCloseRequest(windowEvent -> {
      try {
        stop();
      } catch (Exception ignored) {
        //
      }
    });

    FXMLLoader loaderLobby = new FXMLLoader(
        AppGUI.class.getClassLoader().getResource("gui/Lobby.fxml"));
    setLobbyScene(new Scene(loaderLobby.load()));
    setLobbyFXController(loaderLobby.getController());

    FXMLLoader loaderStart = new FXMLLoader(
        AppGUI.class.getClassLoader().getResource("gui/Start.fxml"));

    FXMLLoader loaderBoard = new FXMLLoader(
        AppGUI.class.getClassLoader().getResource("gui/Board.fxml"));
    setBoardFXController(new BoardFXController());
    loaderBoard.setController(getBoardFXController());

    setBoardScene(new Scene(loaderBoard.load()));
    getBoardScene().getStylesheets().addAll(getCSS());

    Scene startScene = new Scene(loaderStart.load());
    startScene.getStylesheets().addAll(getCSS());
    lobbyScene.getStylesheets().addAll(getCSS());

    primaryStage.setResizable(false);
    primaryStage.setTitle("Adrenalina");

    /*primaryStage.setScene(boardScene);
    boardFXController.loadPlayerDashboard(PlayerColor.YELLOW);
    boardFXController.loadEnemyDashboard(PlayerColor.PURPLE);
    boardFXController.loadEnemyDashboard(PlayerColor.GREEN);
    boardFXController.getDashboardController(PlayerColor.GREEN).updateAmmos(1,1,1);
    boardFXController.getDashboardController(PlayerColor.YELLOW).updateAmmos(1,1,1);
    /*boardFXController.loadEnemyDashboard(PlayerColor.BLUE);
    boardFXController.loadEnemyDashboard(PlayerColor.GREY);*/
    /*boardFXController.updateKilltrack(Arrays.asList(new Kill[]{
        new Kill(PlayerColor.GREEN, false),
        new Kill(PlayerColor.GREEN, true),
        new Kill(PlayerColor.GREEN, true),
        new Kill(PlayerColor.GREEN, true),
        new Kill(PlayerColor.YELLOW, false)
    }), 8);/*
    boardFXController.updateSpawnpointDamages(AmmoColor.RED, Arrays.asList(new PlayerColor[] {
        PlayerColor.YELLOW,
        PlayerColor.BLUE,
        PlayerColor.GREEN,
        PlayerColor.GREEN,
        PlayerColor.GREEN,
        PlayerColor.PURPLE
    }));
    boardFXController.updateSpawnpointDamages(AmmoColor.BLUE, Arrays.asList(new PlayerColor[] {
        PlayerColor.YELLOW,
        PlayerColor.BLUE,
        PlayerColor.BLUE,
        PlayerColor.BLUE,
        PlayerColor.GREEN,
        PlayerColor.PURPLE
    }));
    boardFXController.updateSpawnpointDamages(AmmoColor.YELLOW, Arrays.asList(new PlayerColor[] {
        PlayerColor.YELLOW,
        PlayerColor.YELLOW,
        PlayerColor.YELLOW,
        PlayerColor.YELLOW,
        PlayerColor.YELLOW,
        PlayerColor.BLUE,
        PlayerColor.GREEN,
        PlayerColor.PURPLE
    }));
    boardFXController.getDashboardController(PlayerColor.YELLOW).updateDamages(Arrays.asList(new PlayerColor[] {
        PlayerColor.YELLOW,
        PlayerColor.YELLOW,
        PlayerColor.YELLOW,
    }));
    /*boardFXController.getDashboardController(PlayerColor.GREEN).updateDamages(Arrays.asList(new PlayerColor[] {
        PlayerColor.YELLOW,
        PlayerColor.YELLOW,
        PlayerColor.YELLOW,
        PlayerColor.YELLOW,
        PlayerColor.YELLOW,
        PlayerColor.BLUE,
        PlayerColor.GREEN,
        PlayerColor.GREEN,
        PlayerColor.GREEN,
        PlayerColor.GREEN,
        PlayerColor.GREEN,
        PlayerColor.PURPLE
    }));
    boardFXController.getDashboardController(PlayerColor.YELLOW).updateTags(Arrays.asList(new PlayerColor[] {
        PlayerColor.YELLOW,
        PlayerColor.YELLOW,
        PlayerColor.YELLOW,
        PlayerColor.YELLOW,
        PlayerColor.YELLOW,
        PlayerColor.BLUE,
        PlayerColor.GREEN,
        PlayerColor.PURPLE,
        PlayerColor.PURPLE,
        PlayerColor.PURPLE,
        PlayerColor.PURPLE,
        PlayerColor.PURPLE
    }));
    boardFXController.getDashboardController(PlayerColor.GREEN).updateTags(Arrays.asList(new PlayerColor[] {
        PlayerColor.YELLOW,
        PlayerColor.YELLOW,
        PlayerColor.YELLOW,
        PlayerColor.YELLOW,
        PlayerColor.YELLOW,
        PlayerColor.BLUE,
        PlayerColor.BLUE,
        PlayerColor.BLUE,
        PlayerColor.BLUE,
        PlayerColor.BLUE,
        PlayerColor.GREEN,
        PlayerColor.PURPLE
    }));
    boardFXController.getDashboardController(PlayerColor.GREEN).updateSkulls(2);*/
    //boardFXController.getDashboardController(PlayerColor.YELLOW).updateSkulls(2);
    primaryStage.setScene(startScene);
    primaryStage.show();
  }

  public static void startClient(String ipAddress, int port, String name, boolean domination,
      boolean socket) {
    new Thread(() -> {
      if (socket) {
        client = new ClientSocket(ipAddress, port, name, domination, false);
        ((Runnable) client).run();
      } else {
        ClientRMI clientRMI = new ClientRMI(ipAddress, port, name, domination, false);
        try {
          client = (ClientInterface) UnicastRemoteObject.exportObject(clientRMI, 0);
          clientRMI.getServer().addClient(client);
        } catch (RemoteException e) {
          Log.exception(e);
        } catch (InvalidPlayerException ignored) {
          //
        }
      }
    }).start();
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

  private static void setLobbyScene(Scene scene) {
    lobbyScene = scene;
  }

  public static Scene getLobbyScene() {
    return lobbyScene;
  }

  private static void setBoardScene(Scene scene) {
    boardScene = scene;
  }

  public static Scene getBoardScene() {
    return boardScene;
  }

  public static LobbyFXController getLobbyFXController() {
    return lobbyFXController;
  }

  public static BoardFXController getBoardFXController() {
    return boardFXController;
  }

  public static PlayerDashboardFXController getPlayerDashboardFXController() {
    try {
      return (PlayerDashboardFXController) boardFXController
          .getDashboardController(client.getPlayerColor());
    } catch (InvalidPlayerException ignored) {
      //
    } catch (RemoteException e) {
      Log.exception(e);
    }

    return null;
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
