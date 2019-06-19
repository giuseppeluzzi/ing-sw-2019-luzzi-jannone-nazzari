package it.polimi.se2019.adrenalina.ui.graphic.controller;

import javafx.animation.TranslateTransition;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class FXUtils {

  public static final int TRANSLATE_OUT = 2000;
  public static final int TRANSLATE_DURATION = 500;

  public static void transition(BorderPane from, BorderPane to) {
    TranslateTransition transOut = new TranslateTransition(Duration.millis(TRANSLATE_DURATION),
        from);
    transOut.setToX(-TRANSLATE_OUT);
    transOut.play();

    if (to != null) {
      double initialX = to.getLayoutX();
      to.setTranslateX(TRANSLATE_OUT);
      to.setVisible(true);

      TranslateTransition transIn = new TranslateTransition(Duration.millis(TRANSLATE_DURATION),
          to);
      transIn.setToX(initialX);
      transIn.play();
    }
  }
}
