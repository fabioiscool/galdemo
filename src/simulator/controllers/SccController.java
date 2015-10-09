package simulator.controllers;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JLabel;
import simulator.graphs.AlgorithmGraph;
import simulator.graphs.AlgorithmGraph.AlgorithmGraphCellVisitor;
import simulator.graphs.AlgorithmGraphComponent;
import simulator.graphs.BasicStylesheet;
import simulator.graphs.GraphCell;
import simulator.graphs.dfs.DfsGraphVertex;
import simulator.graphs.dfs.DfsGraphEdge;
import simulator.graphs.scc.SccGraphCell;
import simulator.gui.VariablesPanel;
import simulator.gui.VisualizationPseudocodePanel;

/**
 * Ridici jednotka pro SCC
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class SccController extends AbstractDfsController {
    
  /*
   * Stav algoritmu SCC
   */
  protected enum SccControllerState{
    NORMAL_GRAPH_DFS_CALL,
    TRANSPOSE_GRAPH_DFS_CALL;
  }
  
  /**
   * Nastaveni barev pro komponenty
   */
  protected static class ExtendedColorStyle{
    protected BasicStylesheet extendedSheet;
    static private String[] names = 
    {"AERO", "AMETHYST", "APPLE", "X11GRAY", "AURELION", "BLUSH",
     "CADMIUM_ORANGE", "MAROON", "CG Blue", "DARK_PASTER_GREEN",
     "CHARCOAL", "CAFE_AU_LAIT", "HAN_PURPLE", "MODE_NEIGE", "MINT", "FERRARI_RED"
    };
    protected String fromStyle;
    
    public ExtendedColorStyle(BasicStylesheet transposeStylesheet, String fromStyle) {
      this.extendedSheet = transposeStylesheet;
      this.fromStyle = fromStyle;
      addNewColorStyle(names[0], "#7CB9E8");
      addNewColorStyle(names[1], "#9966CC");
      addNewColorStyle(names[2], "#8DB600");
      addNewColorStyle(names[3], "#BEBEBE");
      addNewColorStyle(names[4], "#FDEE00");
      addNewColorStyle(names[5], "#DE5D83");
      addNewColorStyle(names[6], "#ED872D");
      addNewColorStyle(names[7], "#800000");
      addNewColorStyle(names[8], "#007AA5");
      addNewColorStyle(names[9], "#03C03C");
      addNewColorStyle(names[10], "#36454F");
      addNewColorStyle(names[11], "#A67B5B");
      addNewColorStyle(names[12], "#5218FA");
      addNewColorStyle(names[13], "#967117");
      addNewColorStyle(names[14], "#3EB489");
      addNewColorStyle(names[15], "#FF2800");
    }
    
    private void addNewColorStyle(String name, String color){
      Map<String,Object> newStyles = new HashMap<>();
      newStyles.putAll(extendedSheet.getStyles().get(fromStyle));
      newStyles.put(mxConstants.STYLE_FILLCOLOR, color);
      extendedSheet.putCellStyle(name, newStyles);
    }
    
    private static int index = 0;
    public static String getNextStyle(){ 
      return names[index++ % names.length];
    }
    
    public static  void resetIndex(){ 
      index = 0;
    }
  }
  
  protected AlgorithmGraphComponent standardGraph;
  protected AlgorithmGraphComponent transposeGraph;
  protected AlgorithmGraphComponent componentsGraph;
  protected AlgorithmGraph mainGraph;
  
  protected Deque<mxCell> componentNodes = new LinkedList<>();
  
  protected SccControllerState state = SccControllerState.NORMAL_GRAPH_DFS_CALL;
  
  /**
   * inicializace, naplneni pasky instrukci
   * @param graphCom
   * @param graphComponentLabel
   * @param completeCodePanel
   * @param variablesPanel
   * @param transposeGraph
   * @param componentsGraph 
   */
  public SccController(final AlgorithmGraphComponent graphCom, final JLabel graphComponentLabel, 
                       final VisualizationPseudocodePanel completeCodePanel, 
                       final VariablesPanel variablesPanel, 
                       final AlgorithmGraphComponent transposeGraph, 
                       final AlgorithmGraphComponent componentsGraph){
    super(graphCom, graphComponentLabel, completeCodePanel, variablesPanel);
    mainGraph = graph;
    this.transposeGraph = transposeGraph;
    this.standardGraph = graphCom;
    new ExtendedColorStyle((BasicStylesheet) transposeGraph.getGraph().getAlgorithmStylesheet(),
                                              transposeGraph.getGraph().getAlgorithmStylesheet().getRootVertexStyle());
    
    this.componentsGraph = componentsGraph;
    new ExtendedColorStyle((BasicStylesheet) componentsGraph.getGraph().getAlgorithmStylesheet(),
                                              componentsGraph.getGraph().getAlgorithmStylesheet().getComponentVertexStyle());
    interactiveMode = false;
    resetControllerImpl();
    Instruction dfsLabel = new LabelInstruction();
    Instruction dfsReturnLabel1 = new LabelInstruction();
    Instruction dfsReturnLabel2 = new LabelInstruction();
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 1), algorithm.getRealLineNumber(0, 4), Color.GREEN));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(1, 1), algorithm.getRealLineNumber(1, 10), Color.GREEN));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(2, 1), algorithm.getRealLineNumber(2, 11), Color.GREEN));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 1)));
    tape.add(new SetLabelTextInstruction("První volání DFS"));
    tape.add(new PauseInstruction());
    tape.add(new SelectStartNodeInstruction());
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 1), algorithm.getRealLineNumber(0, 1), doneColor));
    tape.add(new LabelInstruction(dfsLabel, new Condition(){
      @Override
      public boolean isFulfil() {
        return true;
      }
    }));
    tape.add(dfsReturnLabel1);
    tape.add(new SetLabelTextInstruction("Vytvoření transponovaného grafu"));
    tape.add(new SetSccAlgStateInstruction(SccControllerState.TRANSPOSE_GRAPH_DFS_CALL));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 2)));
    tape.add(new ChangeTimeInstruction(true));
    tape.add(new PauseInstruction());
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 2), algorithm.getRealLineNumber(0, 2), doneColor));
    tape.add(new MakeTransposeGraphInstruction()); 
    tape.add(new SetNewActiveGraphInstruction(transposeGraph)); 
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 3)));
    tape.add(new SetLabelTextInstruction("Druhé volání DFS"));
    tape.add(new PauseInstruction());
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 3), algorithm.getRealLineNumber(0, 3), doneColor));
    tape.add(new LabelInstruction(dfsLabel, new Condition(){
      @Override
      public boolean isFulfil() {
        return true;
      }
    }));
    tape.add(dfsReturnLabel2);
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 1), algorithm.getRealLineNumber(0, 4), doneColor));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(1, 1), algorithm.getRealLineNumber(1, 10), doneColor));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(2, 1), algorithm.getRealLineNumber(2, 11), doneColor));
    tape.add(new EndInstruction());
    
    tape.add(dfsLabel);
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 0)));
    tape.add(new PauseInstruction()); 
    tape.add(new AddAllVertexListInstruction(vertexList));
    tape.add(new SetLabelTextInstruction("Inicializace všech uzlů"));
    Instruction forLabel = new LabelInstruction();
    tape.add(forLabel);
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 1)));
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 2)));
    tape.add(new DequeueInstruction(vertexList));
    tape.add(new ActiveToRegisterInstruction(variableU));
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 3)));
    tape.add(new SetColorInstruction(stylesheet.getInitVertexStyle()));
    tape.add(new SetVertexEnableInstruction());
    tape.add(new SetVertexToolTipsInstruction());
    tape.add(new SetPathInstruction());
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 4)));
    tape.add(new PauseInstruction());
    tape.add(new LabelInstruction(forLabel, new Condition(){
      @Override
      public boolean isFulfil() {
        return (vertexList.isEmpty() == false);
      }  
    }));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 5)));
    tape.add(new SetLabelTextInstruction("Inicializace času"));
    tape.add(new PauseInstruction());
    tape.add(new ShowOrHideVariableInstruction("time", true));
    tape.add(new AddAllVertexListInstruction(vertexList));
    tape.add(new OrderVertexListBySecondTimestampInstruction());
    Instruction forLabel2 = new LabelInstruction();
    tape.add(forLabel2);
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 6)));
    tape.add(new SetLabelTextInstruction("Na všechny dosud nenavštívené uzly\nzavolej metodu DFS-VISIT"));
    tape.add(new SetSelectedCellFirstInstruction(vertexList));
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 7)));
    tape.add(new DequeueInstruction(vertexList));
    tape.add(new ActiveToRegisterInstruction(variableU));
    Instruction ifEnd = new LabelInstruction();
    tape.add(new LabelInstruction(ifEnd, new Condition(){
      @Override
      public boolean isFulfil() {
        return (((mxCell)activeVariable).getStyle().equals(stylesheet.getInitVertexStyle()) == false);
      }
    }));
    tape.add(new SelectNextScComponentInstruction());
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 8)));
    tape.add(new SetLabelTextInstruction("Volání metody DFS-VISIT"));
    tape.add(new PauseInstruction());
    tape.add(new ClearQueueInstruction(componentNodes));
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", false));
    Instruction dfsReturnLabel = new LabelInstruction();
    Instruction dfsVisitLabel = new LabelInstruction();
    tape.add(new ContextInstruction(contextStack, dfsReturnLabel));   
    tape.add(new LabelInstruction(dfsVisitLabel, new Condition(){
      @Override
      public boolean isFulfil() {
        return true;
      }
    }));
    tape.add(dfsReturnLabel);
    tape.add(new LabelInstruction(ifEnd, new Condition(){
      @Override
      public boolean isFulfil() {
        return state == SccControllerState.NORMAL_GRAPH_DFS_CALL;
      }
    }));
    tape.add(new SetLabelTextInstruction("Přídaní nově nalezené silně souvislé komponenty"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 4)));
    tape.add(new PauseInstruction());
    tape.add(new AddNewScComponentInstruction());
    //tape.add(new SetColorInstruction(stylesheet.getRootVertexStyle()));
    tape.add(ifEnd);
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", true));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 9)));
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 10)));
    tape.add(new PauseInstruction());
    tape.add(new LabelInstruction(forLabel2, new Condition(){
      @Override
      public boolean isFulfil() {
        return (vertexList.isEmpty() == false);
      }  
    }));
    tape.add(new ClearQueueInstruction(vertexList));
    tape.add(new LabelInstruction(dfsReturnLabel1, new Condition(){
      @Override
      public boolean isFulfil() {
        return state == SccControllerState.NORMAL_GRAPH_DFS_CALL;
      }
    }));
    tape.add(new LabelInstruction(dfsReturnLabel2, new Condition(){
      @Override
      public boolean isFulfil() {
        return state == SccControllerState.TRANSPOSE_GRAPH_DFS_CALL;
      }
    }));
    
    tape.add(dfsVisitLabel);
    tape.add(new EnqueueInstruction(componentNodes));
    tape.add(new ShowOrHideVariableInstruction("v", false));
    tape.add(new RegisterToActiveInstruction(variableU));
    tape.add(new PushInstruction(visitStack));
    tape.add(new SetLabelTextInstruction("Začátek DFS-VISIT"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 0)));
    tape.add(new PauseInstruction());
    tape.add(new SetLabelTextInstruction("První navštívení uzlu"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 1)));
    tape.add(new PauseInstruction());
    tape.add(new SetLabelTextInstruction("Inkrementace času"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 2)));
    tape.add(new SetColorInstruction(stylesheet.getFirstPassVertexStyle()));
    tape.add(new PauseInstruction());
    tape.add(new SetLabelTextInstruction("Nastavení první časové známky"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 3)));
    tape.add(new ChangeTimeInstruction());
    tape.add(new PauseInstruction());
    tape.add(new SetTimeInstruction(true));
    tape.add(new MakeAdjListInstruction(vertexList, true));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 4)));
    //tape.add(new MakeUserAdjListInstruction());
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", true));
    Instruction forEndLabel = new LabelInstruction();
    Instruction forLabel3 = new LabelInstruction();

    tape.add(forLabel3);
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 4)));
    tape.add(new SetLabelTextInstruction("Projdi všechny uzly, které sousedí s právě zpracovávaným"));
    tape.add(new PauseInstruction());
    //tape.add(new SelectNextVisitedNodeInstruction());
    tape.add(new LabelInstruction(forEndLabel, new Condition(){
      @Override
      public boolean isFulfil() {
        return (vertexList.isEmpty() == true);
      }
    }));
    tape.add(new ShowOrHideVariableInstruction("v", true));
    tape.add(new SetLabelTextInstruction("Pokud sousední uzel dosud nebyl navštíven\nnastav mu cestu a zavolej pro něj DFS-VISIT"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 5))); 
    tape.add(new DequeueInstruction(vertexList));
    tape.add(new ActiveToRegisterInstruction(variableV));
    tape.add(new ColorEdgeActiveAndRegisterInstruction(variableU, stylesheet.getHighlighEdgeStyle(), true));
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 6)));
    Instruction ifEnd2 = new LabelInstruction();
    tape.add(new LabelInstruction(ifEnd2, new Condition(){
      @Override
      public boolean isFulfil() {
        return (((mxCell)activeVariable).getStyle().equals(stylesheet.getInitVertexStyle()) == false);
      }
    }));
    tape.add(new SetLabelTextInstruction("Nastavení cesty"));
    tape.add(new ColorEdgeActiveAndRegisterInstruction(variableU, stylesheet.getPathEdgeStyle(), true));
    tape.add(new PauseInstruction());
    tape.add(new SetLabelTextInstruction("Volání metody DFS-VISIT pro sousední uzel"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 7)));
    tape.add(new SetPathInstruction(variableU));
    tape.add(new PauseInstruction());
    tape.add(new ContextInstruction(contextStack, ifEnd2));
    tape.add(new RegisterToActiveInstruction(variableV));
    tape.add(new ActiveToRegisterInstruction(variableU));
    tape.add(new LabelInstruction(dfsVisitLabel, new Condition(){
      @Override
      public boolean isFulfil() {
        return true;
      }
    }));
    tape.add(ifEnd2);
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 8)));
    tape.add(new SetDfsEdgeTypeInstruction(variableU, stylesheet.getPathEdgeStyle(), stylesheet.getEdgeStyle()));
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 9)));
    tape.add(new PauseInstruction());
    tape.add(new LabelInstruction(forLabel3, new Condition(){
      @Override
      public boolean isFulfil() {
        return (vertexList.isEmpty() == false);
      }
    }));
    tape.add(forEndLabel);
    tape.add(new ClearQueueInstruction(vertexList));
    tape.add(new ShowOrHideVariableInstruction("v", false));
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", false));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 10)));
    tape.add(new RegisterToActiveInstruction(variableU));
    tape.add(new SetLabelTextInstruction("Druhé navštívení uzlu"));
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 11)));
    tape.add(new SetColorInstruction(stylesheet.getDoneVertexStyle()));
    tape.add(new SetLabelTextInstruction("Nastavení druhé časové známky"));
    tape.add(new PauseInstruction());
    tape.add(new ChangeTimeInstruction());
    tape.add(new SetTimeInstruction(false));
    tape.add(new DequeueInstruction(visitStack));
    tape.add(new ContextInstruction(contextStack));
  }
  
  /**
   * Metoda uvede ridici jednotku do pocatecniho nastaveni
   */
  private void resetControllerImpl(){
    graphComponentLabel.setText("Algoritmus hledání silně souvislých komponent");
    codePanel.setCurrentLine(1);
    ExtendedColorStyle.resetIndex();
    variables.put("Zásobník S", new VariableObject(){

      @Override
      public String getVariableValue() {
        return dequeToString(visitStack);
      }
      
    });
    variables.put("u", new VariableObject(){

      @Override
      public String getVariableValue() {
        return getNodeName(getRegisterValue(variableU));
      }
      
    });
    variables.put("v", new VariableObject(){

      @Override
      public String getVariableValue() {
        return getNodeName(getRegisterValue(variableV));
      }
      
    });
    
    variables.put("time", new VariableObject(true, false){

      @Override
      public String getVariableValue() {
        return Integer.toString(timeVariable);
      }
      
    });
    
    variables.put("Iterační seznam", new VariableObject(){

      @Override
      public String getVariableValue() {
        return dequeToString(vertexList);
      }
      
    });
    
    for (Map.Entry<String, VariableObject> e : variables.entrySet()){
      if (e.getValue().isEnabled()){
        variablesPanel.addVariable(e.getKey());
      } 
    }
    vertexList.clear();
    contextStack.clear();
    visitStack.clear();
    timeVariable = 0;
    state = SccControllerState.NORMAL_GRAPH_DFS_CALL;
    clearGraphComponent(transposeGraph);
    transposeGraph.getGraph().refresh();
    clearGraphComponent(componentsGraph);
    transposeGraph.getGraph().refresh();
  }
  
  /**
   * Metoda pro reset
   */
  @Override
  public void resetController(){
    graph = mainGraph;
    graphComponent = standardGraph;
    super.resetController();
    resetControllerImpl();
  }
  
  /**
   * Interaktivni instrukce pro vybrani dalsi SC komponenty.
   */
  protected class SelectNextScComponentInstruction extends InteractiveInstruction {
    protected SelectCellsMouseAdapter adapter;
    protected Deque<mxCell> expectedNodes = new LinkedList<>();
    
    public SelectNextScComponentInstruction() {
      adapter = new SelectCellsMouseAdapter(transposeGraph, graph);
      adapter.setActivated(false);
      transposeGraph.getGraphControl().addMouseListener(adapter);
    }
    
    protected void makeExpectedList(mxCell startCell){
      Deque<mxCell> stack = new LinkedList<>();
      stack.push(startCell);
      while(stack.isEmpty() == false){
        mxCell cell = stack.pop();
        for (int i = 0; i < graph.getModel().getEdgeCount(cell); i++){
          mxCell edge = (mxCell) ((mxCell) graph.getModel().getEdgeAt(cell, i));
          mxCell source = (mxCell) edge.getSource();
          mxCell target = (mxCell) edge.getTarget();
          if (source == cell && expectedNodes.contains(target) == false && 
              target.getStyle().equals(stylesheet.getInitVertexStyle())){
            stack.push(target);
          }
        }
        if (expectedNodes.contains(cell) == false){
          expectedNodes.add(cell);
        }
      }
    }
    
    @Override
    public void preform() {
      if (state == SccControllerState.TRANSPOSE_GRAPH_DFS_CALL){
        if (adapter.isActivated() == false){
          expectedNodes.clear(); 
          //System.out.println(getNodeName(getRegisterValue(variableU)));
          makeExpectedList((mxCell)getRegisterValue(variableU));
          graphComponentLabel.setText(getTextForLabel("Vyber uzly další silně souvislé komponenty a klikni pokračovat\n[uživatelská akce]"));
          timer.setEnabled(false);
          adapter.setActivated(true);
        } else if (adapter.getLastSelected() != null){
          Deque<mxCell> selected = adapter.getSelectedNodes();
          if (selected.size() == expectedNodes.size() && selected.containsAll(expectedNodes)){
            stop(); 
            return;
          } else {
            graphComponentLabel.setText(getTextForLabel("Špatně vybrané uzly silně souvislé komponenty\n[uživatelská akce]"));
          }
        }
        pause = true;
      }
    }

    @Override
    public int nextTapePos() {
      if (adapter.isActivated() == true){
        return tapePosition;
      }
      return super.nextTapePos();
    }

    @Override
    public void stop() {
      if (adapter.isActivated()){
        adapter.setActivated(false);
        adapter.resetStyles();
        adapter.reset();
        timer.setEnabled(true); 
      }
    }
  } 
  
  /**
   * Instrukce pro sezareni seznamu podle druhe casove znamky
   */
  protected class OrderVertexListBySecondTimestampInstruction extends BasicInstruction {

    @Override
    public void preform() {
      if (state == SccControllerState.TRANSPOSE_GRAPH_DFS_CALL){
        editBlock.addEdit(new OrderVertexListBySecondTimestampEdit(vertexList, mainGraph));
      }
    }
  }
  
  /**
   * Editacni akce pro sezareni seznamu podle druhe casove znamky
   */
  protected final class OrderVertexListBySecondTimestampEdit extends AlgorithmUndoableEdit{
    protected Deque<mxCell> cellList;
    protected Deque<mxCell> oldCellList;
    protected AlgorithmGraph cellsGraph;
    
    public OrderVertexListBySecondTimestampEdit(Deque<mxCell> cellList, AlgorithmGraph cellsGraph) {
      this.cellList = cellList;
      oldCellList = new LinkedList<>();
      oldCellList.addAll(cellList);
      this.cellsGraph = cellsGraph;
      action();
    }
    
    @Override
    public void undo() {
      super.undo();
      cellList.clear();
      cellList.addAll(oldCellList);
    }
    
    @Override
    protected void action() {
      Collections.sort((List<mxCell>) cellList, new Comparator<mxCell>(){
      private int firstCellTime;
      private int secondCellTime;

      @Override
      public int compare(mxCell o1, mxCell o2) {
        final DfsGraphVertex value1 = (DfsGraphVertex) o1.getValue();
        final DfsGraphVertex value2 = (DfsGraphVertex) o2.getValue();
        firstCellTime = 0;
        secondCellTime = 0;
        cellsGraph.traverseAllCells(new AlgorithmGraphCellVisitor(){
          @Override
          public boolean visit(mxCell cell, GraphCell node) {
            if (value1.getName().equals(node.getName())){
              firstCellTime = ((DfsGraphVertex) node).getSecondTimestamp();
            } else if (value2.getName().equals(node.getName())){
              secondCellTime = ((DfsGraphVertex) node).getSecondTimestamp();
            }
            if (firstCellTime != 0 && secondCellTime != 0){
              return false;
            }
            return true;
          }

          @Override
          public boolean allowEdge() {
            return false;
          }
        });
        return secondCellTime - firstCellTime;
      }

    });
    }
    
  }
  
  /**
   * Instrukce pro pridani noveho uzlu do grafu komponent
   */
  protected class AddNewScComponentInstruction extends BasicInstruction {
 
    @Override
    public void preform() {
      String style = ExtendedColorStyle.getNextStyle();
      for (mxCell cell : componentNodes){
        editBlock.addEdit(new SetColorEdit(style, (mxCell)cell));
      }
      editBlock.addEdit(new AddNewScComponentEdit(componentsGraph, componentNodes, graph, style));
    }
  }

  /**
   * Editacni akce pro pridani noveho uzlu do grafu komponent
   */
  protected final class AddNewScComponentEdit extends AlgorithmUndoableEdit{
    protected AlgorithmGraphComponent gc;
    protected Deque<mxCell> nodesList;
    protected Deque<mxCell> oldList;
    protected mxGraph originalGraph;
    protected List<mxCell> newCells = new ArrayList<>();
    protected String newStyle;
    public AddNewScComponentEdit(AlgorithmGraphComponent gc, Deque<mxCell> nodesList, mxGraph originalGraph, String newStyle) {
      this.gc = gc;
      this.nodesList = nodesList;
      this.originalGraph = originalGraph;
      this.newStyle = newStyle;
      oldList = new LinkedList<>();
      oldList.addAll(nodesList);
      action();
    }
    
    @Override
    public void undo() {
      super.undo();
      nodesList.clear();
      nodesList.addAll(oldList);
      for (mxCell cell : newCells){
        gc.getGraph().getModel().remove(cell);
      }
      newCells.clear();
    }
    
    protected void makeEdges(AlgorithmGraph cg){
      Map<mxCell, Set<mxCell>> adjs = new HashMap<>();
      for (int i = 0; i < cg.getModel().getChildCount(cg.getDefaultParent()); i++){
        mxCell cell1 = (mxCell) cg.getModel().getChildAt(cg.getDefaultParent(), i);
        if (cell1.isVertex()){
          adjs.put(cell1, ((SccGraphCell)cell1.getValue()).makeAdj(originalGraph, true));
        }
      }
      for (Entry<mxCell, Set<mxCell>> e1 : adjs.entrySet()){
        for (Entry<mxCell, Set<mxCell>> e2 : adjs.entrySet()){
          if (e1.getKey() != e2.getKey() && cg.getEdgesBetween(e1.getKey(), e2.getKey(), false).length == 0){
            for (mxCell cellInSet : e1.getValue()){
              if (((SccGraphCell) e2.getKey().getValue()).getNodes().contains(cellInSet)){
                newCells.add((mxCell)cg.insertEdge(cg.getDefaultParent(), null, 
                                                   new DfsGraphEdge(), e1.getKey(), 
                                                   e2.getKey(), stylesheet.getEdgeStyle()));
                break;
              }
            }
          }
        }
      }
    }
    
    @Override
    protected void action() {
      final AlgorithmGraph cg = (AlgorithmGraph) gc.getGraph();
      double x = 0;
      double y = 0;
      StringBuilder name = new StringBuilder();
      if (nodesList.size() > 0){
        SccGraphCell newCell = new SccGraphCell();
        for (mxCell cell : nodesList){
          DfsGraphVertex value = (DfsGraphVertex) cell.getValue();
          name.append(value.getName());
          x = x + cell.getGeometry().getX();
          y = y + cell.getGeometry().getY();
          newCell.addNode(cell);
        }
        newCell.setName(name.toString());
        x = x / nodesList.size();
        y = y / nodesList.size();
        newCells.add((mxCell)cg.insertVertex(cg.getDefaultParent(), null, newCell, 
                                             x, y, 80, 80, newStyle));
        nodesList.clear();
        makeEdges(cg);
      }
    }
  }
  
  /**
   * Instrukce pro vytvoreni transponovaneho grafu
   */
  protected class MakeTransposeGraphInstruction extends BasicInstruction {

    @Override
    public void preform() {
      final AlgorithmGraph tg = (AlgorithmGraph) transposeGraph.getGraph();
      tg.addCells(graph.cloneCells(graph.getChildCells(graph.getDefaultParent())));
      tg.traverseAllCells(new AlgorithmGraph.AlgorithmGraphCellVisitor(){
        @Override
        public boolean visit(mxCell cell, GraphCell node) {
          if (cell.isVertex()){
            cell.setValue(new DfsGraphVertex(node.getName()));
            cell.setStyle(stylesheet.getVertexStyle());
          } else if (cell.isEdge()){
            cell.setValue(new DfsGraphEdge());
            cell.setStyle(stylesheet.getEdgeStyle());
            mxCell target = (mxCell) cell.getTarget();
            mxCell source = (mxCell) cell.getSource();
            cell.setSource(target);
            cell.setTarget(source);
          }
          cell.setVisible(false);
          return true;
        }
        
        @Override
        public boolean allowEdge() {
          return true;
        }
      });
      editBlock.addEdit(new MakeTransposeGraphVisibleEdit(transposeGraph));
    }
  }
  
  /**
   * Editacni akce pro vytvoreni transponovaneho grafu
   */
  protected final class MakeTransposeGraphVisibleEdit extends AlgorithmUndoableEdit{
    protected AlgorithmGraphComponent gc;
    
    public MakeTransposeGraphVisibleEdit(AlgorithmGraphComponent gc) {
      this.gc = gc;
      action();
    }
    
    protected void setVisible(AlgorithmGraph tg, final boolean visible){
      tg.traverseAllCells(new AlgorithmGraph.AlgorithmGraphCellVisitor(){

        @Override
        public boolean visit(mxCell cell, GraphCell value) {
          cell.setVisible(visible);
          return true;
        }

        @Override
        public boolean allowEdge() {
          return true;
        }
      });
    }
    
    @Override
    public void undo() {
      super.undo();
      setVisible(gc.getGraph(), false);
      gc.getGraph().refresh();
    }
    
    @Override
    protected void action() {
      setVisible(gc.getGraph(), true);
      gc.getGraph().refresh();
    }
  }
  
  /**
   * Instrukce pro nastaveni stavu pri pruchodu algoritmu SCC
   */
  protected class SetSccAlgStateInstruction extends BasicInstruction {
    protected SccControllerState newState;

    public SetSccAlgStateInstruction(SccControllerState newState) {
      this.newState = newState;
    }

    @Override
    public void preform() {
      editBlock.addEdit(new SetSccAlgStateEdit(newState));
    }
  }
  
  /**
   * Editacni akce pro nastaveni stavu pri pruchodu algoritmu SCC
   */
  protected final class SetSccAlgStateEdit extends AlgorithmUndoableEdit{
    protected SccControllerState newState;
    protected SccControllerState oldState;
    
    public SetSccAlgStateEdit(SccControllerState newState) {
      this.newState = newState;
      this.oldState = state;
      action();
    }
    
    @Override
    public void undo() {
      super.undo();
      state = oldState;
    }
    
    @Override
    protected void action() {
      state = newState;
    }
  }
  
}
