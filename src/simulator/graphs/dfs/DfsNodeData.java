package simulator.graphs.dfs;

import simulator.graphs.BasicNodeData;

/**
 * Data objektu ve vrcholu u DFS
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class DfsNodeData extends BasicNodeData implements DfsOperations {

  protected Integer firstTimestamp = null;
  protected Integer secondTimestamp = null;
  protected DfsGraphVertex nodePath = null;
  
  @Override
  public Integer getFirstTimestamp() {
    return firstTimestamp;
  }

  @Override
  public Integer getSecondTimestamp() {
    return secondTimestamp;
  }

  @Override
  public DfsGraphVertex getNodePath() {
    return nodePath;
  }

  @Override
  public void setNodePath(DfsGraphVertex nodePath) {
    this.nodePath = nodePath;
  }

  @Override
  public void setFirstTimestamp(Integer firstTimestamp) {
    this.firstTimestamp = firstTimestamp;
  }

  @Override
  public void setSecondTimestamp(Integer secondTimestamp) {
    this.secondTimestamp = secondTimestamp;
  }
  
  @Override
  public void resetState() {
    super.resetState();
    firstTimestamp = null;
    secondTimestamp = null;
    nodePath = null;
  }
}
