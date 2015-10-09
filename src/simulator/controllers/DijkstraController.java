package simulator.controllers;

import com.mxgraph.model.mxCell;
import java.awt.Color;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.JLabel;
import simulator.graphs.AlgorithmGraphComponent;
import simulator.graphs.bellmanford.BellmanFordGraphVertex;
import simulator.graphs.bellmanford.BellmanFordGraphEdge;
import simulator.gui.VariablesPanel;
import simulator.gui.VisualizationPseudocodePanel;

/**
 * Ridici jednotka pro dijkstruv algoritmus
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class DijkstraController extends AbstractShortestPathController {
  
  protected Deque<mxCell> Q = new LinkedList<>();
  protected Deque<mxCell> S = new LinkedList<>();
  
  public DijkstraController(AlgorithmGraphComponent graphCom, JLabel graphComponentLabel, VisualizationPseudocodePanel completeCodePanel, VariablesPanel variablesPanel) {
    super(graphCom, graphComponentLabel, completeCodePanel, variablesPanel);
    resetControllerImpl();
    interactiveMode = false;
    
    Instruction iniSingleSource = new LabelInstruction();
    Instruction relax = new LabelInstruction();
    Instruction returnIniSingleSource = new LabelInstruction();
    Instruction returnRelax = new LabelInstruction();
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 1), algorithm.getRealLineNumber(0, 10), Color.GREEN));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(1, 1), algorithm.getRealLineNumber(1, 4), Color.GREEN));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(2, 1), algorithm.getRealLineNumber(2, 5), Color.GREEN));
    tape.add(new ShowOrHideVariableInstruction("Q", false));
    tape.add(new ShowOrHideVariableInstruction("S", false));
    tape.add(new ShowOrHideVariableInstruction("u", false));
    tape.add(new ShowOrHideVariableInstruction("v", false));
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", false));
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
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 2)));
    tape.add(new SetLabelTextInstruction("Inicializace množiny S dokončených uzlů"));
    tape.add(new PauseInstruction());
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 1), algorithm.getRealLineNumber(0, 2), doneColor));
    tape.add(new ClearQueueInstruction(S));
    tape.add(new ShowOrHideVariableInstruction("S", true));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 3)));
    tape.add(new SetLabelTextInstruction("Vložených všech uzlů do fronty Q"));
    tape.add(new PauseInstruction());
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 1), algorithm.getRealLineNumber(0, 3), doneColor));
    tape.add(new AddAllVertexListInstruction(Q));
    tape.add(new ShowOrHideVariableInstruction("Q", true));
    tape.add(new ShowOrHideVariableInstruction("u", true));
    Instruction whileLabel = new LabelInstruction();
    Instruction endWhile = new LabelInstruction(whileLabel, new Condition(){
      @Override
      public boolean isFulfil() {
        return (Q.isEmpty() == false);
      }  
    });
    
    tape.add(new LabelInstruction(endWhile, new Condition(){
      @Override
      public boolean isFulfil() {
        return Q.isEmpty();
      }  
    }));
    
    tape.add(whileLabel);
    tape.add(new SetLabelTextInstruction("Hlavní cyklus, dokud jsou uzly ke zpracování ve frontě Q"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 4)));
    tape.add(new PauseInstruction());
    tape.add(new SetLabelTextInstruction("Odebrání uzlu s nejnižší hodnotou vzdálenosti z fronty Q"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 5)));
    tape.add(new SelectNextNodeInstruction());
    tape.add(new PauseInstruction());
    tape.add(new SetLabelTextInstruction("Přidání uzlu do množiny S dokončených uzlů"));
    tape.add(new DequeueMinInstruction(Q));
    tape.add(new ActiveToRegisterInstruction(variableU));
    tape.add(new SetColorInstruction(stylesheet.getDoneVertexStyle()));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 6)));
    tape.add(new PauseInstruction());
    tape.add(new EnqueueInstruction(S));
    tape.add(new MakeAdjListInstruction(cellList, true));
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", true));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 7)));
    tape.add(new SetVerticesDistanceInstruction());
    Instruction forLabel = new LabelInstruction();
    Instruction forEndLabel1 = new LabelInstruction();
    tape.add(forLabel);
    tape.add(new SetLabelTextInstruction("Provedení relaxe všech hran vedoucích z aktuálního uzlu"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 7)));
    tape.add(new PauseInstruction());
    tape.add(new LabelInstruction(forEndLabel1, new Condition(){
      @Override
      public boolean isFulfil() {
        return (cellList.isEmpty() == true);
      }
    }));
    tape.add(new DequeueInstruction(cellList));
    tape.add(new ActiveToRegisterInstruction(variableV));
    tape.add(new ShowOrHideVariableInstruction("v", true));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 8)));
    tape.add(new PauseInstruction());
    tape.add(new EdgeToActiveInstruction(variableU, variableV));
    tape.add(new SetColorInstruction(stylesheet.getHighlighEdgeStyle()));
    tape.add(new LabelInstruction(relax, new Condition(){
      @Override
      public boolean isFulfil() {
        return true;
      }
    }));
    tape.add(returnRelax);
    tape.add(new ColorPathsInstruction(stylesheet.getPathEdgeStyle(), stylesheet.getEdgeStyle()));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 9)));
    tape.add(new PauseInstruction());
    tape.add(new LabelInstruction(forLabel, new Condition(){
      @Override
      public boolean isFulfil() {
        return (cellList.isEmpty() == false);
      }  
    }));
    tape.add(forEndLabel1);
    tape.add(new ShowOrHideVariableInstruction("v", false));
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", false));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 10)));
    tape.add(new PauseInstruction());
    tape.add(endWhile);
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 1), algorithm.getRealLineNumber(0, 10), doneColor));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(1, 1), algorithm.getRealLineNumber(1, 4), doneColor));
    tape.add(new EndInstruction());
    
    //relax
    tape.add(relax);
    tape.add(new SetLabelTextInstruction("Metoda relaxace zapíše novou vzdálenost do uzlu, pokud je menší než původní"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 0)));
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 1)));
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
    
    tape.add(iniSingleSource);
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 0)));
    tape.add(new PauseInstruction());
    tape.add(new AddAllVertexListInstruction(cellList));
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", true));
    Instruction forLabel2 = new LabelInstruction();
    tape.add(forLabel2);
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(2, 1)));
    tape.add(new PauseInstruction());
    tape.add(new DequeueInstruction(cellList));
    tape.add(new ActiveToRegisterInstruction(variableV));
    tape.add(new ShowOrHideVariableInstruction("v", true));
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
    tape.add(new LabelInstruction(forLabel2, new Condition(){
      @Override
      public boolean isFulfil() {
        return (cellList.isEmpty() == false);
      }  
    }));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(2, 1), algorithm.getRealLineNumber(2, 4), doneColor));
    tape.add(new ShowOrHideVariableInstruction("v", false));
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", false));
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
  }

  /**
   * Metoda uvede ridici jednotku do pocatecniho nastaveni
   */
  private void resetControllerImpl(){
    graphComponentLabel.setText("Dijkstrův algoritmus");
    codePanel.setCurrentLine(1);
    
    variables.put("Q", new VariableObject(){

      @Override
      public String getVariableValue() {
        return dequeToString(Q);
      }
      
    });
    variables.put("S", new VariableObject(){

      @Override
      public String getVariableValue() {
        return dequeToString(S);
      }
      
    });
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
    
    for (Map.Entry<String, VariableObject> e : variables.entrySet()){
      if (e.getValue().isEnabled()){
        variablesPanel.addVariable(e.getKey());
      } 
    }
    Q.clear();
    S.clear();
    cellList.clear();
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
   * Interaktivni instrukce pro uzivatelske nastaveni vzdalenosti pro vrchol
   */
  protected class SetVerticesDistanceInstruction extends InteractiveInstruction {
    protected Map<mxCell, Double> newDistanceMap = new HashMap<>();
    protected Map<mxCell, Double> oldDistanceMap = new HashMap<>();   
    
    @Override
    public void preform() {
      if (graph.isCellsEditable() == false){
        graphComponentLabel.setText(getTextForLabel("Zadejte do uzlů hodnotu vzdálenosti po provedení relaxace hrany\n[uživatelská akce]"));
        graph.setCellsEditable(true, true, false);
        timer.setEnabled(false);
        mxCell u = (mxCell) getRegisterValue(variableU);
        BellmanFordGraphVertex uValue = (BellmanFordGraphVertex) u.getValue();
        newDistanceMap.clear();
        oldDistanceMap.clear();
        for (mxCell cell : cellList){
          BellmanFordGraphEdge edge = (BellmanFordGraphEdge) ((mxCell) graph.getEdgesBetween(u, cell, true)[0]).getValue();
          BellmanFordGraphVertex cellValue = (BellmanFordGraphVertex) cell.getValue();
          oldDistanceMap.put(cell, cellValue.getDistance());
          double newDistance = cellValue.getDistance();
          if (cellValue.getDistance() > uValue.getDistance() + edge.getDistance()){
            newDistance = uValue.getDistance() + edge.getDistance();
          }
          newDistanceMap.put(cell, newDistance);
          cellValue.setEditable(true);
        }
      } else {
        boolean isRight = true;
        for (mxCell cell : cellList){
          BellmanFordGraphVertex cellValue = (BellmanFordGraphVertex) cell.getValue();
          if (isDoublesEquals(cellValue.getDistance(), newDistanceMap.get(cell)) == false){
            cellValue.setDistance(oldDistanceMap.get(cell));
            isRight = false;
          }
        }
        if (isRight){
          for (mxCell cell : cellList){
            BellmanFordGraphVertex cellValue = (BellmanFordGraphVertex) cell.getValue();
            cellValue.setDistance(oldDistanceMap.get(cell));
          }
          stop();
          graph.refresh();
          return;
        } else {
          graphComponentLabel.setText(getTextForLabel("Zadané hodnoty vzdáleností jsou chybné\n[uživatelská akce]"));
        }
      }  
      pause = true;
    }

    @Override
    public void stop() {
      if (graph.isCellsEditable() == true){
        for (mxCell cell : cellList){
          BellmanFordGraphVertex cellValue = (BellmanFordGraphVertex) cell.getValue();
          cellValue.setEditable(false);
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
   * Interaktivni instrukce pro uzivatelske zvoleni dalsiho vrcholu podle algoritmu
   */
  protected class SelectNextNodeInstruction extends InteractiveInstruction {
    protected SelectCellsMouseAdapter adapter;
    protected mxCell minNode = null;
    
    public SelectNextNodeInstruction() {
      adapter = new SelectCellsMouseAdapter(1, graphComponent, graph);
      adapter.setActivated(false);
      graphComponent.getGraphControl().addMouseListener(adapter);
    }
    
    @Override
    public void preform() {
      if (adapter.isActivated() == false){
        minNode = null;
        for (mxCell cell : Q){
          if (minNode == null || ((BellmanFordGraphVertex) cell.getValue()).getDistance() < 
              ((BellmanFordGraphVertex)minNode.getValue()).getDistance()){
            minNode = cell;
          }
        }
        graphComponentLabel.setText(getTextForLabel("Vyber uzel, který bude odebrán z fronty Q\n[uživatelská akce]"));
        timer.setEnabled(false);
        adapter.setActivated(true);
      } else if (adapter.getLastSelected() != null && 
                 S.contains(adapter.getLastSelected()) == false){
        BellmanFordGraphVertex selected = (BellmanFordGraphVertex) adapter.getLastSelected().getValue();
        if (isDoublesEquals(selected.getDistance(), ((BellmanFordGraphVertex) minNode.getValue()).getDistance())){
          selected.setSelected(true);
          stop(); 
          return;
        }
        graphComponentLabel.setText(getTextForLabel("Byl zvolen špatný uzel\n[uživatelská akce]"));
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
   * Instrukci pro odebrani uzlu z fronty podle vzdalenosti
   */
  protected class DequeueMinInstruction extends BasicInstruction {
    protected Deque<mxCell> q;
    
    public DequeueMinInstruction(Deque<mxCell> q) {
      this.q = q;
    }
    
    @Override
    public void preform() {
      editBlock.addEdit(new DequeueMinEdit(this.q));
    }
    
  }
  
  /**
   * Editacni akce pro odebrani uzlu z fronty podle vzdalenosti
   */
  protected final class DequeueMinEdit extends AlgorithmUndoableEdit{

    protected mxCell oldActive;
    protected mxCell NodeForRemove;
    protected Deque<mxCell> q;
    protected Deque<mxCell> oldQ = new LinkedList<>();
    
    public DequeueMinEdit(Deque<mxCell> q) {
      this.q = q;
      
      oldActive = (mxCell) activeVariable;
      oldQ.addAll(q);
      for (mxCell cell : q){
        if (NodeForRemove == null || ((BellmanFordGraphVertex) cell.getValue()).getDistance() <= 
            ((BellmanFordGraphVertex)NodeForRemove.getValue()).getDistance()){
          NodeForRemove = cell;
          if (((BellmanFordGraphVertex) cell.getValue()).isSelected()){
            break;
          }
        }
      }
      action();
    }
    
    @Override
    public void undo() {
      super.undo();
      q.clear();
      q.addAll(oldQ);
      activeVariable = oldActive;
    }

    @Override
    protected void action() {
      q.remove(NodeForRemove);
      activeVariable = NodeForRemove;
    }
  }
}
