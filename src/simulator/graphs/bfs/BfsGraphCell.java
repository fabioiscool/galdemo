package simulator.graphs.bfs;

import simulator.graphs.AbstractGraphCell;

/**
 * Objekt pro vlozeni do vrcholu u BFS
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class BfsGraphCell extends AbstractGraphCell implements BfsOperations{
  protected BfsNodeData state = null;
  
  public BfsGraphCell() {
    this(null);
  }
  
  public BfsGraphCell(String name) {
    this(name, false);
  }
  
  public BfsGraphCell(String name, Boolean selected) {
    this(name, new BfsNodeData(), selected);
  }
  
  public BfsGraphCell(String name, BfsNodeData state, Boolean selected) {
    super(name, selected);
    this.state = state;
  }

  @Override
  public String getLabel() {
    if (state.isEnabled() == false){
      return this.getName();
    }
    if (state.isInfinity()){
      return this.getName() + ",∞";
    }
    return this.getName() + "," + state.getIntValue();
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
  public boolean isInfinity() {
    return state.isInfinity();
  }

  @Override
  public void setInfinity(boolean infinity) {
    state.setInfinity(infinity);
  }

  @Override
  public int getIntValue() {
    return state.getIntValue();
  }

  @Override
  public void setIntValue(int intValue) {
    state.setIntValue(intValue);
  }

  @Override
  public BfsGraphCell getNodePath() {
    return state.getNodePath();
  }

  @Override
  public void setNodePath(BfsGraphCell nodePath) {
    state.setNodePath(nodePath);
  }

  @Override
  public void clearState() {
    state = null;
  }
  
  @Override
  public void resetState(){
    state.resetState();
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
  public void setToolTipsEnabled(boolean toolTipsEnabled) {
    state.setToolTipsEnabled(toolTipsEnabled);
  }

  @Override
  public boolean isToolTipsEnabled() {
    return state.isToolTipsEnabled();
  }
}
