package simulator.graphs;

/**
 * Rozhrani pro stylesheet v grafu pro algoritmy.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public interface AlgorithmGraphStylesheet {
  public static final String VERTEX_STYLE = "VERTEX_STYLE";
  public static final String EDGE_STYLE = "EDGE_STYLE";
  public static final String HIGHLIGHT_EDGE_STYLE = "HIGHLIGHT_EDGE_STYLE";
  public static final String PATH_EDGE_STYLE = "PATH_EDGE_STYLE";
  public static final String INIT_VERTEX_STYLE = "INIT_VERTEX_STYLE";
  public static final String FIRST_PASS_VERTEX_STYLE = "FIRST_PASS_VERTEX_STYLE";
  public static final String DONE_VERTEX_STYLE = "DONE_VERTEX_STYLE";
  public static final String SELECTED_VERTEX_STYLE = "SELECTED_VERTEX_STYLE";
  public static final String ACTIVE_VERTEX_STYLE = "ACTIVE_VERTEX_STYLE";
  
  public static final String ROOT_VERTEX_STYLE = "ROOT_VERTEX_STYLE";
  public static final String COMPONENT_VERTEX_STYLE = "COMPONENT_VERTEX_STYLE";
  public static final String SELECTED_EDGE_STYLE = "SELECTED_EDGE_STYLE";
  public static final String BREAK_CON_EDGE_STYLE = "BREAK_CON_EDGE_STYLE";
  public static final String MINI_VERTEX_STYLE = "MINI_VERTEX_STYLE";
  
  public String getVertexStyle();
  public String getInitVertexStyle();
  public String getMiniVertexStyle();
  public String getActiveVertexStyle();
  public String getFirstPassVertexStyle();
  public String getDoneVertexStyle();
  public String getRootVertexStyle();
  public String getComponentVertexStyle();
  public String getEdgeStyle();
  public String getHighlighEdgeStyle();
  public String getPathEdgeStyle();
  public String getBreakConEdgeStyle();        
  public String getSelectedStyle(boolean edge);
  public String getSelectedVertexStyle();
  public String getSelectedEdgeStyle();
  public boolean isDirectedEgeStyle();
  public BasicStylesheet getThisStylesheet();
  public void setDirected(boolean directed);
}
