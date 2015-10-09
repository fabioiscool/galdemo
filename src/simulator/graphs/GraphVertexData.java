package simulator.graphs;

/**
 * Trida objektu pro zakladni data vrcholu.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class GraphVertexData implements GraphVertexBasicData {
  protected String name;
  protected Boolean selected;
  
  public GraphVertexData(){
    this("", false);
  }
  
  public GraphVertexData(String name, Boolean selected) {
    this.name = name;
    this.selected = selected;        
  }
  
  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }
  
  @Override
  public Boolean getSelected(){
    return isSelected();
  }
  
  @Override
  public Boolean isSelected() {
    return selected;
  }

  @Override
  public void setSelected(Boolean selected) {
    this.selected = selected;
  }

  @Override
  public GraphVertexBasicData getBasicVertexData() {
    return new GraphVertexData(name, selected);
  }

  @Override
  public String toString() {
    return "GraphVertexData{" + "name=" + name + ", selected=" + selected + '}';
  }

}
