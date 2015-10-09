package simulator.graphs;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Graf specialne urceny pro simulatory algoritmu.
 * Ma vypnute moznosti, ktere jsou v simulaci nezadouci.
 * Pretizene jsou metody specialne pro objekty typu GraphCell
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public final class AlgorithmGraph extends mxGraph{

  protected Map<Object,String> initialStyles = new HashMap<>();
  protected boolean verticesEditable = false;
  protected boolean edgesEditable = false;
  protected boolean directed = false;
  
  public interface AlgorithmGraphCellVisitor{

		public boolean visit(mxCell cell, GraphCell value);
    public boolean allowEdge();
	}
  
  public AlgorithmGraph() {
    this.setCellsEditable(false);
  }
  
  public AlgorithmGraph(BasicStylesheet stylesheet) {
    super(stylesheet);
    this.setCellsEditable(false);
  }
  
  public AlgorithmGraph(BasicStylesheet stylesheet, boolean oriented) {
    super(stylesheet);
    this.directed = oriented;
    this.setCellsEditable(false);
  }
  
  @Override
  public boolean isVertexLabelsMovable() {
    return false;
  }

  @Override
  public boolean isEdgeLabelsMovable() {
    return false;
  }

  @Override
  public boolean isAllowNegativeCoordinates() {
    return false;
  }
  
  @Override
  public boolean isCellEditable(Object cell) {
    mxCell node = (mxCell) cell;
    if ((verticesEditable && node.isVertex()) || (edgesEditable && node.isEdge())){
      if (node.getValue() != null && node.getValue() instanceof GraphCell){
        return ((GraphCell) node.getValue()).isEditable() && super.isCellEditable(cell);
      } else {
        return super.isCellEditable(cell);
      }
    }
    return false;
  }

  @Override
  public void setCellsEditable(boolean value) {
    this.setCellsEditable(value, true, true);
  }
  
  public void setCellsEditable(boolean value, boolean vertices, boolean edges) {
    verticesEditable = vertices;
    edgesEditable = edges;     
    super.setCellsEditable(value);
  }
  
  @Override
  public Object insertVertex(Object parent, String id, Object value,
			double x, double y, double width, double height, String style,
			boolean relative){
    Object o = super.insertVertex(parent, id, value, x, y, width, height, style, relative);
    initialStyles.put(o, style);
    return o;
  }

  @Override
  public Object insertEdge(Object parent, String id, Object value, Object source, Object target, String style) {
    Object o = super.insertEdge(parent, id, value, source, target, style);
    initialStyles.put(o, style);
    return o;
  }

  @Override
  public void cellLabelChanged(Object cell, Object value, boolean autoSize) {
    GraphCell graphCell = (GraphCell) ((mxCell) cell).getValue();
    if (graphCell.isEditable()){
      if (autoSize) {
        cellSizeUpdated(cell, false);
      }
      graphCell.edit((String) value);
      this.getSelectionModel().clear();
      this.refresh();
    }
  }
  
  public void resetCells(){
    
    for (Object vertex : mxGraphModel.getChildCells(this.getModel(), this.getDefaultParent(), true, true)){ 
      if (vertex instanceof mxCell){
        mxCell cell = (mxCell) vertex;
        cell.setStyle(initialStyles.get(vertex));
        if (cell.isVertex() || cell.isEdge()){  
          Object value = ((mxCell) vertex).getValue();
          if (value instanceof GraphCell){
            ((GraphCell) value).resetState();
          }
        }
      }
    }
  }

  public boolean isOriented() {
    return directed;
  }

  public void setOriented(boolean oriented) {
    this.directed = oriented;
  }

  @Override
  public boolean isKeepEdgesInBackground() {
    return true;
  }
  
  @Override
  public boolean isCellsBendable() {
    return false;
  }

  @Override
  public boolean isCellsDeletable() {
    return false;
  }

  @Override
  public boolean isCellsCloneable() {
    return false;
  }

  @Override
  public boolean isCellsDisconnectable() {
    return false;
  }

  /**
   * Drop into another cell
   * @return 
   */
  @Override
  public boolean isDropEnabled() {
    return super.isDropEnabled();
  }

  /**
   * Zakazat volne hrany
   * @return 
   */
  @Override
  public boolean isAllowDanglingEdges() {
    return false;
  }
/*
  @Override
  public boolean isCellsEditable() {
    return false;
  }*/

  @Override
  public boolean isCellConnectable(Object cell) {
    return false;
  }
  
  @Override
  public boolean isCellDisconnectable(Object cell, Object terminal, boolean source) {
    return false;
  }

  @Override
  public String getToolTipForCell(Object cell) {
    if (cell instanceof mxCell){
      Object value = ((mxCell) cell).getValue();
      if (value instanceof GraphCell){
        GraphCell node = (GraphCell) value;
        return node.getToolTips();
      }
    }
    return super.getToolTipForCell(cell);
  }

  @Override
  public String convertValueToString(Object cell){
    if (cell instanceof mxCell){
      Object value = ((mxCell) cell).getValue();
      if (value instanceof GraphCell){
        GraphCell node = (GraphCell) value;
        return node.getLabel();
      }
    }
    return super.convertValueToString(cell);
  }

  @Override
  public Object[] moveCells(Object[] cells, double dx, double dy, boolean clone, Object target, Point location) {
    this.orderCells(false, cells);
    Object[] array = super.moveCells(cells, dx, dy, clone, target, location);
    new mxParallelEdgeLayout(this).execute(this.getDefaultParent());
    return array;
  }
 
  @Override
  public Object resizeCell(Object cell, mxRectangle bounds) {
    /*System.out.println("resizeCell");
    if (cell instanceof mxCell){
      mxCell mxcell = (mxCell) cell;
      //System.out.println(((Integer)((Double) (bounds.getHeight()/8)).intValue()).toString());
      mxcell.setStyle(mxcell.getStyle() + ";" + mxConstants.STYLE_FONTSIZE+"="+((Integer)((Double) (bounds.getHeight()/4)).intValue()).toString());
      this.refresh();
    }*/
    this.orderCells(false, new Object[]{cell});
    Object o = super.resizeCell(cell, bounds);
    new mxParallelEdgeLayout(this).execute(this.getDefaultParent());
    return o;
  }
  
  public void orderVertexToFront(){
    this.orderCells(false, mxGraphModel.getChildVertices(this.getModel(), this.getDefaultParent()));
  }
  
  public void traverseAllCells(AlgorithmGraphCellVisitor visitor){
    for (Object vertex : mxGraphModel.getChildCells(this.getModel(), this.getDefaultParent(), true, visitor.allowEdge())){
      if (vertex instanceof mxCell){
        Object value = ((mxCell) vertex).getValue();
          if (value instanceof GraphCell){
            if (visitor.visit((mxCell) vertex, (GraphCell) value) == false){
              return;
            }
          }
      }
    }
  }

  public AlgorithmGraphStylesheet getAlgorithmStylesheet(){
    return (AlgorithmGraphStylesheet) this.getStylesheet();
  }
  
  public Object insertVertex(Object value, double x, double y, double width, double height) {
    return this.insertVertex(getDefaultParent(), null, value, x, y, width, height, getAlgorithmStylesheet().getVertexStyle());
  }
  
  public Object insertEdge(Object value, Object source, Object target) {
    return this.insertEdge(getDefaultParent(), null, value, source, target, getAlgorithmStylesheet().getEdgeStyle());
  }
}
