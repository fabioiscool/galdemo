package simulator.graphs.dfs;

import com.mxgraph.util.mxConstants;
import java.util.HashMap;
import java.util.Map;
import simulator.graphs.BasicStylesheet;

/**
 * Styly pro DFS
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class DfsStylesheet extends BasicStylesheet {
  
  private DfsStylesheet() {}
  
  public static BasicStylesheet getStylesheet() {
    BasicStylesheet newSheet = BasicStylesheet.getClearsheet();
    Map<String, Object> styleVertex = new HashMap<>();
    styleVertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
    styleVertex.put(mxConstants.STYLE_PERIMETER, mxConstants.PERIMETER_ELLIPSE);
    styleVertex.put(mxConstants.STYLE_FONTSIZE, 16);
    styleVertex.put(mxConstants.STYLE_FONTCOLOR, "black");
    styleVertex.put(mxConstants.STYLE_STROKECOLOR, "black");
    styleVertex.put(mxConstants.STYLE_STROKEWIDTH, 4);
    styleVertex.put(mxConstants.STYLE_FILLCOLOR, "557290");
    
    newSheet.putCellStyle(VERTEX_STYLE, styleVertex);
    newSheet.setDefaultVertexStyle(styleVertex);
    
    Map<String, Object> styleSelectedVertex = new HashMap<>();
    styleSelectedVertex.putAll(styleVertex);
    styleSelectedVertex.put(mxConstants.STYLE_FILLCOLOR, "#FFFF33");
    styleSelectedVertex.put(mxConstants.STYLE_STROKECOLOR, "FF003F");
    newSheet.putCellStyle(SELECTED_VERTEX_STYLE, styleSelectedVertex);
    
    Map<String, Object> styleInitVertex = new HashMap<>();
    styleInitVertex.putAll(styleVertex);
    styleInitVertex.put(mxConstants.STYLE_FILLCOLOR, "white");
    newSheet.putCellStyle(INIT_VERTEX_STYLE, styleInitVertex);
    
    Map<String, Object> styleFirstPassVertex = new HashMap<>();
    styleFirstPassVertex.putAll(styleVertex);
    styleFirstPassVertex.put(mxConstants.STYLE_FILLCOLOR, "A9A9A9");
    newSheet.putCellStyle(FIRST_PASS_VERTEX_STYLE, styleFirstPassVertex);
    
    Map<String, Object> styleDoneVertex = new HashMap<>();
    styleDoneVertex.putAll(styleVertex);
    styleDoneVertex.put(mxConstants.STYLE_FILLCOLOR, "#FE6F5E");
    newSheet.putCellStyle(DONE_VERTEX_STYLE, styleDoneVertex);
    
    Map<String, Object> styleEdge = new HashMap<>();
    styleEdge.put(mxConstants.STYLE_STROKECOLOR, "blue");
    //styleEdge.put(mxConstants.STYLE_ENDARROW, "none");
    //styleEdge.put(mxConstants.STYLE_STARTARROW, "none");
    //styleEdge.put(mxConstants.STYLE_LABEL_POSITION, "top");
    styleEdge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
    styleEdge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
    styleEdge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_TOP);
    styleEdge.put(mxConstants.STYLE_FONTSIZE, 20);
    newSheet.setDefaultEdgeStyle(styleEdge);
    
    styleEdge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_LEFT);
    styleEdge.put(mxConstants.STYLE_FONTSIZE, 16);
    styleEdge.put(mxConstants.STYLE_FONTCOLOR, "black");
    styleEdge.put(mxConstants.STYLE_EDGE, mxConstants.ENTITY_SEGMENT);
    newSheet.putCellStyle(EDGE_STYLE, styleEdge);
    
    Map<String, Object> styleHighlightEdge = new HashMap<>();
    styleHighlightEdge.putAll(styleEdge);
    styleHighlightEdge.put(mxConstants.STYLE_STROKECOLOR, "red");
    styleHighlightEdge.put(mxConstants.STYLE_STROKEWIDTH, "4");
    newSheet.putCellStyle(HIGHLIGHT_EDGE_STYLE, styleHighlightEdge);
    
    Map<String, Object> pathHighlightEdge = new HashMap<>();
    pathHighlightEdge.putAll(styleEdge);
    pathHighlightEdge.put(mxConstants.STYLE_STROKECOLOR, "E2062C");
    pathHighlightEdge.put(mxConstants.STYLE_STROKEWIDTH, "2");
    newSheet.putCellStyle(PATH_EDGE_STYLE, pathHighlightEdge);
    return newSheet;
  }
  
}
