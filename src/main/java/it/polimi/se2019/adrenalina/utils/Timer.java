package it.polimi.se2019.adrenalina.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Timer implements Serializable {

  private static final long serialVersionUID = -1689799953239659194L;
  private volatile int timerSeconds;
  private final Object timerLock = new Object();
  private Thread timerThread;

  public Timer() {
    timerSeconds = 0;
    timerThread = null;
  }

  /**
   * Returns remaining seconds for this timerSeconds
   * @return remaining seconds
   */
  public int getRemainingSeconds() {
    return timerSeconds;
  }

  /**
   * Starts a timerSeconds
   * @param seconds length of the timerSeconds
   */
  public void start(int seconds) {
    start(seconds, null);
  }

  /**
   * Starts a timerSeconds and executes the runnable at the end of the timerSeconds
   * @param seconds length of the timerSeconds
   * @param runnable function to be executed
   */
  public void start(int seconds, Runnable runnable) {
    synchronized (timerLock) {
      if (timerSeconds != 0) {
        timerThread.interrupt();
        timerThread = null;
      }
      timerSeconds = seconds;

      timerThread = new Thread(() -> {
        while (true) {
          synchronized (timerLock) {
            if (timerSeconds <= 0) {
              if (runnable != null) {
                runnable.run();
              }
              break;
            }
            Log.info("Timer", "" + timerSeconds);
            timerSeconds--;
            try {
              timerLock.wait(1000);
            } catch (InterruptedException e) {
              timerThread.interrupt();
            }
          }
        }
      });
      timerThread.start();
    }
  }

  /**
   * Stops a running timerSeconds
   */
  public void stop() {
    synchronized (timerLock) {
      if (timerThread != null) {
        timerThread.interrupt();
      }
    }
  }

  private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
  }

  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    synchronized (timerLock) {
      timerThread = null;
    }
  }
}
