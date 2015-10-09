package simulator.graphs;

/**
 * Rozhrani pro objekt, ktery se vlozi do hrany i grafu.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public interface GraphCell extends NodeData, GraphVertexBasicData, GraphEdgeBasicData{
  public String getLabel();
  public String getToolTips();
  public void clearState();
  public boolean isEditable();
  public void setEditable(boolean editable);
  public void edit(String newValue);
}
