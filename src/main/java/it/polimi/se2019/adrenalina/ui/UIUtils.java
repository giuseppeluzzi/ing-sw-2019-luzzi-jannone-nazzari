package it.polimi.se2019.adrenalina.ui;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.model.Kill;

import java.util.List;

/**
 * UI utilities, common for TUI and GUI.
 */
public class UIUtils {

    private UIUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static int getFirstKillshotIndex(List<Kill> kills, PlayerColor playerColor) {
        for (int i = 0; i < kills.size(); i++) {
            if (kills.get(i).getPlayerColor() == playerColor) {
                return i;
            }
        }
        return -1;
    }
}
