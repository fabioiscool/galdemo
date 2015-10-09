package simulator.graphs;

/**
 * Trida pro objekt vlozeny do uzlu i hran.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public abstract class AbstractGraphCell implements GraphCell {
  
  /**
   * Priznak jestli je mozne data obejktu menit
   */
  protected boolean editable = false;
  
  /**
   * Data vrcholu
   */
  protected GraphVertexData vertexData = new GraphVertexData();
  
  /**
   * Data hrany 
   */
  protected GraphEdgeData edgeData = new GraphEdgeData();
  
  public AbstractGraphCell(){
    this("");
  }
  
  public AbstractGraphCell(String name){
    this(name, false);
  }

  public AbstractGraphCell(String name, Boolean selected) {
    vertexData.setName(name);
    vertexData.setSelected(selected);
  }
  
  public AbstractGraphCell(Double distance) {
    edgeData.setDistance(distance);
  }
  
  @Override
  public boolean isEditable(){
    return editable;
  }
  
  @Override
  public void setEditable(boolean editable){
    this.editable = editable;
  }

  @Override
  public String toString() {
    return "AbstractGraphCell{" + "editable=" + editable + ", vertexData=" + vertexData + ", edgeData=" + edgeData + '}';
  }

  @Override
  public String getName() {
    return vertexData.getName();
  }

  @Override
  public void setName(String name) {
    vertexData.setName(name);
  }

  @Override
  public Boolean isSelected() {
    return vertexData.isSelected();
  }

  @Override
  public Boolean getSelected() {
    return vertexData.getSelected();
  }

  @Override
  public void setSelected(Boolean selected) {
    vertexData.setSelected(selected);
  }

  @Override
  public GraphVertexBasicData getBasicVertexData() {
    return vertexData.getBasicVertexData();
  }

  @Override
  public Double getDistance() {
    return edgeData.getDistance();
  }

  @Override
  public void setDistance(Double distance) {
    edgeData.setDistance(distance);
  }

  @Override
  public GraphEdgeBasicData getBasicEdgeData() {
    return edgeData.getBasicEdgeData();
  }
  
  /**
   * Potomci musi pretizit tuto metodu, ktera je pouzita pro editaci objektu.
   * @param newValue 
   */
  @Override
  public void edit(String newValue){}
}
