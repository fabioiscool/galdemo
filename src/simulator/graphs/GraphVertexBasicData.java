package simulator.graphs;

import java.io.Serializable;

/**
 * Rozhrani pro objekty, ktere nesou zakladni data vrcholu.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public interface GraphVertexBasicData extends Serializable{
  public String getName();
  public void setName(String name);
  public Boolean isSelected();
  //kopie metody isSelected kvuli serializaci grafu
  public Boolean getSelected();
  public void setSelected(Boolean selected);
  public GraphVertexBasicData getBasicVertexData();
}
