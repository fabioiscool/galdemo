package simulator.graphs.bellmanford;

import java.text.DecimalFormat;
import simulator.graphs.AbstractGraphCell;
import simulator.graphs.BasicNodeData;

/**
 * Objekt pro vlozeni do hrany u BellmanFordova algoritmu.
 * @author JVaradinek
 */
public class BellmanFordGraphEdge extends AbstractGraphCell {
  protected BasicNodeData state;
  
  public BellmanFordGraphEdge() {
    this(null, false);
  }
  
  public BellmanFordGraphEdge(Double distance) {
    this(distance, true);
  }
  
  public BellmanFordGraphEdge(boolean enabled) {
    this(null, enabled);
  }
  
  public BellmanFordGraphEdge(Double distance, boolean enabled) {
    this.setDistance(distance);
    state = new BasicNodeData();
    state.setEnabled(enabled);
  }
  
  @Override
  public String getLabel() {
    return new DecimalFormat("#").format(this.getDistance());
  }

  @Override
  public String getToolTips() {
    return "";
  }

  @Override
  public void clearState() {
    state = null;
  }

  @Override
  public boolean isEnabled() {
    return state.isEnabled();
  }

  @Override
  public void setEnabled(boolean enabled) {
    state.setEnabled(enabled);
  }

  @Override
  public boolean isActive() {
    return state.isActive();
  }

  @Override
  public void setActive(boolean active) {
    state.setActive(active);
  }

  @Override
  public void resetState() {
    state.resetState();
  }

  @Override
  public void setToolTipsEnabled(boolean toolTipsEnabled) {
    state.setToolTipsEnabled(toolTipsEnabled);
  }

  @Override
  public boolean isToolTipsEnabled() {
    return state.isToolTipsEnabled();
  }
  
}
