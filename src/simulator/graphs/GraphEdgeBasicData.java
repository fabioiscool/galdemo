package simulator.graphs;

import java.io.Serializable;

/**
 * Rozhrani pro objekty, ktere nesou zakladi data hran
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public interface GraphEdgeBasicData extends Serializable{
  public Double getDistance();
  public void setDistance(Double distance);
  public GraphEdgeBasicData getBasicEdgeData();
}
