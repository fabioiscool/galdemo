package simulator.graphs;

import com.mxgraph.swing.mxGraphComponent;

/**
 * Komponenta pro zobrazovani GraphAlgorithm
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class AlgorithmGraphComponent extends mxGraphComponent{

  public AlgorithmGraphComponent(AlgorithmGraph graph) {
    super(graph);
  }

  @Override
  public AlgorithmGraph getGraph() {
    return (AlgorithmGraph) super.getGraph();
  }
  
}
