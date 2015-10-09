package simulator.graphs.dfs;

import com.mxgraph.model.mxCell;
import simulator.graphs.AbstractGraphCell;
import simulator.graphs.BasicNodeData;

/**
 * Objekt vkladanz do hranz u DFS
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class DfsGraphEdge extends AbstractGraphCell {

  public enum DfsEdgeType{
    NOTHING(""),
    BACK("B"),
    FORWARD("F"),
    CROSS("C"),
    TREE("");
    
    private String strRepresentation;
    
    DfsEdgeType(String strRepresentation){
      this.strRepresentation = strRepresentation;
    }

    public String getStrRepresentation() {
      return strRepresentation;
    }
    
    public static DfsEdgeType getType(mxCell edge, String style){
      DfsGraphVertex sValue = (DfsGraphVertex) edge.getSource().getValue();
      DfsGraphVertex tValue = (DfsGraphVertex) edge.getTarget().getValue();
      if (edge.getStyle().equals(style) == true || tValue.getFirstTimestamp() == null){
        return DfsEdgeType.NOTHING;
      } else if (tValue.getSecondTimestamp() == null){
        return DfsEdgeType.BACK;
      } else if (tValue.getSecondTimestamp() != null && sValue.getFirstTimestamp() < tValue.getFirstTimestamp()) {
        return DfsEdgeType.FORWARD;
      } 
      return DfsEdgeType.CROSS;
    }
  }
  
  protected BasicNodeData state;
  protected DfsEdgeType edgeType;
  protected DfsEdgeType iniEdgeType;
  
  public DfsGraphEdge() {
    this(DfsEdgeType.NOTHING, false);
  }
  
  public DfsGraphEdge(boolean enabled) {
    this(DfsEdgeType.NOTHING, enabled);
  }
  
  public DfsGraphEdge(DfsEdgeType edgeType, boolean enabled) {
    this.edgeType = this.iniEdgeType = edgeType;
    state = new BasicNodeData();
    state.setEnabled(enabled);
  }

  public DfsEdgeType getEdgeType() {
    return edgeType;
  }

  public void setEdgeType(DfsEdgeType edgeType) {
    this.edgeType = edgeType;
  }
 
  @Override
  public String getLabel() {
    if (state.isEnabled()){
      return edgeType.getStrRepresentation();
    }
    return "";
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
    edgeType = iniEdgeType;
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
