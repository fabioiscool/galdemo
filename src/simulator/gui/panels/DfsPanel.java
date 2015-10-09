package simulator.gui.panels;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import java.awt.BorderLayout;
import simulator.algorithms.AlgorithmDfs;
import simulator.controllers.AbstractController;
import simulator.controllers.DfsController;
import simulator.graphs.AlgorithmGraph;
import simulator.graphs.GraphEdgeBasicData;
import simulator.graphs.GraphVertexBasicData;
import simulator.graphs.dfs.DfsGraphEdge;
import simulator.graphs.dfs.DfsGraphVertex;
import simulator.gui.AbstractSimulatorPanel;

/**
 * Panel pro DFS
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class DfsPanel extends AbstractAlgorithmPanel {
  
  protected AbstractSimulatorPanel simulatorPanel;
  
  public DfsPanel(AlgorithmGraph graph) {    
    makeDfsGraph(graph);
    graph.refresh();
    graph.setOriented(graph.getAlgorithmStylesheet().isDirectedEgeStyle());
    this.setLayout(new BorderLayout());
    simulatorPanel = new AbstractSimulatorPanel(graph, new AlgorithmDfs());
    
    simulatorPanel.getGraphPanel().add(simulatorPanel.getGraphComponent(), BorderLayout.CENTER);
    AbstractController controller = new DfsController(simulatorPanel.getGraphComponent(), simulatorPanel.getLabel(),
                                                      simulatorPanel.getCodePanel(), simulatorPanel.getVariablesPanel());
    simulatorPanel.setController(controller);
    this.add(simulatorPanel, BorderLayout.CENTER);
  }
  
  @Override
  public void stop(){
    simulatorPanel.getVariablesPanel().dockTables();
    simulatorPanel.getController().setTimerStart(false);
  }
  
  private void makeDfsGraph(AlgorithmGraph graph){
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
