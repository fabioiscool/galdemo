package simulator.controllers;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxConnectionConstraint;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JLabel;
import simulator.graphs.AlgorithmGraph;
import simulator.graphs.AlgorithmGraph.AlgorithmGraphCellVisitor;
import simulator.graphs.AlgorithmGraphComponent;
import simulator.graphs.GraphCell;
import simulator.graphs.dfs.DfsGraphVertex;
import simulator.gui.VariablesPanel;
import simulator.gui.VisualizationPseudocodePanel;

/**
 * Ridici jednotka pro algoritmus topologickeho razeni
 * @author JVaradinek
 */
public class TopologicalSortController extends AbstractDfsController {
  
  protected Deque<mxCell> listL = new LinkedList<>();
  
  protected AlgorithmGraphComponent graphComponentList;
  
  public TopologicalSortController(final AlgorithmGraphComponent graphCom, final JLabel graphComponentLabel, 
                       final VisualizationPseudocodePanel completeCodePanel, 
                       final VariablesPanel variablesPanel, final AlgorithmGraphComponent graphComponentList) {
    
    super(graphCom, graphComponentLabel, completeCodePanel, variablesPanel);

    this.graphComponentList = graphComponentList;
    interactiveMode = false;
    resetControllerImpl();
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 1), algorithm.getRealLineNumber(0, 3), Color.GREEN));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(1, 1), algorithm.getRealLineNumber(1, 10), Color.GREEN));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(2, 1), algorithm.getRealLineNumber(2, 11), Color.GREEN));
    tape.add(new SetLabelTextInstruction("Volání algoritmu DFS"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 1)));
    tape.add(new PauseInstruction());
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 1), algorithm.getRealLineNumber(0, 1), doneColor));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 0)));
    tape.add(new PauseInstruction()); 
    tape.add(new SetLabelTextInstruction("Inicializace všech uzlů"));
    tape.add(new AddAllVertexListInstruction(vertexList));
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
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(1, 1), algorithm.getRealLineNumber(1, 4), doneColor));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 5)));
    tape.add(new SetLabelTextInstruction("Inicializace času"));
    tape.add(new PauseInstruction());
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(1, 5), algorithm.getRealLineNumber(1, 5), doneColor));
    tape.add(new ShowOrHideVariableInstruction("time", true));
    tape.add(new AddAllVertexListInstruction(vertexList));
    Instruction forLabel2 = new LabelInstruction();
    tape.add(forLabel2);
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 6)));
    tape.add(new SetLabelTextInstruction("Na všechny dosud nenavštívené uzly\nzavolej metodu DFS-VISIT"));
    tape.add(new SelectNextNodeInstruction());
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
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 8)));
    tape.add(new SetLabelTextInstruction("Volání metody DFS-VISIT"));
    tape.add(new PauseInstruction());
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", false));
    tape.add(new ContextInstruction(contextStack, ifEnd));
    Instruction dfsVisitLabel = new LabelInstruction();
    tape.add(new LabelInstruction(dfsVisitLabel, new Condition(){
      @Override
      public boolean isFulfil() {
        return true;
      }
    }));
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
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(2, 1), algorithm.getRealLineNumber(2, 11), doneColor));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(1, 1), algorithm.getRealLineNumber(1, 10), doneColor));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 2), algorithm.getRealLineNumber(0, 2), doneColor));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 3)));
    tape.add(new SetLabelTextInstruction("Vrať výsledek jako seznam L"));
    tape.add(new PauseInstruction());
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 3), algorithm.getRealLineNumber(0, 3), doneColor));
    tape.add(new SetLabelTextInstruction("Algoritmus Topologického uspořádání dokončen"));
    tape.add(new EndInstruction());
    
    tape.add(dfsVisitLabel);
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
    tape.add(new SelectNextVisitedNodeInstruction());
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
    tape.add(new SetDfsEdgeTypeInstruction(variableU, stylesheet.getPathEdgeStyle(), 
                                           stylesheet.getEdgeStyle()));
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
    tape.add(new SetColorInstruction(stylesheet.DONE_VERTEX_STYLE));
    tape.add(new SetLabelTextInstruction("Nastavení druhé časové známky"));
    tape.add(new PauseInstruction());
    tape.add(new ChangeTimeInstruction());
    tape.add(new SetTimeInstruction(false));
    tape.add(new DequeueInstruction(visitStack));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 2)));
    tape.add(new SetLabelTextInstruction("Přidání uzlu do seznamu L"));
    tape.add(new AddActiveNodeToListLInstruction());
    tape.add(new PauseInstruction());
    tape.add(new ContextInstruction(contextStack));
  }
  
  /**
   * Metoda uvede ridici jednotku do pocatecniho nastaveni
   */
  private void resetControllerImpl(){
    graphComponentLabel.setText("Algoritmus topologického uspořádání");
    codePanel.setCurrentLine(1);
    variables.put("Seznam L", new VariableObject(){

      @Override
      public String getVariableValue() {
        return dequeToString(listL);
      }
      
    });
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
    contextStack.clear();
    vertexList.clear();
    listL.clear();
    visitStack.clear();
    timeVariable = 0;
    clearGraphComponent(graphComponentList);
    graphComponentList.getGraph().refresh();
  }
  
  /**
   * Metoda pro reset
   */
  @Override
  public void resetController(){
    super.resetController();
    resetControllerImpl();
  }
  
  /**
   * Interaktivni instrukce pro uzivatelske vybrani dalsiho uzlu
   */
  protected class SelectNextVisitedNodeInstruction extends InteractiveInstruction {
    protected SelectCellsMouseAdapter adapter;
    protected mxCell lastClosed = null;
    protected Set<mxCell> canBeVisitedSet = new HashSet<>();
    
    public SelectNextVisitedNodeInstruction() {
      adapter = new SelectCellsMouseAdapter(1, graphComponent, graph);
      adapter.setActivated(false);
      graphComponent.getGraphControl().addMouseListener(adapter);
    }
    
    @Override
    public void preform() {
      if (adapter.isActivated() == false){
        if (lastClosed == getRegisterValue(variableU)){
          return;
        }
        canBeVisitedSet.clear();
        for (int i = 0; i < graph.getModel().getEdgeCount(getRegisterValue(variableU)); i++){
          mxCell edge = (mxCell) ((mxCell) graph.getModel().getEdgeAt(getRegisterValue(variableU), i));
          mxCell source = (mxCell) edge.getSource();
          mxCell target = (mxCell) edge.getTarget();
          DfsGraphVertex value = (DfsGraphVertex) target.getValue();
          if (source == getRegisterValue(variableU) && value.getFirstTimestamp() == null){
            canBeVisitedSet.add(target);
          }
        }
        graphComponentLabel.setText(getTextForLabel("Vyber další uzel, který bude navštívený a klikni pokračovat\n[uživatelská akce]"));
        timer.setEnabled(false);
        adapter.setActivated(true);
      } else if (adapter.getLastSelected() != null) {
        mxCell selected = adapter.getLastSelected();
        if ((canBeVisitedSet.isEmpty() == true && selected == getRegisterValue(variableU)) || canBeVisitedSet.contains(selected)){
          if (canBeVisitedSet.contains(selected) == false){
            lastClosed = selected;
          }
          if (vertexList.isEmpty() == false && vertexList.contains(selected)){
            vertexList.remove(selected);
            vertexList.addFirst(selected);
          }
          stop();
          return;
        }
      }
      pause = true;
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
    
    @Override
    public int nextTapePos() {
      if (adapter.isActivated() == true){
        return tapePosition;
      }
      return super.nextTapePos();
    }
  }
  
  /**
   * Interaktivni instrukce pro uzivatelske vybrani dalsiho uzlu
   */
  protected class SelectNextNodeInstruction extends InteractiveInstruction {
    protected SelectCellsMouseAdapter adapter;
 
    public SelectNextNodeInstruction() {
      adapter = new SelectCellsMouseAdapter(1, graphComponent, graph);
      adapter.setActivated(false);
      graphComponent.getGraphControl().addMouseListener(adapter);
    }
    
    @Override
    public void preform() {
      if (adapter.isActivated() == false){
        graph.traverseAllCells(new AlgorithmGraphCellVisitor(){

          @Override
          public boolean visit(mxCell vertex, GraphCell value) {
            if (vertex.getStyle().equals(stylesheet.getInitVertexStyle())){
              graphComponentLabel.setText(getTextForLabel("Vyber dosud nenavštívený uzel a klikni pokračovat\n[uživatelská akce]"));
              timer.setEnabled(false);
              adapter.setActivated(true);
              return false;
            }
            return true;
          }

          @Override
          public boolean allowEdge() {
            return false;
          }
          
        });

      } else if (adapter.getLastSelected() != null){
        graph.traverseAllCells(new AlgorithmGraphCellVisitor(){
          @Override
          public boolean visit(mxCell vertex, GraphCell node) {
            if (vertex == adapter.getLastSelected()){
              node.setSelected(true);
            } else {
              node.setSelected(false);
            }
            return true;
          }   

          @Override
          public boolean allowEdge() {
            return false;
          }
        });
        stop(); 
        return;
      }
      pause = true;
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
   * Instrukce pro pridani aktivniho uzlu do seznamu
   */
  protected class AddActiveNodeToListLInstruction extends BasicInstruction {

    @Override
    public void preform() {
      editBlock.addEdit(new AddActiveNodeToListLEdit((mxCell)activeVariable));
    }
  }
  
  /**
   * Editacni akce pro pridani aktivniho uzlu do seznamu
   */
  protected final class AddActiveNodeToListLEdit extends AlgorithmUndoableEdit { 
    private final int VERTEX_X = 10;
    private final int VERTEX_Y = 60;
    private final int VERTEX_WIDTH = 80;
    private final int VERTEX_HEIGHT = 60;
    private final int VERTEX_MOVE = 120;
    
    private final int MAX_LEVEL = 4;
    
    private final int EDGE_LEVEL_STEP = 12;
    
    protected mxCell inserted;
    protected mxCell newLGraphVertex;
    
    public AddActiveNodeToListLEdit(mxCell active) {
      this.inserted = active;
      action();
    }

    @Override
    public void undo() {
      super.undo();
      listL.remove(newLGraphVertex);
      AlgorithmGraph listGraph = (AlgorithmGraph) graphComponentList.getGraph();
      for (Object edge : listGraph.getEdges(newLGraphVertex)){
        listGraph.getModel().remove(edge);
      }
      listGraph.getModel().remove(newLGraphVertex);
      listGraph.moveCells(listGraph.getChildCells(listGraph.getDefaultParent()), -1 * VERTEX_MOVE, 0);  
      arrangeEdges(listGraph);
      listGraph.refresh();
    }

    protected void insertAllEdges(AlgorithmGraph listGraph, List<List<mxCell>> levelsMap){
      int minLevel = 1;
      for (int i = 1; i < levelsMap.size(); i++ ){
        if (levelsMap.get(i).size() > 0){
          minLevel = i;
          break;
        }
      }
      int maxLevel = levelsMap.size() - minLevel + 1;
      if (maxLevel > MAX_LEVEL){
        maxLevel = MAX_LEVEL;
      }
      for (int i = 1; i < levelsMap.size(); i++){
        int r = 0;
        for (mxCell edge : levelsMap.get(i)){ 
          r = r + (EDGE_LEVEL_STEP - 2)/levelsMap.get(i).size();
          mxCell source = (mxCell) edge.getSource();
          mxCell target = (mxCell) edge.getTarget();
          Point p1 = (listGraph.getModel()).getGeometry(source).getPoint();
          Point p2 = (listGraph.getModel()).getGeometry(target).getPoint();
          int level = i - minLevel + 1;
          mxGeometry geometryOfEdge = (listGraph.getModel()).getGeometry(edge);
          geometryOfEdge = (mxGeometry) geometryOfEdge.clone();
          List<mxPoint> pointsOfTheEdge = new ArrayList<>();
          if (level > MAX_LEVEL){
            level = level - MAX_LEVEL;
            listGraph.setConnectionConstraint(edge, source, true, new mxConnectionConstraint(new mxPoint((1.0 - (double)level /maxLevel), 1.0), true));
            listGraph.setConnectionConstraint(edge, target, false, new mxConnectionConstraint(new mxPoint((double)level /maxLevel, 1.0), true));
            pointsOfTheEdge.add(new mxPoint(p1.x + VERTEX_WIDTH*(1.0 - (double)level /maxLevel) , p1.y + VERTEX_HEIGHT + EDGE_LEVEL_STEP * level + r));
            pointsOfTheEdge.add(new mxPoint(p2.x + VERTEX_WIDTH*((double)level /maxLevel) , p2.y + VERTEX_HEIGHT + EDGE_LEVEL_STEP * level + r));
          } else {
            level = level - 1;
            listGraph.setConnectionConstraint(edge, source, true, new mxConnectionConstraint(new mxPoint((1.0 - (double)level /maxLevel), 0), true));
            listGraph.setConnectionConstraint(edge, target, false, new mxConnectionConstraint(new mxPoint((double)level /maxLevel, 0), true));
            pointsOfTheEdge.add(new mxPoint(p1.x + VERTEX_WIDTH*(1.0 - (double)level /maxLevel) , p1.y - EDGE_LEVEL_STEP * (level + 1) - r));
            pointsOfTheEdge.add(new mxPoint(p2.x + VERTEX_WIDTH*((double)level /maxLevel) , p2.y - EDGE_LEVEL_STEP * (level + 1) - r));
          }
          
          geometryOfEdge.setPoints(pointsOfTheEdge);
          (listGraph.getModel()).setGeometry(edge, geometryOfEdge);
        }
      }
    }
    
    protected void arrangeEdges(AlgorithmGraph listGraph){
      for (mxCell vertex : listL){
        listGraph.moveCells(listGraph.getChildCells(listGraph.getDefaultParent()), VERTEX_MOVE, 0);
        mxGeometry cellGeo = (listGraph.getModel()).getGeometry(vertex);
        cellGeo = (mxGeometry) cellGeo.clone();
        cellGeo.setX(VERTEX_X);
        cellGeo.setY(VERTEX_Y);    
        (listGraph.getModel()).setGeometry(vertex, cellGeo); 
      }
      List<List<mxCell>> levelsMap = new ArrayList<>();
      for (Object o : listGraph.getChildEdges(listGraph.getDefaultParent())){
        mxCell edge = (mxCell) o;
        Point p1 = (listGraph.getModel()).getGeometry(((mxCell)edge).getSource()).getPoint();
        Point p2 = (listGraph.getModel()).getGeometry(((mxCell)edge).getTarget()).getPoint();
        int level;
        if (p1.x > p2.x){
          level = (p1.x - p2.x)/VERTEX_MOVE;
        } else {
          level = (p2.x - p1.x)/VERTEX_MOVE;
        }
        for (int f = levelsMap.size(); f < level; f++){
          levelsMap.add(f, new ArrayList<mxCell>());
        }
        levelsMap.get(level - 1).add((mxCell)edge);
        listGraph.resetEdge(edge);
      }
      insertAllEdges(listGraph, levelsMap);
    }
    
    protected void insertNewEdges(AlgorithmGraph listGraph, mxCell active){
      for (int i = 0; i < graph.getModel().getEdgeCount(active); i++){
        mxCell edge = (mxCell) graph.getModel().getEdgeAt(active, i);
        GraphCell source = (GraphCell) edge.getSource().getValue();
        GraphCell target = (GraphCell) edge.getTarget().getValue(); 
        mxCell sourceIn = null;
        mxCell targetIn = null;
        for (mxCell cellInList : listL){
          if (sourceIn == null && ((GraphCell)cellInList.getValue()).getName().equals(source.getName())){
            sourceIn = cellInList;
          } else if (targetIn == null && ((GraphCell)cellInList.getValue()).getName().equals(target.getName())){
            targetIn = cellInList;
          }
        } 
        if (sourceIn != null && targetIn != null){
          listGraph.insertEdge(listGraph.getDefaultParent(), null, null, sourceIn, targetIn, edge.getStyle()); 
        }
      }
    }
    
    @Override
    protected void action() {
      AlgorithmGraph listGraph = (AlgorithmGraph) graphComponentList.getGraph();
      listGraph.moveCells(listGraph.getChildCells(listGraph.getDefaultParent()), VERTEX_MOVE, 0);  
      newLGraphVertex = (mxCell) listGraph.insertVertex(listGraph.getDefaultParent(), null, 
                                                ((DfsGraphVertex)(this.inserted).getValue()), VERTEX_X, 
                                                VERTEX_Y, VERTEX_WIDTH, VERTEX_HEIGHT, stylesheet.getMiniVertexStyle());
      listL.addLast(newLGraphVertex);
      insertNewEdges(listGraph, this.inserted);
      arrangeEdges(listGraph);
      listGraph.refresh();
    }
    
  }
}
