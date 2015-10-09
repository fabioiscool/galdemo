package simulator.graphs.bellmanford;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import simulator.graphs.AbstractGraphCell;

/**
 * Objekt pro vlozeni do vrcholu u BellmanFordova algoritmu
 * @author JVaradinek
 */
public class BellmanFordGraphVertex extends AbstractGraphCell implements BellmanFordOperations{
  
  protected BellmanFordNodeData state = null;
   
  public BellmanFordGraphVertex() {
    this(null);
  }
  
  public BellmanFordGraphVertex(String name) {
    this(name, false);
  }
  
  public BellmanFordGraphVertex(String name, Boolean selected) {
    this(name, new BellmanFordNodeData(), selected);
  }
  
  public BellmanFordGraphVertex(String name, BellmanFordNodeData state, Boolean selected) {
    super(name, selected);
    this.state = state;
  }
  
  @Override
  public String getLabel() {
    if (state.isEnabled() == false || state.getDistance() == null){
      return this.getName();
    }
    if (state.getDistance() == Double.POSITIVE_INFINITY){
      return this.getName() + " / ∞";
    }
    StringBuilder sb = new StringBuilder(this.getName());
    sb.append(" / ");
    if (state.getDistance() != null){
      sb.append(new DecimalFormat("#").format(state.getDistance()));
    } 
    return sb.toString();
  }

  @Override
  public String getToolTips() {
    if (state.isEnabled() == false || state.isToolTipsEnabled() == false){
      return "π = ";
    }
    if (state.getNodePath() == null){
      return "π = NIL";
    }
    return "π = " + state.getNodePath().getName();
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

  @Override
  public void setDistance(Double distance) {
    state.setDistance(distance);
  }

  @Override
  public Double getDistance() {
    return state.getDistance();
  }

  @Override
  public BellmanFordGraphVertex getNodePath() {
    return state.getNodePath();
  }

  @Override
  public void setNodePath(BellmanFordGraphVertex nodePath) {
    state.setNodePath(nodePath);
  }

  @Override
  public void edit(String newValue) {
    if (this.isEditable()){
      if (newValue.contains("∞") || newValue.contains("inf")){
        state.setDistance(Double.POSITIVE_INFINITY);
        return;
      }
      Pattern p = Pattern.compile("-?\\d+");
      Matcher m = p.matcher(newValue);

      if (m.find()){
        double d = Double.parseDouble(m.group());
        state.setDistance(d);
      }
    }
  }
}
