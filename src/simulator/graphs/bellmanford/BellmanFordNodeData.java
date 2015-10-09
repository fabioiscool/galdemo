package simulator.graphs.bellmanford;

import simulator.graphs.BasicNodeData;

/**
 * Data pro vrchol u bellmanfordova algoritmu
 * @author JVaradinek
 */
public class BellmanFordNodeData extends BasicNodeData implements BellmanFordOperations{
  protected Double distance = null;
  protected BellmanFordGraphVertex nodePath = null;
  protected boolean infinity = false;
  
  @Override
  public void setDistance(Double distance) {
    this.distance = distance;
  }

  @Override
  public Double getDistance() {
    return distance;
  }

  @Override
  public BellmanFordGraphVertex getNodePath() {
    return nodePath;
  }

  @Override
  public void setNodePath(BellmanFordGraphVertex nodePath) {
    this.nodePath = nodePath;
  }
  
  @Override
  public void resetState() {
    super.resetState();
    distance = null;
    nodePath = null;
  }
}
