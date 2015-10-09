package simulator.gui.panels;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.handler.mxRubberband;
import java.awt.BorderLayout;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JSplitPane;
import simulator.algorithms.AlgorithmTopologicalSort;
import simulator.controllers.AbstractController;
import simulator.controllers.TopologicalSortController;
import simulator.graphs.AlgorithmGraph;
import simulator.graphs.AlgorithmGraphComponent;
import simulator.graphs.GraphEdgeBasicData;
import simulator.graphs.GraphVertexBasicData;
import simulator.graphs.dfs.DfsGraphEdge;
import simulator.graphs.dfs.DfsGraphVertex;
import simulator.graphs.toposort.TopologicalSortStylesheet;
import simulator.gui.AbstractSimulatorPanel;

/**
 * Panel pro algoritmus topologickeho usporadani
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class TopologicalSortPanel extends AbstractAlgorithmPanel {
  protected AbstractSimulatorPanel simulatorPanel;
  
  public TopologicalSortPanel(AlgorithmGraph graph) {
    makeTopologicalSortGraph(graph);
    graph.setOriented(true);
    if (graph.getAlgorithmStylesheet().isDirectedEgeStyle() == false){
      graph.getAlgorithmStylesheet().setDirected(true);
    }
    graph.refresh();
    this.setLayout(new BorderLayout());
    simulatorPanel = new AbstractSimulatorPanel(graph, new AlgorithmTopologicalSort());

    final AlgorithmGraphComponent graphComponentList = AbstractSimulatorPanel.getmxGraphComponent(
                                                  new AlgorithmGraph(TopologicalSortStylesheet.getStylesheet()));
    mxRubberband rubberBand2 = new mxRubberband(graphComponentList);
    graphComponentList.addMouseWheelListener(new MouseWheelListener() {
      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isControlDown()) {
          if (e.getWheelRotation() < 0) {
            graphComponentList.zoomIn();
          }
          else {
            graphComponentList.zoomOut();
          }
        }
      }
    });
    JSplitPane pane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, simulatorPanel.getGraphComponent(), graphComponentList);
    pane2.setContinuousLayout(true);
    pane2.setDividerSize(6);
    pane2.setDividerLocation(500);
    pane2.setResizeWeight(1.0);
    simulatorPanel.getGraphPanel().add(pane2, BorderLayout.CENTER);
    AbstractController controller = new TopologicalSortController(simulatorPanel.getGraphComponent(), simulatorPanel.getLabel(),
                                                  simulatorPanel.getCodePanel(), simulatorPanel.getVariablesPanel(), graphComponentList);
    simulatorPanel.setController(controller);
    
    this.add(simulatorPanel, BorderLayout.CENTER);
  }
  
  @Override
  public void stop(){
    simulatorPanel.getVariablesPanel().dockTables();
    simulatorPanel.getController().setTimerStart(false);
  }
  
  private void makeTopologicalSortGraph(AlgorithmGraph graph){
    for (Object o : mxGraphModel.getChildren(graph.getModel(), graph.getDefaultParent())){
      if (o instanceof mxCell){
        mxCell cell = (mxCell) o;
        if (cell.isVertex()){
          if (cell.getValue() == null || cell.getValue() instanceof GraphVertexBasicData == false){
            throw new IllegalArgumentException();
          }
          if ((cell.getValue() instanceof DfsGraphVertex) == false){
            GraphVertexBasicData data = (GraphVertexBasicData) cell.getValue();
            cell.setValue(new DfsGraphVertex(data.getName(), data.isSelected()));
          }
          cell.setStyle(graph.getAlgorithmStylesheet().getVertexStyle());
        } else {
          if (cell.getValue() != null && cell.getValue() instanceof GraphEdgeBasicData == false){
            throw new IllegalArgumentException();
          }
          cell.setValue(new DfsGraphEdge());
          cell.setStyle(graph.getAlgorithmStylesheet().getEdgeStyle());
        }
      }
    }
  }
}
