package simulator.controllers;

import com.mxgraph.model.mxCell;
import java.awt.Color;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map.Entry;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import simulator.graphs.AlgorithmGraph.AlgorithmGraphCellVisitor;
import simulator.graphs.AlgorithmGraphComponent;
import simulator.graphs.GraphCell;
import simulator.graphs.bellmanford.BellmanFordGraphVertex;
import simulator.graphs.bellmanford.BellmanFordGraphEdge;
import simulator.gui.VariablesPanel;
import simulator.gui.VisualizationPseudocodePanel;

/**
 * Ridici jednotka pro Bellman-Forduv algoritmus
 * Jsou zde prevazne interaktivni instrukce a konrketni reseni pro Bellman-Forda.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class BellmanFordController extends AbstractShortestPathController {
  
  /**
   * Stav pri pruchodu: prvni pruchod a ostatni pruchody
   */
  public enum BellmanFordState{
    FIRST_RUN,
    RUN,
  }
          
  protected Deque<mxCell> userEdgeList = new LinkedList<>();
  
  protected Integer variableI = 3; 
  protected Integer variableN = 4; 
  
  protected BellmanFordState state;
  protected boolean wasChange;
  
  /**
   * Inicializace, napleni pasky instrukci
   * @param graphCom
   * @param graphComponentLabel
   * @param completeCodePanel
   * @param variablesPanel 
   */
  public BellmanFordController(AlgorithmGraphComponent graphCom, JLabel graphComponentLabel, 
                               VisualizationPseudocodePanel completeCodePanel, 
                               VariablesPanel variablesPanel) {
    super(graphCom, graphComponentLabel, completeCodePanel, variablesPanel);
    resetControllerImpl();
    interactiveMode = false;
    Instruction iniSingleSource = new LabelInstruction();
    Instruction relax = new LabelInstruction();
    Instruction returnIniSingleSource = new LabelInstruction();
    Instruction returnRelax = new LabelInstruction();
    Instruction checkLabel = new LabelInstruction();
    Instruction algEndLabel = new LabelInstruction();
    tape.add(new ShowOrHideVariableInstruction("u", false));
    tape.add(new ShowOrHideVariableInstruction("v", false));
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", false));
    tape.add(new ShowOrHideVariableInstruction("i", false));
    tape.add(new ShowOrHideVariableInstruction("n", false));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 1), algorithm.getRealLineNumber(0, 12), Color.GREEN));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(1, 1), algorithm.getRealLineNumber(1, 4), Color.GREEN));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(2, 1), algorithm.getRealLineNumber(2, 5), Color.GREEN));
    tape.add(new SelectStartNodeInstruction());
    tape.add(new InitInstruction(stylesheet.getSelectedVertexStyle()));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 1)));
    tape.add(new SetLabelTextInstruction("Inicializace všech uzlů"));
    tape.add(new PauseInstruction());
    tape.add(new LabelInstruction(iniSingleSource, new Condition(){
      @Override
      public boolean isFulfil() {
        return true;
      }
    }));
    tape.add(returnIniSingleSource);
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 1), algorithm.getRealLineNumber(0, 1), doneColor));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(2, 1), algorithm.getRealLineNumber(2, 5), doneColor));
    tape.add(new SetIntRegisterInstruction(variableI,0));
    tape.add(new SetIntRegisterInstruction(variableN,graph.getChildVertices(graph.getDefaultParent()).length));
    tape.add(new ShowOrHideVariableInstruction("i", true));
    tape.add(new ShowOrHideVariableInstruction("n", true));
    Instruction forLabel = new LabelInstruction();
    tape.add(forLabel);
    tape.add(new SetLabelTextInstruction("Hlavní cyklus dle počtu uzlů v grafu"));
    tape.add(new SetChangedInstruction(false));
    tape.add(new IncIntRegisterInstruction(variableI));
    tape.add(new ClearQueueInstruction(cellList));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 2)));
    tape.add(new PauseInstruction());
    tape.add(new AddAllEdgesListInstruction(cellList));
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", true));
    Instruction forLabel2 = new LabelInstruction();
    Instruction forEndLabel2 = new LabelInstruction();
    tape.add(forLabel2);
    tape.add(new SetLabelTextInstruction("Cyklus pro relaxaci všech hran"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 3)));
    tape.add(new PauseInstruction());
    tape.add(new LabelInstruction(forEndLabel2, new Condition(){
      @Override
      public boolean isFulfil() {
        return (cellList.isEmpty() == true);
      }
    }));  
    tape.add(new SelectNextEdgeInstruction(cellList));
    tape.add(new OrderCellListInstruction(cellList, userEdgeList));
    tape.add(new DequeueInstruction(cellList));
    tape.add(new EnqueueBellmanFordInstruction(userEdgeList));
    tape.add(new EdgeToRegistersInstruction(variableU, variableV));
    tape.add(new ShowOrHideVariableInstruction("u", true));
    tape.add(new ShowOrHideVariableInstruction("v", true));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 4)));
    tape.add(new PauseInstruction());
    tape.add(new SetColorInstruction(stylesheet.getHighlighEdgeStyle()));
    tape.add(new LabelInstruction(relax, new Condition(){
      @Override
      public boolean isFulfil() {
        return true;
      }
    }));
    tape.add(returnRelax);
    tape.add(new ColorPathsInstruction(stylesheet.getPathEdgeStyle(), 
                                       stylesheet.getEdgeStyle()));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 5)));
    tape.add(new PauseInstruction());
    tape.add(new LabelInstruction(forLabel2, new Condition(){
      @Override
      public boolean isFulfil() {
        return (cellList.isEmpty() == false);
      }  
    }));
    tape.add(forEndLabel2);
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", false));
    tape.add(new ShowOrHideVariableInstruction("u", false));
    tape.add(new ShowOrHideVariableInstruction("v", false));
    tape.add(new SetBellmanFordAlgStateInstruction(BellmanFordState.RUN));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 6)));
    tape.add(new PauseInstruction());
    tape.add(new LabelInstruction(checkLabel, new Condition(){
      @Override
      public boolean isFulfil() {
        int i = (int) getRegisterValue(variableI);
        int n = (int) getRegisterValue(variableN) - 1;
        if (!wasChange && n > i){
          Object[] options = {"Ano", "Ne"};
          int ret = JOptionPane.showOptionDialog(graphComponent,
          "V tomto kroku nedošlo k žadným změnám, chcete přeskočit další kroky?",
          "Sledování změn",
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE,
          null,
          options,
          options[0]);
          if (ret == 0){
            return true;
          }
        }
        return false;
      }
    }));
    tape.add(new LabelInstruction(forLabel, new Condition(){
      @Override
      public boolean isFulfil() {
        int i = (int) getRegisterValue(variableI);
        int n = (int) getRegisterValue(variableN) - 1;
        return (n > i);
      }  
    }));
    tape.add(checkLabel);
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 1), algorithm.getRealLineNumber(0, 6), doneColor));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(1, 1), algorithm.getRealLineNumber(1, 4), doneColor));
    tape.add(new ShowOrHideVariableInstruction("u", false));
    tape.add(new ShowOrHideVariableInstruction("v", false));
    tape.add(new ShowOrHideVariableInstruction("i", false));
    tape.add(new ShowOrHideVariableInstruction("n", false));
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", true));
    tape.add(new AddAllEdgesListInstruction(cellList));
    tape.add(new OrderCellListInstruction(cellList, userEdgeList));
    Instruction forLabel3 = new LabelInstruction();
    Instruction forEndLabel3 = new LabelInstruction();
    tape.add(forLabel3);
    tape.add(new SetLabelTextInstruction("Ověření zda se v grafu nevyskytuje záporný cyklus"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 7)));
    tape.add(new PauseInstruction());
    tape.add(new LabelInstruction(forEndLabel3, new Condition(){
      @Override
      public boolean isFulfil() {
        return (cellList.isEmpty() == true);
      }
    }));  
    tape.add(new DequeueInstruction(cellList));
    tape.add(new EdgeToRegistersInstruction(variableU, variableV));
    tape.add(new ShowOrHideVariableInstruction("u", true));
    tape.add(new ShowOrHideVariableInstruction("v", true));
    tape.add(new SetColorInstruction(stylesheet.getHighlighEdgeStyle()));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 8)));
    tape.add(new PauseInstruction());
    Instruction ifEnd = new LabelInstruction();
    tape.add(new LabelInstruction(ifEnd, new Condition(){
      @Override
      public boolean isFulfil() {
        BellmanFordGraphVertex u = (BellmanFordGraphVertex) ((mxCell) getRegisterValue(variableU)).getValue();
        BellmanFordGraphVertex v = (BellmanFordGraphVertex) ((mxCell) getRegisterValue(variableV)).getValue();
        BellmanFordGraphEdge edge = (BellmanFordGraphEdge) ((mxCell) activeVariable).getValue();
        return (v.getDistance() <= u.getDistance() + edge.getDistance());
      }
    }));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 1), algorithm.getRealLineNumber(0, 8), doneColor));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 10), algorithm.getRealLineNumber(0, 12), doneColor));
    tape.add(new SetLabelTextInstruction("V grafu byl nalezen záporný cyklus"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 9)));
    tape.add(new SetColorInstruction(stylesheet.getBreakConEdgeStyle()));
    tape.add(new PauseInstruction());
    tape.add(new LabelInstruction(algEndLabel, new Condition(){
      @Override
      public boolean isFulfil() {
        return (true);
      }  
    }));
    tape.add(ifEnd);
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 10)));
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 11)));
    tape.add(new PauseInstruction());
    tape.add(new ColorPathsInstruction(stylesheet.getPathEdgeStyle(), 
                                       stylesheet.getEdgeStyle()));
    tape.add(new LabelInstruction(forLabel3, new Condition(){
      @Override
      public boolean isFulfil() {
        return (cellList.isEmpty() == false);
      }  
    }));
    tape.add(forEndLabel3);
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 1), algorithm.getRealLineNumber(0, 12), doneColor));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 12)));
    tape.add(new SetLabelTextInstruction("V grafu se nevyskytuje záporný cyklus"));
    tape.add(new PauseInstruction());
    tape.add(algEndLabel);
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 1), algorithm.getRealLineNumber(0, 12), doneColor));
    tape.add(new EndInstruction());
    
    tape.add(iniSingleSource);
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 0)));
    tape.add(new PauseInstruction());
    tape.add(new ShowOrHideVariableInstruction("v", true));
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", true));
    tape.add(new AddAllVertexListInstruction(cellList));
    Instruction forLabel4 = new LabelInstruction();
    tape.add(forLabel4);
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 1)));
    tape.add(new PauseInstruction());
    tape.add(new DequeueInstruction(cellList));
    tape.add(new ActiveToRegisterInstruction(variableV));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 2)));
    tape.add(new PauseInstruction());
    tape.add(new SetVertexEnableInstruction());
    tape.add(new SetDistanceInstruction(Double.POSITIVE_INFINITY));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 3)));
    tape.add(new PauseInstruction());
    tape.add(new SetVertexToolTipsInstruction());
    tape.add(new SetPathInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 4)));
    tape.add(new PauseInstruction());
    tape.add(new LabelInstruction(forLabel4, new Condition(){
      @Override
      public boolean isFulfil() {
        return (cellList.isEmpty() == false);
      }  
    }));
    tape.add(new ShowOrHideVariableInstruction("v", false));
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", false));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(2, 1), algorithm.getRealLineNumber(2, 4), doneColor));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 5)));
    tape.add(new PauseInstruction());
    tape.add(new RegisterToActiveInstruction(variableS));
    tape.add(new SetDistanceInstruction(0.0));
    tape.add(new SetColorInstruction(stylesheet.getVertexStyle()));
    tape.add(new LabelInstruction(returnIniSingleSource, new Condition(){
      @Override
      public boolean isFulfil() {
        return true;
      }
    }));
    
    tape.add(relax);
    tape.add(new SetLabelTextInstruction("Metoda relaxace zapíše novou vzdálenost do uzlu, pokud je menší než původní"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 0)));
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 1)));
    tape.add(new SetVertexDistanceInstruction());
    tape.add(new PauseInstruction());
    Instruction ifEnd2 = new LabelInstruction();
    tape.add(new LabelInstruction(ifEnd2, new Condition(){
      @Override
      public boolean isFulfil() {
        BellmanFordGraphVertex u = (BellmanFordGraphVertex) ((mxCell) getRegisterValue(variableU)).getValue();
        BellmanFordGraphVertex v = (BellmanFordGraphVertex) ((mxCell) getRegisterValue(variableV)).getValue();
        BellmanFordGraphEdge edge = (BellmanFordGraphEdge) ((mxCell) activeVariable).getValue();
        return (v.getDistance() <= u.getDistance() + edge.getDistance());
      }
    }));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 2)));
    tape.add(new SetChangedInstruction(true));
    tape.add(new PauseInstruction());
    tape.add(new SetDistanceInstruction(variableV, variableU));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 3)));
    tape.add(new PauseInstruction());
    tape.add(new SetPathInstruction(variableV, variableU));
    tape.add(ifEnd2);
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 4)));
    tape.add(new PauseInstruction());
    tape.add(new LabelInstruction(returnRelax, new Condition(){
      @Override
      public boolean isFulfil() {
        return true;
      }
    }));
  }
  
  /**
   * Metoda uvede ridici jednotku do pocatecniho nastaveni
   */
  private void resetControllerImpl(){
    graphComponentLabel.setText("Algoritmus Bellman-Ford");
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
    variables.put("Iterační seznam", new VariableObject(){

      @Override
      public String getVariableValue() {
        return dequeToString(cellList);
      }
      
    });
    variables.put("i", new VariableObject(){

      @Override
      public String getVariableValue() {
        return (getRegisterValue(variableI) != null) ? getRegisterValue(variableI).toString() : "";
      }
      
    });
    variables.put("n", new VariableObject(){

      @Override
      public String getVariableValue() {
        return (getRegisterValue(variableI) != null) ? getRegisterValue(variableN).toString() : "";
      }
      
    });
    
    for (Entry<String, VariableObject> e : variables.entrySet()){
      if (e.getValue().isEnabled()){
        variablesPanel.addVariable(e.getKey());
      } 
    }
    cellList.clear();
    userEdgeList.clear();
    state = BellmanFordState.FIRST_RUN;
    wasChange = false;
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
   * Interaktivni instrukce pro nastaveni vzdalenosti uzivatelem.
   * Necha uzivatele nastavit vzdalenosti a pote zkontroluje jejich spravnost
   */
  protected class SetVertexDistanceInstruction extends InteractiveInstruction {
    protected Double newDistance;
    protected Double oldDistance;
    protected BellmanFordGraphVertex v;
    
    @Override
    public void preform() {
      if (graph.isCellsEditable() == false){
        graphComponentLabel.setText(getTextForLabel("Zadejte do uzlu hodnotu vzdálenosti po provedení relaxace hrany\n[uživatelská akce]"));
        graph.setCellsEditable(true, true, false);
        timer.setEnabled(false);
        BellmanFordGraphVertex u = (BellmanFordGraphVertex) ((mxCell) getRegisterValue(variableU)).getValue();
        v = (BellmanFordGraphVertex) ((mxCell) getRegisterValue(variableV)).getValue();
        BellmanFordGraphEdge edge = (BellmanFordGraphEdge) ((mxCell) activeVariable).getValue();
        v.setEditable(true);
        oldDistance = v.getDistance();
        if (v.getDistance() > u.getDistance() + edge.getDistance()){
          newDistance = u.getDistance() + edge.getDistance();
        } else {
          newDistance = v.getDistance();
        }
      } else {
        v = (BellmanFordGraphVertex) ((mxCell) getRegisterValue(variableV)).getValue();
        //System.out.println(v.getDistance() + " : " + newDistance);
        if (isDoublesEquals(newDistance, v.getDistance())){
          v.setDistance(oldDistance);
          stop();
          return;
        }
        v.setDistance(oldDistance);
        graph.refresh();
        graphComponentLabel.setText(getTextForLabel("Zadaná hodnota je chybná\n[uživatelská akce]"));
      }
      pause = true;
    }

    @Override
    public void stop() {
      if (graph.isCellsEditable() == true){
        if (v != null){
          v.setEditable(false);
        }
        graph.setCellsEditable(false);
        timer.setEnabled(true); 
      }
    }
    
    @Override
    public int nextTapePos() {
      if (graph.isCellsEditable() == true){
        return tapePosition;
      }
      return super.nextTapePos();
    }
  }
  
  /**
   * Interaktivni instrukce pro vybrani dalsi hrany k relaxaci uzivatelem
   */
  protected class SelectNextEdgeInstruction extends InteractiveInstruction {

    protected SelectCellsMouseAdapter adapter;
    protected Deque<mxCell> edgeList;
    
    public SelectNextEdgeInstruction(Deque<mxCell> edgeList) {
      this.edgeList = edgeList;
      adapter = new SelectCellsMouseAdapter(1, graphComponent, graph, false, true);
      adapter.setActivated(false);
      graphComponent.getGraphControl().addMouseListener(adapter);
    }

    @Override
    public void preform() {
      if (state != BellmanFordState.FIRST_RUN){
        return;
      }
      if (adapter.isActivated() == false){
        graphComponentLabel.setText(getTextForLabel("Vyber další hranu pro relaxaci\n[uživatelská akce]"));
        timer.setEnabled(false);
        adapter.setActivated(true);
      } else if (adapter.getLastSelected() != null){
        mxCell selected = adapter.getLastSelected();
        if (edgeList.contains(selected)){
          graph.traverseAllCells(new AlgorithmGraphCellVisitor(){
            @Override
            public boolean visit(mxCell vertex, GraphCell node) {
              if (vertex.isEdge()){
                if (vertex == adapter.getLastSelected()){
                  node.setSelected(true);
                } else {
                  node.setSelected(false);
                }
              }
              return true;
            }   

            @Override
            public boolean allowEdge() {
              return true;
            }
          });
          stop(); 
          return;
        } else {
          graphComponentLabel.setText(getTextForLabel("Hrana byla již relaxována\n[uživatelská akce]"));
        }
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
   * Instrukce pro usporadani seznamu hran
   */
  protected class OrderCellListInstruction extends BasicInstruction {
    protected Deque<mxCell> list;
    protected Deque<mxCell> orderPosition;
    
    public OrderCellListInstruction(Deque<mxCell> list, Deque<mxCell> orderPosition) {
      this.list = list;
      this.orderPosition = orderPosition;
    }

    @Override
    public void preform() {
      if (state == BellmanFordState.FIRST_RUN){ 
        new SetSelectedCellFirstInstruction(list).preform();
      } else {
        editBlock.addEdit(new OrderCellListEdit(list, orderPosition));
      }
    }
  }
  
  /**
   * Editacni akce pro usporadani seznamu hran
   */
  protected final class OrderCellListEdit extends AlgorithmUndoableEdit{
    protected Deque<mxCell> list;
    protected Deque<mxCell> oldList;
    protected Deque<mxCell> orderPosition;
    
    public OrderCellListEdit(Deque<mxCell> list, Deque<mxCell> orderPosition) {
      this.list = list;
      this.orderPosition = orderPosition;
      oldList = new LinkedList<>();
      oldList.addAll(list);
      action();
    }
    
    @Override
    public void undo() {
      super.undo();
      list.clear();
      list.addAll(oldList);
    }

    @Override
    protected void action() {
      Deque<mxCell> tmp = new LinkedList<>();
      tmp.addAll(orderPosition);
      tmp.retainAll(list);
      list.clear();
      list.addAll(tmp);
    }
    
  } 
  
  /**
   * Instrukce pro vlozeni do fronty hran pokud je prvni beh
   */
  protected class EnqueueBellmanFordInstruction extends BasicInstruction {
    protected Deque<mxCell> q;
    
    public EnqueueBellmanFordInstruction(Deque<mxCell> q) {
      this.q = q;
    }
    
    @Override
    public void preform() {
      if (state == BellmanFordState.FIRST_RUN){ 
        editBlock.addEdit(new EnqueueEdit(this.q, true));
      }
    }
    
  }
  
  /**
   * Instrukce pro nastaveni typu pruchodu grafem
   */
  protected class SetBellmanFordAlgStateInstruction extends BasicInstruction {
    protected BellmanFordState newState;

    public SetBellmanFordAlgStateInstruction(BellmanFordState newState) {
      this.newState = newState;
    }

    @Override
    public void preform() {
      editBlock.addEdit(new SetBellmanFordAlgStateEdit(newState));
    }
  }
  
  /**
   * Editacni akce pro nastaveni typu pruchodu grafem
   */
  protected final class SetBellmanFordAlgStateEdit extends AlgorithmUndoableEdit{
    protected BellmanFordState newState;
    protected BellmanFordState oldState;
    
    public SetBellmanFordAlgStateEdit(BellmanFordState newState) {
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
  
  /**
   * Instrukce zapise zda doslo ke zmenam
   */
  protected class SetChangedInstruction extends BasicInstruction {
    protected boolean changed;

    public SetChangedInstruction(boolean changed) {
      this.changed = changed;
    }

    @Override
    public void preform() {
      editBlock.addEdit(new SetChangedEdit(changed));
    }
  }
  
  /**
   * Editacni akce pro kontrolu zda doslo ke zmenam
   */
  protected final class SetChangedEdit extends AlgorithmUndoableEdit{
    protected boolean oldChanged;
    protected boolean changed;
    
    public SetChangedEdit(boolean changed) {
      this.changed = changed;
      oldChanged = wasChange;
      action();
    }
    
    @Override
    public void undo() {
      super.undo();
      wasChange = oldChanged;
    }
    
    @Override
    protected void action() {
      wasChange = changed;
    }
  }
}
