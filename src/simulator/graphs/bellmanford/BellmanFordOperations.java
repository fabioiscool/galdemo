package simulator.graphs.bellmanford;

/**
 * Operace objektu u BellmanFordova algoritmu
 * @author JVaradinek
 */
public interface BellmanFordOperations {
  public void setDistance(Double distance);
  public Double getDistance();
  public BellmanFordGraphVertex getNodePath();
  public void setNodePath(BellmanFordGraphVertex nodePath);
}
