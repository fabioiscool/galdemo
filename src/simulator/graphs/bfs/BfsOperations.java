package simulator.graphs.bfs;

/**
 * Operace s objektem vrcholu v BFS
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public interface BfsOperations {
  public boolean isInfinity();
  public void setInfinity(boolean infinity);
  public int getIntValue();
  public void setIntValue(int intValue);
  public BfsGraphCell getNodePath();
  public void setNodePath(BfsGraphCell nodePath);
}
