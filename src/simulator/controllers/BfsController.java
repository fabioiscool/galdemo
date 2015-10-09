package simulator.controllers;

import com.mxgraph.model.mxCell;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map.Entry;
import javax.swing.JLabel;
import simulator.graphs.AlgorithmGraph.AlgorithmGraphCellVisitor;
import simulator.graphs.AlgorithmGraphComponent;
import simulator.graphs.GraphCell;
import simulator.graphs.bfs.BfsGraphCell;
import simulator.gui.VariablesPanel;
import simulator.gui.VisualizationPseudocodePanel;

/**
 * Ridici jednotka pro algorimus BFS.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class BfsController extends AbstractController {

  protected Deque<mxCell> queue = new LinkedList<>();
  protected Deque<mxCell> vertexList = new LinkedList<>();
  protected Deque<mxCell> userQueue;
  
  protected Integer variableS = 0;
  protected Integer variableV = 1;
  protected Integer variableU = 2; 
  
  /**
   * inicializace a naplneni pasky instrukci
   * @param graphCom
   * @param graphComponentLabel
   * @param completeCodePanel
   * @param variablesPanel 
   */
  public BfsController(final AlgorithmGraphComponent graphCom, final JLabel graphComponentLabel, 
                       final VisualizationPseudocodePanel completeCodePanel, 
                       final VariablesPanel variablesPanel) {
    
    super(graphCom, graphComponentLabel, completeCodePanel, variablesPanel);

    resetControllerImpl();
    interactiveMode = false;
    tape.add(new SelectStartNodeInstruction());
    
    tape.add(new SetLabelTextInstruction("Inicializace všech uzlů kromě počátečního"));
    tape.add(new InitInstruction());
    tape.add(new ShowOrHideVariableInstruction("v", false));
    tape.add(new ShowOrHideVariableInstruction("Q", false));
    Instruction forLabel = new LabelInstruction();
    Instruction forEndLabel = new LabelInstruction();
    tape.add(forLabel);
    tape.add(new SetCursorInstruction(2));
    tape.add(new SetColorHighlighInstruction(2, 22, Color.GREEN));
    tape.add(new PauseInstruction());
    tape.add(new LabelInstruction(forEndLabel, new Condition(){
      @Override
      public boolean isFulfil() {
        return (vertexList.isEmpty() == true);
      }
    }));
    tape.add(new SetCursorInstruction(3));
    tape.add(new DequeueInstruction(vertexList));
    tape.add(new ActiveToRegisterInstruction(variableU));
    tape.add(new PauseInstruction());
    
    tape.add(new SetCursorInstruction(4));
    tape.add(new SetColorInstruction(stylesheet.getInitVertexStyle()));
    tape.add(new PauseInstruction());
    
    tape.add(new SetCursorInstruction(5));
    tape.add(new SetVertexEnableInstruction());
    tape.add(new SetVertexInfinityInstruction());
    tape.add(new PauseInstruction());
    
    tape.add(new SetCursorInstruction(6));
    tape.add(new SetVertexToolTipsInstruction());
    tape.add(new SetPathInstruction());
    tape.add(new PauseInstruction());
    
    tape.add(new LabelInstruction(forLabel, new Condition(){
      @Override
      public boolean isFulfil() {
        return (vertexList.isEmpty() == false);
      }  
    }));
    tape.add(forEndLabel);
    tape.add(new ShowOrHideVariableInstruction("u", false));
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", false));
    tape.add(new SetCursorInstruction(7));
    tape.add(new SetColorHighlighInstruction(2, 6, doneColor));
    tape.add(new SetLabelTextInstruction("Inicializace počátečního uzlu"));
    tape.add(new PauseInstruction().setBigPause(true));
    
    tape.add(new SetCursorInstruction(8));
    tape.add(new RegisterToActiveInstruction(variableS));
    tape.add(new SetColorInstruction(stylesheet.getFirstPassVertexStyle()));
    tape.add(new SetColorHighlighInstruction(2, 7, doneColor));
    tape.add(new PauseInstruction());
    
    tape.add(new SetCursorInstruction(9));
    tape.add(new SetVertexEnableInstruction());
    tape.add(new SetDistanceInstruction());
    tape.add(new SetColorHighlighInstruction(2, 8, doneColor));
    tape.add(new PauseInstruction());
    
    tape.add(new SetCursorInstruction(10));
    tape.add(new SetVertexToolTipsInstruction());
    tape.add(new SetPathInstruction());
    tape.add(new SetColorHighlighInstruction(2, 9, doneColor));
    tape.add(new SetLabelTextInstruction("Inicializace fronty Q"));
    tape.add(new PauseInstruction().setBigPause(true));
    
    tape.add(new ShowOrHideVariableInstruction("Q", true));
    tape.add(new SetCursorInstruction(11));
    tape.add(new ClearQueueInstruction(queue));
    tape.add(new SetColorHighlighInstruction(2, 10, doneColor));
    tape.add(new PauseInstruction());
    tape.add(new EnqueueInstruction(queue));
    Instruction whileLabel = new LabelInstruction();
    Instruction endWhile = new LabelInstruction(whileLabel, new Condition(){
      @Override
      public boolean isFulfil() {
        return (queue.isEmpty() == false);
      }  
    });
    
    tape.add(new LabelInstruction(endWhile, new Condition(){
      @Override
      public boolean isFulfil() {
        return queue.isEmpty();
      }  
    }));
    
    tape.add(whileLabel);
    tape.add(new SetLabelTextInstruction("Zpracování dalšího uzlu ve frontě"));
    tape.add(new SetCursorInstruction(12));
    tape.add(new SetColorHighlighInstruction(2, 11, doneColor));
    tape.add(new PauseInstruction());
    
    tape.add(new SetLabelTextInstruction("Výběr prvního uzlu z fronty"));
    tape.add(new SetCursorInstruction(13));
    tape.add(new SelectNextDequeNodeInstruction());
    tape.add(new PauseInstruction());
    
    tape.add(new DequeueInstruction(queue));
    tape.add(new ActiveToRegisterInstruction(variableU));
    tape.add(new ShowOrHideVariableInstruction("u", true));
    tape.add(new SetColorInstruction(stylesheet.getDoneVertexStyle()));
    tape.add(new SetCursorInstruction(14));
    tape.add(new MakeAdjListInstruction(vertexList, graph.isOriented()));
    tape.add(new SelectNodesToQNodeInstruction());
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", true));
    Instruction forLabel2 = new LabelInstruction();
    Instruction forEndLabel2 = new LabelInstruction();
    tape.add(forLabel2);
    tape.add(new SetCursorInstruction(14));
    tape.add(new SetLabelTextInstruction("Projdi všechny uzly, které sousedí s právě zpracovávaným"));
    tape.add(new PauseInstruction());
    
    tape.add(new LabelInstruction(forEndLabel2, new Condition(){
      @Override
      public boolean isFulfil() {
        return vertexList.isEmpty();
      }
    }));
    tape.add(new DequeueInstruction(vertexList));
    tape.add(new ActiveToRegisterInstruction(variableV));
    tape.add(new ShowOrHideVariableInstruction("v", true));
    tape.add(new SetCursorInstruction(15));   
    tape.add(new SetLabelTextInstruction("Pokud uzel ještě nebyl navštíven\nuprav barvu, vzdálenost, cestu a vlož ho do fronty"));
    tape.add(new PauseInstruction());
    
    tape.add(new SetCursorInstruction(16));
    Instruction ifEnd = new LabelInstruction();
    tape.add(new LabelInstruction(ifEnd, new Condition(){
      @Override
      public boolean isFulfil() {
        return (((mxCell)activeVariable).getStyle().equals(stylesheet.getInitVertexStyle()) == false);
      }
    }));
    tape.add(new ColorEdgeActiveAndRegisterInstruction(variableU, stylesheet.getHighlighEdgeStyle(), graph.isOriented()));
    tape.add(new PauseInstruction());
    
    tape.add(new SetCursorInstruction(17));
    tape.add(new SetColorInstruction(stylesheet.getFirstPassVertexStyle()));
    tape.add(new PauseInstruction());
    
    tape.add(new SetCursorInstruction(18));
    tape.add(new SetVertexInfinityInstruction());
    tape.add(new SetDistanceInstruction(variableU));
    tape.add(new PauseInstruction());
    
    tape.add(new SetCursorInstruction(19));
    tape.add(new SetPathInstruction(variableU));
    tape.add(new PauseInstruction());
    
    tape.add(new EnqueueInstruction(queue));
    tape.add(ifEnd);
    tape.add(new SetCursorInstruction(20));
    tape.add(new PauseInstruction());
    
    tape.add(new SetCursorInstruction(21)); 
    tape.add(new PauseInstruction());
    
    tape.add(new LabelInstruction(forLabel2, new Condition(){
      @Override
      public boolean isFulfil() {
        return (vertexList.isEmpty() == false);
      }
    }));
    tape.add(forEndLabel2);
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", false));
    tape.add(new ShowOrHideVariableInstruction("v", false));
    tape.add(new SetCursorInstruction(22));
    tape.add(new PauseInstruction());
    
    tape.add(endWhile);
    tape.add(new SetColorHighlighInstruction(2, 22, doneColor));
    tape.add(new SetCursorInstruction(22));
    tape.add(new SetLabelTextInstruction("Algoritmus BFS dokončen"));
    tape.add(new EndInstruction());
  }
  
  /**
   * Metoda uvede ridici jednotku do pocatecniho nastaveni
   */
  private void resetControllerImpl(){
    graphComponentLabel.setText("Algoritmus BFS");
    codePanel.setCurrentLine(1);
    variables.put("s", new VariableObject(){

      @Override
      public String getVariableValue() {
        return getNodeName(getRegisterValue(variableS));
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
    variables.put("Q", new VariableObject(){

      @Override
      public String getVariableValue() {
        return dequeToString(queue);
      }
      
    });
    variables.put("Iterační seznam", new VariableObject(){

      @Override
      public String getVariableValue() {
        return dequeToString(vertexList);
      }
      
    });
    
    variables.put("UserQueue", new VariableObject(false){

      @Override
      public String getVariableValue() {
        return dequeToString(userQueue);
      }
      
    });
    
    for (Entry<String, VariableObject> e : variables.entrySet()){
      if (e.getValue().isEnabled()){
        variablesPanel.addVariable(e.getKey());
      } 
    }
    queue.clear();
    vertexList.clear();
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
   * Pomocna metoda pro ziskani vzdalenosti.
   * V BFS pouze formou inkrementace o 1.
   * @param first nevyuziva se
   * @param second
   * @return 
   */
  protected int getDistance(Object first, Object second) {
    if (second instanceof mxCell){
      return ((BfsGraphCell)((mxCell)second).getValue()).getIntValue() + 1;
    }
    return 0;
  } 
  
  /**
   * Interaktini instrukce pro uzivatelsky vyber uzlu, ktere se maji vlozit do fronty Q.
   */
  protected class SelectNodesToQNodeInstruction extends InteractiveInstruction {
    protected SelectCellsMouseAdapter adapter;
    protected Deque<mxCell> expectedQueue = new LinkedList<>();
    
    public SelectNodesToQNodeInstruction() {
      adapter = new SelectCellsMouseAdapter(graphComponent, graph);
      adapter.setActivated(false);
      adapter.addPropertyChangeListener(new PropertyChangeListener(){

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          userQueue = adapter.getSelectedNodes();
          variablesPanel.addVariable("UserQueue", variables.get("UserQueue").toString());
        }
        
      });
      graphComponent.getGraphControl().addMouseListener(adapter);
    }
    
    protected void MakeExpectedQueue(){
      expectedQueue.clear();
      for (mxCell cell : vertexList){
        if (cell.getStyle().equals(stylesheet.getInitVertexStyle())){
          expectedQueue.add(cell);
        }
      }
    }
    
    protected boolean isSelectedCorrect(Deque<mxCell> selected){
      if (selected.size() != expectedQueue.size()){
        return false;
      }
      for (mxCell cell : expectedQueue){
        if (selected.contains(cell) == false){
          return false;
        }
      }
      return true;
    }
    
    @Override
    public void preform() {
      graphComponentLabel.setText(getTextForLabel("Vyber všechny uzly, které se vloží do fronty a klikni pokračovat\n[uživatelská akce]"));
      if (adapter.isActivated() == false){
        timer.setEnabled(false);
        MakeExpectedQueue();
        adapter.setActivated(true);
        variables.get("UserQueue").setEnabled(true);
      } else if (isSelectedCorrect(adapter.getSelectedNodes())){
        vertexList.removeAll(adapter.getSelectedNodes());
        vertexList.addAll(adapter.getSelectedNodes());
        stop();
        return;
      } else {
        graphComponentLabel.setText(getTextForLabel("Špatně vybrané uzly\n[uživatelská akce]"));
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
   * Interaktvni instrukce pro uzivatelsky vyber dalsiho uzlu odebraneho z Q
   */
  protected class SelectNextDequeNodeInstruction extends InteractiveInstruction {
    protected SelectCellsMouseAdapter adapter;
    
    public SelectNextDequeNodeInstruction() {
      adapter = new SelectCellsMouseAdapter(1, graphComponent, graph);
      adapter.setActivated(false);
      graphComponent.getGraphControl().addMouseListener(adapter);
    }
    
    @Override
    public void preform() {
      graphComponentLabel.setText(getTextForLabel("Vyber uzel, který bude odebrán z fronty a klikni pokračovat\n[uživatelská akce]"));
      if (adapter.isActivated() == false){
        timer.setEnabled(false);
        adapter.setActivated(true);
      } else if (adapter.getLastSelected() != null){
        if (queue.peekFirst().equals(adapter.getLastSelected())){
          stop();
          return;
        }
        graphComponentLabel.setText(getTextForLabel("Špatně vybraný uzel\n[uživatelská akce]"));
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
          adapter.reset();
          timer.setEnabled(true);
       }
    }
  }

  /**
   * Instrukce pro inicializaci
   */
  protected class InitInstruction extends BasicInstruction {
    protected boolean selectedFound = false;
    
    @Override
    public void preform() {
      graph.traverseAllCells(new AlgorithmGraphCellVisitor(){
        @Override
        public boolean visit(mxCell vertex, GraphCell node) {
          if (node.isSelected() == false){
            vertexList.add(vertex);
          } else {
            selectedFound = true;
            editBlock.addEdit(new SetColorEdit(stylesheet.getSelectedVertexStyle(), vertex));
            setRegisterValue(vertex, variableS);
          }
          return true;
        }
        
        @Override
        public boolean allowEdge() {
          return false;
        }
      });
      if (selectedFound == false){
        Object[] vertices =  graph.getChildVertices(graph.getDefaultParent());
        if (vertices.length > 0){
          mxCell vertex = (mxCell)vertices[0];
          vertexList.remove(vertex);
          ((GraphCell)vertex.getValue()).setSelected(true);
          editBlock.addEdit(new SetColorEdit(stylesheet.getSelectedVertexStyle(), vertex));
          setRegisterValue(vertex, variableS);
        }
      }
    }
  }
  
  /**
   * Instrukce nastaveni vzdalenosti u vrcholu jako nekonecno
   */
  protected class SetVertexInfinityInstruction extends BasicInstruction {
    
    @Override
    public void preform() {
      editBlock.addEdit(new SetInfinityEdit((mxCell)activeVariable));
    }
  }
  
  /**
   * Instrukce nastaveni noveho stylu pro vsechny hrany u uzlu
   */
  protected class ColorActiveEdgesInstruction extends BasicInstruction {
    protected String newStyle;
    
    public ColorActiveEdgesInstruction(String newStyle) {
      this.newStyle = newStyle;
    }

    @Override
    public void preform() {
      for (int i = 0; i < graph.getModel().getEdgeCount(activeVariable); i++){
        mxCell edge = (mxCell) graph.getModel().getEdgeAt(activeVariable, i);
        editBlock.addEdit(new ChangeEdgeStyleEdit(edge, newStyle));
      }
    } 
  }

  /**
   * Instrukce pro nastaveni predchoziho uzlu v ceste
   */
  protected class SetPathInstruction extends BasicInstruction {
    
    protected Integer regNum = null;
    
    public SetPathInstruction(){}
    
    public SetPathInstruction(int regNum){
      this.regNum = regNum;
    }
    
    @Override
    public void preform() {
      if (regNum != null){
        editBlock.addEdit(new SetPathEdit((mxCell)activeVariable, (mxCell)getRegisterValue(regNum)));
      } else {
        editBlock.addEdit(new SetPathEdit((mxCell)activeVariable, null));
      }
    }
  }
  
  /**
   * Instrukce pro nastaveni vzdalenosti
   */
  protected class SetDistanceInstruction extends BasicInstruction {
    protected boolean zero = false;
    protected int regIndex;
    
    public SetDistanceInstruction(){
      this.zero = true;
    }
    
    public SetDistanceInstruction(int regIndex){
      this.zero = false;
      this.regIndex = regIndex;
    }
    
    @Override
    public void preform() {
      if (zero) {
        editBlock.addEdit(new SetDistanceEdit(((mxCell)activeVariable), 0));
      } else {
        int distance = getDistance(activeVariable, getRegisterValue(regIndex));
        editBlock.addEdit(new SetDistanceEdit(((mxCell)activeVariable), distance));
      }
    }
  }
  
  /**
   * Editacni akce pro nastaveni nekonecna do vrcholu
   */
  protected final class SetInfinityEdit extends AlgorithmUndoableEdit{
    protected BfsGraphCell graphNode;
    protected boolean infinity;
    
    public SetInfinityEdit(mxCell node) {
      this.graphNode = (BfsGraphCell) node.getValue();
      this.infinity = graphNode.isInfinity();
      action();
    }
    
    @Override
    public void undo() {
      super.undo();
      graphNode.setInfinity(infinity);
    }

    @Override
    protected void action() {
      graphNode.setInfinity(!infinity);
    }
  }
  
  /**
   * Editacni akce pro nastaveni predchoziho uzlu v ceste
   */
  protected final class SetPathEdit extends AlgorithmUndoableEdit{
    protected BfsGraphCell graphNode;
    protected BfsGraphCell newPath;
    protected BfsGraphCell oldPath;
    
    public SetPathEdit(mxCell node, mxCell newPath) {
      this.graphNode = (BfsGraphCell) node.getValue();
      this.oldPath = graphNode.getNodePath();
      this.newPath = (newPath != null) ? (BfsGraphCell) newPath.getValue() : null;
      action();
    }
    
    @Override
    public void undo() {
      super.undo();
      graphNode.setNodePath(oldPath);
    }

    @Override
    protected void action() {
      graphNode.setNodePath(newPath);
    }
    
  }
  
  /**
   * Editacni akce pro nastaveni vzdalenosti do uzlu
   */
  protected final class SetDistanceEdit extends AlgorithmUndoableEdit{
    protected BfsGraphCell graphNode;
    protected Integer newValue;
    protected Integer oldValue;
    
    public SetDistanceEdit(mxCell node, Integer newValue) {
      this.graphNode = (BfsGraphCell) node.getValue();
      this.newValue = newValue;
      this.oldValue = graphNode.getIntValue();
      action();
    }

    @Override
    public void undo() {
      super.undo();
      graphNode.setIntValue(oldValue);
    }

    @Override
    protected void action() {
      graphNode.setIntValue(newValue);
    }
  }
  
}
