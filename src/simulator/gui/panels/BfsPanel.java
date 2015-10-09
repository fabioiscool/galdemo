package simulator.gui.panels;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import java.awt.BorderLayout;
import simulator.algorithms.AlgorithmBfs;
import simulator.controllers.AbstractController;
import simulator.controllers.BfsController;
import simulator.graphs.AlgorithmGraph;
import simulator.graphs.GraphVertexBasicData;
import simulator.graphs.bfs.BfsGraphCell;
import simulator.gui.AbstractSimulatorPanel;

/**
 * Panel pro BFS
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class BfsPanel extends AbstractAlgorithmPanel{
  
  protected AbstractSimulatorPanel simulatorPanel;
  
  public BfsPanel(AlgorithmGraph graph) {
    makeBfsGraph(graph);
    graph.refresh();
    graph.setOriented(graph.getAlgorithmStylesheet().isDirectedEgeStyle());
    this.setLayout(new BorderLayout());
    simulatorPanel = new AbstractSimulatorPanel(graph, new AlgorithmBfs());
    
    simulatorPanel.getGraphPanel().add(simulatorPanel.getGraphComponent(), BorderLayout.CENTER);
    AbstractController controller = new BfsController(simulatorPanel.getGraphComponent(), simulatorPanel.getLabel(),
                                                      simulatorPanel.getCodePanel(), simulatorPanel.getVariablesPanel());
    simulatorPanel.setController(controller);
    this.add(simulatorPanel, BorderLayout.CENTER);
  }
  
  @Override
  public void stop(){
    simulatorPanel.getVariablesPanel().dockTables();
    simulatorPanel.getController().setTimerStart(false);
  }
  
  private void makeBfsGraph(AlgorithmGraph graph){
    for (Object o : mxGraphModel.getChildren(graph.getModel(), graph.getDefaultParent())){
      if (o instanceof mxCell){
        mxCell cell = (mxCell) o;
        if (cell.isVertex()){
          if (cell.getValue() == null || cell.getValue() instanceof GraphVertexBasicData == false){
            throw new IllegalArgumentException();
          }
          if ((cell.getValue() instanceof BfsGraphCell) == false){
            GraphVertexBasicData data = (GraphVertexBasicData) cell.getValue();
            cell.setValue(new BfsGraphCell(data.getName(), data.isSelected()));
          }
          cell.setStyle(graph.getAlgorithmStylesheet().getVertexStyle());
        } else {
          cell.setValue(null);
          cell.setStyle(graph.getAlgorithmStylesheet().getEdgeStyle());
        }
      }
    }
  }
  
}
