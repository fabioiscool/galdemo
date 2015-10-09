package simulator.controllers;

/**
 * Trida pro objekty pro panel promennych v ridici jednotce simulatoru
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public abstract class VariableObject {
    protected boolean enabled;
    protected boolean show;
    
    public VariableObject() {
      this(true);
    }
    
    public VariableObject(boolean enabled) {
      this(enabled, true);
    }
    
    public VariableObject(boolean enabled, boolean show) {
      this.enabled = enabled;
      this.show = show;
    }
    
    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public boolean isShow() {
      return show;
    }

    public void setShow(boolean show) {
      this.show = show;
    }
    
    public abstract String getVariableValue();

    @Override
    public String toString() {
      if (enabled && show){
        return getVariableValue();
      }
      return "";
    }
  }
