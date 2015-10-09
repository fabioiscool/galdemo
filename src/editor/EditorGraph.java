package editor;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import simulator.graphs.AlgorithmGraphStylesheet;
import simulator.graphs.GraphEdgeData;
import simulator.graphs.GraphVertexData;

/**
 * Trida grafu upravene tak, aby graf byl vhodny pro editor.
 * Jsou vylepsene nektere zakladni funkce a nastavene urcite omezeni a moznosti.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class EditorGraph extends mxGraph{
  protected boolean vertexLabel = true;
  protected boolean edgeLabel = true;
  
  public EditorGraph(mxStylesheet stylesheet) {
    super(stylesheet);
  }

  public boolean isVertexLabel() {
    return vertexLabel;
  }

  public void setVertexLabel(boolean vertexLabel) {
    this.vertexLabel = vertexLabel;
  }

  public boolean isEdgeLabel() {
    return edgeLabel;
  }

  public void setEdgeLabel(boolean edgeLabel) {
    this.edgeLabel = edgeLabel;
  }

  @Override
  public String getToolTipForCell(Object cell) {
    mxCell node = (mxCell) cell;
    if (node.isVertex()){
      GraphVertexData data = (GraphVertexData) node.getValue();
      if (data.getSelected()){
        return mxResources.get("selected");
      }
    }
    return null;
  }
  
  @Override
  public String convertValueToString(Object cell){
    if (cell instanceof mxCell){
      if ((((mxCell) cell).isVertex() && !vertexLabel) || (((mxCell) cell).isEdge() && !edgeLabel)){
        return "";
      }
      Object value = ((mxCell) cell).getValue();
      if (vertexLabel && value instanceof GraphVertexData){
        GraphVertexData node = (GraphVertexData) value;
        return node.getName();
      }
      if (edgeLabel && value instanceof GraphEdgeData){
        GraphEdgeData node = (GraphEdgeData) value;
        return new DecimalFormat("#").format(node.getDistance());
      }
    }
    return super.convertValueToString(cell);
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
  public boolean isCellEditable(Object cell) {
    mxCell node = (mxCell) cell;
    if ((vertexLabel && node.isVertex()) || (edgeLabel && node.isEdge())){
      return super.isCellEditable(cell);
    }
    return false;
  }
  
  public boolean containCellName(String name, mxCell original){
    for(Object o : this.getChildVertices(this.getDefaultParent())){
      GraphVertexData data = (GraphVertexData) ((mxCell) o).getValue();
      if (o != original && data.getName().equalsIgnoreCase(name)){
        return true;
      }
    }
    return false;
  }

  @Override
  public void cellLabelChanged(Object cell, Object value, boolean autoSize) {
    model.beginUpdate();
    if (autoSize) {
      cellSizeUpdated(cell, false);
    }
    String strValue = (String) value; 
    if (vertexLabel && ((mxCell) cell).isVertex()){
      GraphVertexData data = (GraphVertexData) ((mxCell) cell).getValue();
      getModel().setValue(cell, new GraphVertexData((String) value, data.isSelected()));
    } else if (edgeLabel){
      GraphEdgeData data = new GraphEdgeData();
      if (strValue.contains("∞") || strValue.contains("inf")){
        data.setDistance(Double.POSITIVE_INFINITY);
      } else {
        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(strValue);

        if (m.find()){
          double d = Double.parseDouble(m.group());
          data.setDistance(d);
        }
      }
      getModel().setValue(cell, data);
    }
    this.getSelectionModel().clear();
    this.fireEvent(new mxEventObject(mxEvent.LABEL_CHANGED));
    model.endUpdate();
  }

  @Override
  public boolean isAllowDanglingEdges() {
    return false;
  }

  @Override
  public boolean isAllowLoops() {
    return true;
  }

  @Override
  public boolean isAllowNegativeCoordinates() {
    return false;
  }

  public boolean isOriented() {
    String arrow = (String) this.getStylesheet().getStyles().get(AlgorithmGraphStylesheet.EDGE_STYLE).get(mxConstants.STYLE_ENDARROW);
    return arrow.equalsIgnoreCase(mxConstants.NONE) == false;
  }
  
}
