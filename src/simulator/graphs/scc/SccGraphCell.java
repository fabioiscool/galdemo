package simulator.graphs.scc;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import simulator.graphs.AbstractGraphCell;

/**
 * Objekt vkladany do grafu komponent u SCC
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class SccGraphCell extends AbstractGraphCell{
  protected List<mxCell> nodes = new ArrayList<>();
  
  public SccGraphCell() {
  }
  
  @Override
  public String getLabel() {
    return this.getName();
  }

  @Override
  public String getToolTips() {
    return "";
  }

  @Override
  public void clearState() {
    nodes.clear();
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public void setEnabled(boolean enabled) {}

  @Override
  public boolean isActive() {
    return true;
  }

  @Override
  public void setActive(boolean active) {}

  @Override
  public void resetState() {
    nodes.clear();
  }

  public List<mxCell> getNodes() {
    return nodes;
  }
  
  public Set<mxCell> makeAdj(mxGraph graph, boolean reverse){
    Set<mxCell> set = new HashSet<>();
    for (mxCell cell : nodes){
      for (int i = 0; i < graph.getModel().getEdgeCount(cell); i++){
        mxCell edge = (mxCell) ((mxCell) graph.getModel().getEdgeAt(cell, i));
        mxCell source = (mxCell) edge.getSource();
        mxCell target = (mxCell) edge.getTarget();
        if (target == cell && reverse == true){
          set.add(source);
        } else if (source == cell && reverse == false){
          set.add(target);
        }
      }
    }
    return set;
  }
  
  public void addNode(mxCell node) {
    nodes.add(node);
  }

  @Override
  public void setToolTipsEnabled(boolean toolTipsEnabled) {}

  @Override
  public boolean isToolTipsEnabled() {
    return true;
  }
  
}
