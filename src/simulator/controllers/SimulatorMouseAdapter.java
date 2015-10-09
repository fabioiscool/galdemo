package simulator.controllers;

import com.mxgraph.model.mxCell;
import java.awt.event.MouseAdapter;
import java.util.HashMap;
import java.util.Map;

/**
 * Zaklani trida pro praci s mysi v grafu
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class SimulatorMouseAdapter extends MouseAdapter{
  protected boolean activated = false;
  protected Map<mxCell, String> oldStyles;

  public SimulatorMouseAdapter() {
    oldStyles = new HashMap<>();
  }
  
  public boolean isActivated() {
    return activated;
  }

  public void setActivated(boolean activated) {
    this.activated = activated;
  }
  
  public void resetStyles(){
    for (Map.Entry<mxCell, String> entry : oldStyles.entrySet()){
      entry.getKey().setStyle(entry.getValue());
    }
  }
  
  public void reset(){
    oldStyles.clear();
  }
}
