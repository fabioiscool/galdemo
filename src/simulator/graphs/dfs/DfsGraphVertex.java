package simulator.graphs.dfs;

import simulator.graphs.AbstractGraphCell;

/**
 * Objekt vlozeny do vrcholu u DFS
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class DfsGraphVertex extends AbstractGraphCell implements DfsOperations{
  
  protected DfsNodeData state = null;
  
  public DfsGraphVertex() {
    this(null);
  }
  
  public DfsGraphVertex(String name) {
    this(name, false);
  }
  
  public DfsGraphVertex(String name, Boolean selected) {
    this(name, new DfsNodeData(), selected);
  }
  
  public DfsGraphVertex(String name, DfsNodeData state, Boolean selected) {
    super(name, selected);
    this.state = state;
  }
  
  @Override
  public String getLabel() {
    if (state.isEnabled() == false){
      return this.getName();
    }
    StringBuilder sb = new StringBuilder(this.getName() + '\n');
    if (state.getFirstTimestamp() != null){
      sb.append(state.getFirstTimestamp());
    } 
    sb.append(" / ");
    if (state.getSecondTimestamp() != null){
      sb.append(state.getSecondTimestamp());
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
  public Integer getFirstTimestamp() {
    return state.getFirstTimestamp();
  }

  @Override
  public Integer getSecondTimestamp() {
    return state.getSecondTimestamp();
  }

  @Override
  public DfsGraphVertex getNodePath() {
    return state.getNodePath();
  }

  @Override
  public void setNodePath(DfsGraphVertex nodePath) {
    state.setNodePath(nodePath);
  }

  @Override
  public void setFirstTimestamp(Integer firstTimestamp) {
    state.setFirstTimestamp(firstTimestamp);
  }

  @Override
  public void setSecondTimestamp(Integer secondTimestamp) {
    state.setSecondTimestamp(secondTimestamp);
  }

}
