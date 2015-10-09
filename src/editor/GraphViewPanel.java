package editor;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import simulator.graphs.BasicStylesheet;
import simulator.graphs.GraphEdgeData;
import simulator.graphs.GraphVertexData;

/**
 * Panel pro komponentu editoru grafu
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class GraphViewPanel extends JPanel{
  protected mxGraphComponent graphComponent;
  protected double vertexWidth = 80.0;
  protected double vertexHeight = 80.0;
  protected EditorGraph graph;
  
  private int last = 0;
  private char c = 'A';
  
  public GraphViewPanel() { 
    this.setLayout(new GridLayout(1,0));   
    graph = new EditorGraph(BasicStylesheet.getStyleDefaultsheet()){
      
      private void duplicityNameCheckError(){
        JOptionPane.showMessageDialog(graphComponent,
                                        mxResources.get("badVertexNameError"),
                                        mxResources.get("error"),
                                        JOptionPane.ERROR_MESSAGE);
      }
      
      private boolean duplicityNameCheck(Object cell, Object value){
        if (containCellName((String) value, (mxCell) cell) == false){
          return true;
        }
        return false;
      }
      
      public void cellLabelChangedImp(Object cell, Object value, boolean autoSize) {
        super.cellLabelChanged(cell, value, autoSize); 
        this.refresh();
      } 
      
      @Override
      public void cellLabelChanged(Object cell, Object value, boolean autoSize) {
        if (vertexLabel && ((mxCell) cell).isVertex() && ((String)value).length() == 0){
          JOptionPane.showMessageDialog(graphComponent,
            mxResources.get("NewNodeNameError"),
            mxResources.get("error"),
            JOptionPane.ERROR_MESSAGE);
          return;
        }
        if (duplicityNameCheck(cell, value)){
          cellLabelChangedImp(cell, value, autoSize);
        } else {
          duplicityNameCheckError();
        }
      } 
      
      @Override
      public Object createVertex(Object parent, String id, Object value, double x, double y, double width, double height, String style, boolean relative) {
        if (value == null){
          GraphVertexData data = new GraphVertexData();
          Object newCell = super.createVertex(parent, id, data, x, y, width, height, BasicStylesheet.VERTEX_STYLE, relative);
          setNewName(newCell);
          return newCell;
        }
        if (duplicityNameCheck(null, value)){
          GraphVertexData data = new GraphVertexData();
          data.setName((String) value);
          return super.createVertex(parent, id, data, x, y, width, height, BasicStylesheet.VERTEX_STYLE, relative);
        } else {
          duplicityNameCheckError();
        }
        return null;
      }

      @Override
      public Object createEdge(Object parent, String id, Object value, Object source, Object target, String style) {
        return super.createEdge(parent, id, new GraphEdgeData(), source, target, BasicStylesheet.EDGE_STYLE);
      }
      
      private String getNextName(){
        String name;
        do{ 
          name = Character.toString(c);
          if (last != 0){
            name = name + last;
          }
          if (c == 'Z'){
            c = 'A';
            last++;
          } else {
            c++;
          }
        } while(duplicityNameCheck(null, name) == false);
        return name;
      }
      
      private void setNewName(Object o){
        GraphVertexData data = ((GraphVertexData)((mxCell) o).getValue());
        data.setSelected(false);
        data.setName(getNextName());
      }

      @Override
      public Object[] moveCells(Object[] cells, double dx, double dy, boolean clone, Object target, Point location) {
        Object[] newCells = super.moveCells(cells, dx, dy, clone, target, location);
        if (clone){
          for (Object o : newCells){
            if (((mxCell) o).isVertex()){
              ((mxCell) o).setStyle(BasicStylesheet.VERTEX_STYLE);
              setNewName(o);
            }
          }
          this.refresh();
        }
        return newCells;
      }
  
    };
    
    graphComponent = new mxGraphComponent(graph);
    graphComponent.setToolTips(true);
    graphComponent.setEnterStopsCellEditing(true);
    graphComponent.addMouseWheelListener(new MouseWheelListener() {
      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
        zoomByMouseWheel(e, graphComponent);
      }
    });

    graphComponent.getGraphControl().addMouseListener(new MouseAdapter(){
      @Override
      public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger() == false && e.isAltDown()){
          addNewNodeByMouse(e, graphComponent);
        }
      }
    });
    mxRubberband rubberBand = new mxRubberband(graphComponent);
    //graphComponent.getViewport().setOpaque(true);
    //graphComponent.setPageVisible(true);
    graphComponent.getViewport().setBackground(Color.WHITE);
    graphComponent.setBackground(Color.WHITE);
    this.add(graphComponent);
  }

  public mxGraphComponent getGraphComponent() {
    return graphComponent;
  }
  
  public EditorGraph getGraph() {
    return graph;
  }
  
  public void clearGraph() {
    //mxGraph graph = graphComponent.getGraph();
    //graph.getModel().beginUpdate();
    graph.removeCells(graph.getChildCells(graph.getDefaultParent()), true);
    //graph.getModel().endUpdate();
    resetNewNodesNames();
  }
  
  public void resetNewNodesNames(){
    last = 0;
    c = 'A';
  }
  
  public void setGraph(mxGraph newGraph) {
    if (newGraph == null){
      clearGraph();
      return;
    }
    mxGraph oldGraph = graphComponent.getGraph();
    oldGraph.getModel().beginUpdate();
    oldGraph.setStylesheet(newGraph.getStylesheet());
    oldGraph.removeCells(oldGraph.getChildCells(oldGraph.getDefaultParent()), true);
    oldGraph.addCells(newGraph.cloneCells(newGraph.getChildCells(newGraph.getDefaultParent())));
    for (Object o : oldGraph.getChildVertices(oldGraph.getDefaultParent())){
      mxCell cell = (mxCell) o;
      GraphVertexData data = (GraphVertexData) cell.getValue();
      if (data.isSelected()){
        oldGraph.getModel().setStyle(cell,  BasicStylesheet.SELECTED_VERTEX_STYLE);
      }
    }
    oldGraph.getModel().endUpdate();
    resetNewNodesNames();
  }
  
  public mxGraphOutline getGraphOutline() {
    final mxGraphOutline graphOutline = new mxGraphOutline(graphComponent);
    graphOutline.setAntiAlias(true);
    graphOutline.setDrawLabels(true);
    graphOutline.addMouseWheelListener(new MouseWheelListener() {
      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
        zoomByMouseWheel(e, graphComponent);
      }
    });
    return graphOutline;
  }
  
  protected void zoomByMouseWheel(MouseWheelEvent e, mxGraphComponent gc){
    if (e.isControlDown()) {
      if (e.getWheelRotation() < 0) {
        gc.zoomIn();
      }
      else {
        gc.zoomOut();
      }
    }
  }
  
  protected void addNewNodeByMouse(MouseEvent e, mxGraphComponent gc){
    double scale = gc.getGraph().getView().getScale();
    double x = e.getX()/scale;
    double y = e.getY()/scale;
    if (x - vertexWidth/2 < 0){
      x = 0;
    } else {
      x = x - vertexWidth/2;
    }
    if (y - vertexHeight/2 < 0){
      y = 0;
    } else {
      y = y - vertexHeight/2;
    }
    String s = null;
    /*s = (String)JOptionPane.showInputDialog(graphComponent,
                                            null,
                                            mxResources.get("getNodeNameTitle"),
                                            JOptionPane.PLAIN_MESSAGE,
                                            null,
                                            null,
                                            null);
    if (s == null){
      return;
    }
    if (s.length() == 0){
      JOptionPane.showMessageDialog(graphComponent,
        mxResources.get("NewNodeNameError"),
        mxResources.get("error"),
        JOptionPane.ERROR_MESSAGE);
      return;
    }*/
    gc.getGraph().getModel().beginUpdate();
    try {
      gc.getGraph().insertVertex(gc.getGraph().getDefaultParent(), null, s, x, y, vertexWidth, vertexHeight);
    }
    finally{
      gc.getGraph().getModel().endUpdate();
    }
  }
  
  public void doParallelEdgeLayout() {
    new mxParallelEdgeLayout(graphComponent.getGraph()).execute(graphComponent.getGraph().getDefaultParent());
    graphComponent.getGraph().refresh();
  }

  public boolean isVertexLabel() {
    return graph.isVertexLabel();
  }

  public void setVertexLabel(boolean vertexLabel) {
    if (vertexLabel != graph.isVertexLabel()){
      graph.setVertexLabel(vertexLabel);
      graphComponent.getGraph().refresh();
    }
  }

  public boolean isEdgeLabel() {
    return graph.isEdgeLabel();
  }

  public void setEdgeLabel(boolean edgeLabel) {
    if (edgeLabel != graph.isEdgeLabel()){
      graph.setEdgeLabel(edgeLabel);
      graphComponent.getGraph().refresh();
    }
  }
  
}
