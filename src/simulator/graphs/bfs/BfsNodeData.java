package simulator.graphs.bfs;

import simulator.graphs.BasicNodeData;

/**
 * Data objektu, kterz je ve vrocholu u BFS
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class BfsNodeData extends BasicNodeData implements BfsOperations{
  protected boolean infinity = false;
  protected int intValue = 0;
  protected BfsGraphCell nodePath = null;

  @Override
  public boolean isInfinity() {
    return infinity;
  }

  @Override
  public void setInfinity(boolean infinity) {
    this.infinity = infinity;
  }

  @Override
  public int getIntValue() {
    return intValue;
  }

  @Override
  public void setIntValue(int intValue) {
    this.intValue = intValue;
  }

  @Override
  public BfsGraphCell getNodePath() {
    return nodePath;
  }

  @Override
  public void setNodePath(BfsGraphCell nodePath) {
    this.nodePath = nodePath;
  }
}
