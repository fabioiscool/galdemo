package simulator.gui.panels;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.handler.mxRubberband;
import java.awt.BorderLayout;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import simulator.algorithms.AlgorithmScc;
import simulator.controllers.AbstractController;
import simulator.controllers.SccController;
import simulator.graphs.AlgorithmGraph;
import simulator.graphs.AlgorithmGraphComponent;
import simulator.graphs.GraphEdgeBasicData;
import simulator.graphs.GraphVertexBasicData;
import simulator.graphs.dfs.DfsGraphEdge;
import simulator.graphs.dfs.DfsGraphVertex;
import simulator.graphs.scc.SccStyleSheet;
import simulator.gui.AbstractSimulatorPanel;

/**
 * Panel pro SCC
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class SccPanel extends AbstractAlgorithmPanel {
  
  protected AbstractSimulatorPanel simulatorPanel;
  
  public SccPanel(AlgorithmGraph graph) {
    makeSccGraph(graph);
    graph.setOriented(true);
    if (graph.getAlgorithmStylesheet().isDirectedEgeStyle() == false){
      graph.getAlgorithmStylesheet().setDirected(true);
    }
    graph.refresh();
    this.setLayout(new BorderLayout());
    simulatorPanel = new AbstractSimulatorPanel(graph, new AlgorithmScc());

    final AlgorithmGraphComponent transposeGraph = AbstractSimulatorPanel.getmxGraphComponent(
                                              new AlgorithmGraph(SccStyleSheet.getStylesheet(), true));
    mxRubberband rubberBand2 = new mxRubberband(transposeGraph);
    transposeGraph.addMouseWheelListener(new MouseWheelListener() {
      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isControlDown()) {
          if (e.getWheelRotation() < 0) {
            transposeGraph.zoomIn();
          }
          else {
            transposeGraph.zoomOut();
          }
        }
      }
    });
    final AlgorithmGraphComponent graphOfComponents = AbstractSimulatorPanel.getmxGraphComponent(
                                                new AlgorithmGraph(SccStyleSheet.getStylesheet(), true));
    graphOfComponents.addMouseWheelListener(new MouseWheelListener() {
      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isControlDown()) {
          if (e.getWheelRotation() < 0) {
            graphOfComponents.zoomIn();
          }
          else {
            graphOfComponents.zoomOut();
          }
        }
      }
    });
    mxRubberband rubberBand3 = new mxRubberband(graphOfComponents);
    JTabbedPane tabPane = new JTabbedPane();
    tabPane.add("Graf Transponovaný", transposeGraph);
    tabPane.add("Graf Komponent", graphOfComponents);
    JSplitPane pane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, simulatorPanel.getGraphComponent(), tabPane);
    pane2.setContinuousLayout(true);
    pane2.setDividerSize(6);
    pane2.setDividerLocation(350);
    pane2.setResizeWeight(1.0);
    simulatorPanel.getGraphPanel().add(pane2, BorderLayout.CENTER);
    AbstractController controller = new SccController(simulatorPanel.getGraphComponent(), simulatorPanel.getLabel(),
                                                      simulatorPanel.getCodePanel(), simulatorPanel.getVariablesPanel(), 
                                                      transposeGraph, graphOfComponents);
    simulatorPanel.setController(controller);
    this.add(simulatorPanel, BorderLayout.CENTER);
  }
  
  @Override
  public void stop(){
    simulatorPanel.getVariablesPanel().dockTables();
    simulatorPanel.getController().setTimerStart(false);
  }
  
  private void makeSccGraph(AlgorithmGraph graph){
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
