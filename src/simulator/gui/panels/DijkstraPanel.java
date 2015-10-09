
package simulator.gui.panels;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import java.awt.BorderLayout;
import simulator.algorithms.AlgorithmDijkstra;
import simulator.controllers.AbstractController;
import simulator.controllers.DijkstraController;
import simulator.graphs.AlgorithmGraph;
import simulator.graphs.GraphEdgeBasicData;
import simulator.graphs.GraphVertexBasicData;
import simulator.graphs.bellmanford.BellmanFordGraphEdge;
import simulator.graphs.bellmanford.BellmanFordGraphVertex;
import simulator.gui.AbstractSimulatorPanel;

/**
 * Panel pro Dijkstruv algoritmus
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class DijkstraPanel extends AbstractAlgorithmPanel {
  
  protected AbstractSimulatorPanel simulatorPanel;
  
  public DijkstraPanel(AlgorithmGraph graph) {
    makeDijkstraGraph(graph);
    graph.setOriented(true);
    if (graph.getAlgorithmStylesheet().isDirectedEgeStyle() == false){
      graph.getAlgorithmStylesheet().setDirected(true);
    }
    graph.refresh();
    this.setLayout(new BorderLayout());
    simulatorPanel = new AbstractSimulatorPanel(graph, new AlgorithmDijkstra());
    
    simulatorPanel.getGraphPanel().add(simulatorPanel.getGraphComponent(), BorderLayout.CENTER);
    AbstractController controller = new DijkstraController(simulatorPanel.getGraphComponent(), simulatorPanel.getLabel(),
                                                      simulatorPanel.getCodePanel(), simulatorPanel.getVariablesPanel());
    simulatorPanel.setController(controller);
    this.add(simulatorPanel, BorderLayout.CENTER);
  }
  
  @Override
  public void stop(){
    simulatorPanel.getVariablesPanel().dockTables();
    simulatorPanel.getController().setTimerStart(false);
  }
  
  private void makeDijkstraGraph(AlgorithmGraph graph){
    for (Object o : mxGraphModel.getChildren(graph.getModel(), graph.getDefaultParent())){
      if (o instanceof mxCell){
        mxCell cell = (mxCell) o;
        if (cell.isVertex()){
          if (cell.getValue() == null || cell.getValue() instanceof GraphVertexBasicData == false){
            throw new IllegalArgumentException();
          }
          if ((cell.getValue() instanceof BellmanFordGraphVertex) == false){
            GraphVertexBasicData data = (GraphVertexBasicData) cell.getValue();
            cell.setValue(new BellmanFordGraphVertex(data.getName(), data.isSelected()));
          }
          cell.setStyle(graph.getAlgorithmStylesheet().getVertexStyle());
        } else {
          if (cell.getValue() == null || cell.getValue() instanceof GraphEdgeBasicData == false){
            throw new IllegalArgumentException();
          }
          if ((cell.getValue() instanceof BellmanFordGraphEdge) == false){
            GraphEdgeBasicData data = (GraphEdgeBasicData) cell.getValue();
            cell.setValue(new BellmanFordGraphEdge(data.getDistance()));
          }
          cell.setStyle(graph.getAlgorithmStylesheet().getEdgeStyle());
        }
      }
    }
  }
}
