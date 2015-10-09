package simulator.graphs.dfs;

/**
 * Operace u objektu ve vrcholu u DFS
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public interface DfsOperations {
  Integer getFirstTimestamp();
  void setFirstTimestamp(Integer firstTimestamp);
  Integer getSecondTimestamp();
  void setSecondTimestamp(Integer secondTimestamp);
  public DfsGraphVertex getNodePath();
  public void setNodePath(DfsGraphVertex nodePath);
}
