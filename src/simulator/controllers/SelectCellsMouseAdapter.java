package simulator.controllers;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Deque;
import java.util.LinkedList;
import simulator.graphs.AlgorithmGraph;

/**
 * Adapter pro mys na vybirani uzlu i hran uzivatelem
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class SelectCellsMouseAdapter extends SimulatorMouseAdapter {
  public static final String PROPERTY_SELECTED_NODES = "selectedNodes";

  protected int maxSelected;
  protected mxGraphComponent graphComponent;
  protected AlgorithmGraph graph;
  protected Deque<mxCell> selectedCells;
  
  protected boolean vertices;
  protected boolean edges;
  
  public SelectCellsMouseAdapter(mxGraphComponent graphComponent, 
                                 AlgorithmGraph graph) {
    this(graphComponent, graph, true, false);
  }
  
  public SelectCellsMouseAdapter(int maxSelected, mxGraphComponent graphComponent, 
                                 AlgorithmGraph graph) {
    this(maxSelected, graphComponent, graph, true, false);
  }
  
  public SelectCellsMouseAdapter(mxGraphComponent graphComponent, 
                                 AlgorithmGraph graph, boolean vertices, boolean edges) {
    this(graph.getChildCells(graph.getDefaultParent(), true, false).length, graphComponent, graph, vertices, edges);
  }
  
  public SelectCellsMouseAdapter(int maxSelected, mxGraphComponent graphComponent, 
                                 AlgorithmGraph graph, boolean vertices, boolean edges) {
    this.vertices = vertices;
    this.edges = edges;
    this.maxSelected = maxSelected;
    this.graphComponent = graphComponent;
    this.graph = graph;
    selectedCells = new LinkedList<>();
  }

  public mxCell getLastSelected() {
    return selectedCells.peekLast();
  }

  /**
   * Get the value of selectedNodes
   *
   * @return the value of selectedNodes
   */
  public Deque<mxCell> getSelectedNodes() {
    return selectedCells;
  }
  
  private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  /**
   * Add PropertyChangeListener.
   *
   * @param listener
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  /**
   * Remove PropertyChangeListener.
   *
   * @param listener
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }
  
  @Override
  public void reset(){
    super.reset();
    selectedCells.clear();
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (activated) {
      mxCell cell = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
      if (cell != null && ((cell.isVertex() && vertices) || (cell.isEdge() && edges))){
        if (selectedCells.contains(cell)){
          cell.setStyle(oldStyles.get(cell));
          oldStyles.remove(cell);
          selectedCells.remove(cell);
          propertyChangeSupport.firePropertyChange(PROPERTY_SELECTED_NODES, null, selectedCells);
        } else {
          if (selectedCells.size() == maxSelected){
            mxCell last = selectedCells.removeFirst();
            last.setStyle(oldStyles.get(last));
            oldStyles.remove(last);
          }
          selectedCells.add(cell);
          oldStyles.put(cell, cell.getStyle());
          if (!(vertices && edges)){ 
            cell.setStyle(graph.getAlgorithmStylesheet().getSelectedStyle(edges));
          }
          propertyChangeSupport.firePropertyChange(PROPERTY_SELECTED_NODES, null, selectedCells);
        }
        graphComponent.getGraph().getSelectionModel().clear();
        graphComponent.refresh();
      }
    }
  }
}