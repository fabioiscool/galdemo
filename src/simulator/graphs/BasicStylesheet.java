package simulator.graphs;

import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxStylesheet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Zakladni styly pro grafy.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class BasicStylesheet extends mxStylesheet implements AlgorithmGraphStylesheet{
  
  protected BasicStylesheet(){
    
  }
  
  public static BasicStylesheet getClearsheet(){
    return new BasicStylesheet();
  }
  
  public static BasicStylesheet getFromMxStylesheet(mxStylesheet sheet){
    BasicStylesheet newSheet = new BasicStylesheet();
    Map<String, Map<String, Object>> newStyles = new HashMap<>();
    for (Entry<String, Map<String, Object>> e : sheet.getStyles().entrySet()){
      Map<String, Object> tmp = new HashMap<>();
      for (Entry<String, Object> e2 : e.getValue().entrySet()){
        tmp.put(e2.getKey(), e2.getValue());
      }
      newStyles.put(e.getKey(), tmp);
    }
    
    newSheet.setStyles(newStyles);
    return newSheet;
  }
  
  public static BasicStylesheet getStyleDefaultsheet(){
    BasicStylesheet newSheet = new BasicStylesheet();
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
    
    newSheet.putCellStyle(INIT_VERTEX_STYLE, styleVertex);
    newSheet.putCellStyle(FIRST_PASS_VERTEX_STYLE, styleVertex);
    newSheet.putCellStyle(DONE_VERTEX_STYLE, styleVertex);
    newSheet.putCellStyle(ROOT_VERTEX_STYLE, styleVertex);
    newSheet.putCellStyle(COMPONENT_VERTEX_STYLE, styleVertex);
    newSheet.putCellStyle(MINI_VERTEX_STYLE, styleVertex);
    newSheet.putCellStyle(ACTIVE_VERTEX_STYLE, styleVertex);
    
    Map<String, Object> styleSelectedVertex = new HashMap<>();
    styleSelectedVertex.putAll(styleVertex);
    styleSelectedVertex.put(mxConstants.STYLE_FILLCOLOR, "FF4040");
    newSheet.putCellStyle(SELECTED_VERTEX_STYLE, styleSelectedVertex);
    
    Map<String, Object> styleEdge = new HashMap<>();
    styleEdge.put(mxConstants.STYLE_STROKECOLOR, "08E8DE");
    styleEdge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
    styleEdge.put(mxConstants.STYLE_STARTARROW, "none");
    styleEdge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
    styleEdge.put(mxConstants.STYLE_FONTSIZE, 20);
    newSheet.putCellStyle(EDGE_STYLE, styleEdge);
    newSheet.setDefaultEdgeStyle(styleEdge);
    newSheet.putCellStyle(PATH_EDGE_STYLE, styleEdge);
    newSheet.putCellStyle(BREAK_CON_EDGE_STYLE, styleEdge);

    Map<String, Object> styleHighlightEdge = new HashMap<>();
    styleHighlightEdge.putAll(styleEdge);
    styleHighlightEdge.put(mxConstants.STYLE_STROKECOLOR, "red");
    styleHighlightEdge.put(mxConstants.STYLE_STROKEWIDTH, "4");
    newSheet.putCellStyle(HIGHLIGHT_EDGE_STYLE, styleHighlightEdge);
    
    Map<String, Object> selectedEdge = new HashMap<>();
    selectedEdge.putAll(styleEdge);
    selectedEdge.put(mxConstants.STYLE_STROKECOLOR, "#7FFF00");
    selectedEdge.put(mxConstants.STYLE_STROKEWIDTH, "4");
    newSheet.putCellStyle(SELECTED_EDGE_STYLE, selectedEdge);

    return newSheet;  
  }
  
  @Override
  public String getSelectedStyle(boolean edge){
    if (edge){
      return getSelectedEdgeStyle();
    } else {
      return getSelectedVertexStyle();
    }
  }
  
  @Override
  public String getSelectedVertexStyle(){
    return SELECTED_VERTEX_STYLE;
  }
  
  @Override
  public String getSelectedEdgeStyle(){
    return SELECTED_EDGE_STYLE;
  }

  @Override
  public String getVertexStyle() {
    return VERTEX_STYLE;
  }

  @Override
  public String getInitVertexStyle() {
    return INIT_VERTEX_STYLE;
  }

  @Override
  public String getFirstPassVertexStyle() {
    return FIRST_PASS_VERTEX_STYLE;
  }

  @Override
  public String getDoneVertexStyle() {
    return DONE_VERTEX_STYLE;
  }

  @Override
  public String getRootVertexStyle() {
    return ROOT_VERTEX_STYLE;
  }

  @Override
  public String getComponentVertexStyle() {
    return COMPONENT_VERTEX_STYLE;
  }

  @Override
  public String getEdgeStyle() {
    return EDGE_STYLE;
  }

  @Override
  public String getHighlighEdgeStyle() {
    return HIGHLIGHT_EDGE_STYLE;
  }

  @Override
  public String getPathEdgeStyle() {
    return PATH_EDGE_STYLE;
  }

  @Override
  public String getBreakConEdgeStyle() {
    return BREAK_CON_EDGE_STYLE;
  }

  @Override
  public String getMiniVertexStyle() {
    return MINI_VERTEX_STYLE;
  }
  
  @Override
  public String getActiveVertexStyle() {
    return ACTIVE_VERTEX_STYLE;
  }
  
  @Override
  public boolean isDirectedEgeStyle(){
    String arrow = (String) this.getStyles().get(this.getEdgeStyle()).get(mxConstants.STYLE_ENDARROW);
    return arrow.equalsIgnoreCase(mxConstants.NONE) == false;
  }
  
  public static boolean isVertexStyle(String style){
    switch (style){
      case VERTEX_STYLE:
      case INIT_VERTEX_STYLE:
      case FIRST_PASS_VERTEX_STYLE:
      case DONE_VERTEX_STYLE:
      case COMPONENT_VERTEX_STYLE:  
      case MINI_VERTEX_STYLE:
      case ACTIVE_VERTEX_STYLE: 
      case SELECTED_VERTEX_STYLE: return true;
    }
    return false;
  }
  
  public static boolean isEdgeStyle(String style){
    switch (style){
      case EDGE_STYLE:
      case HIGHLIGHT_EDGE_STYLE:
      case PATH_EDGE_STYLE:
      case BREAK_CON_EDGE_STYLE: 
      case SELECTED_EDGE_STYLE: return true;
    }
    return false;
  }
  
  public static String getDesc(String style){
    switch (style){
      case EDGE_STYLE: return "Styl pro základní hrany";
      case HIGHLIGHT_EDGE_STYLE: return "Styl pro aktuálně zvýrazněnou hranu";
      case PATH_EDGE_STYLE: return "Styl hranu, která je cestou";
      case BREAK_CON_EDGE_STYLE: return "Styl pro hranu porušující pravidlo při hledání nejkratší cesty";
      case SELECTED_EDGE_STYLE: return "Styl pro uživatelem vybranou hranu";
      case VERTEX_STYLE: return "Styl pro základní uzel";
      case INIT_VERTEX_STYLE: return "Styl pro uzel, který byl inicializován";
      case FIRST_PASS_VERTEX_STYLE: return "Styl pro uzel, který byl poprvé navštíven";
      case DONE_VERTEX_STYLE: return "Styl pro uzel, který je dokončen";
      case COMPONENT_VERTEX_STYLE: return "Styl pro uzel sloužící jako uzel komponenty v SCC";  
      case MINI_VERTEX_STYLE: return "Styl pro uzel sloužící jako uzel seznamu v Topologickém uspořádání";
      case ACTIVE_VERTEX_STYLE: return "Styl pro zpracovávaný uzel";
      case SELECTED_VERTEX_STYLE: return "Styl pro uživatelem vybraný uzel"; 
    }
    return null;
  }
  
  public static Set<String> getAllStyles(){
    Set<String> names = new TreeSet<>();
    names.add(VERTEX_STYLE);
    names.add(SELECTED_VERTEX_STYLE);
    names.add(INIT_VERTEX_STYLE);
    names.add(FIRST_PASS_VERTEX_STYLE);
    names.add(DONE_VERTEX_STYLE);
    names.add(COMPONENT_VERTEX_STYLE);
    names.add(MINI_VERTEX_STYLE);
    names.add(ACTIVE_VERTEX_STYLE);
    names.add(EDGE_STYLE);
    names.add(HIGHLIGHT_EDGE_STYLE);
    names.add(PATH_EDGE_STYLE);
    names.add(BREAK_CON_EDGE_STYLE);
    names.add(SELECTED_EDGE_STYLE);
    return names;
  }

  @Override
  public BasicStylesheet getThisStylesheet() {
    return this;
  }
  
  @Override
  public void setDirected(boolean directed){
    for (String style : getAllStyles()){
      if (isEdgeStyle(style)){
        Map<String, Object> sett = this.getStyles().get(style);
        if (sett != null){
          if (directed){
            sett.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
          } else {
            sett.put(mxConstants.STYLE_ENDARROW, mxConstants.NONE);
          }
        }
      }
    }
  }
  
  public static Map<String, Object> stringToStyle(String styleString){
    Map<String, Object> styles = new TreeMap<>();
    for (String str : styleString.split(";")){
      if (str.length() > 0){
        String[] style = str.split(":");
        if (style.length == 2){
          styles.put(style[0], style[1]);
        }
      }
    }
    return styles;
  }
  
  public static String styleToString(Map<String, Object> styles){
    if (styles == null){
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (Entry<String, Object> entry : styles.entrySet()){
      sb.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
    }
    return sb.toString(); 
  }
}
