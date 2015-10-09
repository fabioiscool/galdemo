package simulator.controllers;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.Timer;

/**
 * Upravena trida timeru pro ucely ridici jednotky
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class ControllerTimer extends Timer {
  boolean enable = true;
  boolean wasRunning = false;

  public ControllerTimer(int delay, ActionListener listener) {
    super(delay, listener);
  }

  public boolean isEnable() {
    return enable;
  }

  public void setEnabled(boolean enableTimer) {
    if (enableTimer == this.enable){
      return;
    }
    this.enable = enableTimer;
    if (this.enable == false){
      wasRunning = this.isRunning();
      if (wasRunning){
        this.stop();
      }
    } else if (wasRunning){
      this.start();
    }
  }

  public static final String PROP_TIMER_RUNNING = "timerRunning";

  private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }
  
  @Override
  public void stop() {
    boolean running = isRunning();
    wasRunning = false;
    super.stop();
    propertyChangeSupport.firePropertyChange(PROP_TIMER_RUNNING, running, false);
  }

  @Override
  public void start() {
    wasRunning = true;
    if (enable){
      boolean running = isRunning();
      super.start();
      propertyChangeSupport.firePropertyChange(PROP_TIMER_RUNNING, running, true);
    }
  }
}
