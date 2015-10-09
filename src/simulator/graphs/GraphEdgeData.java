package simulator.graphs;

/**
 * Objekt pro zakladni data hrany.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class GraphEdgeData implements GraphEdgeBasicData{
  protected Double distance;

  public GraphEdgeData() {
    this(0.0);
  }

  public GraphEdgeData(Double distance) {
    this.distance = distance;
  }

  @Override
  public Double getDistance() {
    return distance;
  }
  
  @Override
  public void setDistance(Double distance) {
    this.distance = distance;
  }

  @Override
  public GraphEdgeBasicData getBasicEdgeData() {
    return new GraphEdgeData(distance);
  }

  @Override
  public String toString() {
    return "GraphEdgeData{" + "distance=" + distance + '}';
  }

}
