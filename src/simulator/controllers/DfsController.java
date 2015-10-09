package simulator.controllers;

import com.mxgraph.model.mxCell;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Deque;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import simulator.graphs.AlgorithmGraph.AlgorithmGraphCellVisitor;
import simulator.graphs.AlgorithmGraphComponent;
import simulator.graphs.GraphCell;
import simulator.graphs.dfs.DfsGraphVertex;
import simulator.graphs.dfs.DfsGraphEdge;
import simulator.graphs.dfs.DfsGraphEdge.DfsEdgeType;
import simulator.gui.VariablesPanel;
import simulator.gui.VisualizationPseudocodePanel;

/**
 * Ridici jednotka pro algoritmus DFS
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class DfsController extends AbstractDfsController {
  
  public DfsController(final AlgorithmGraphComponent graphCom, final JLabel graphComponentLabel, 
                       final VisualizationPseudocodePanel completeCodePanel, 
                       final VariablesPanel variablesPanel) {
    
    super(graphCom, graphComponentLabel, completeCodePanel, variablesPanel);

    interactiveMode = false;
    resetControllerImpl();
    tape.add(new SetLabelTextInstruction("Inicializace všech uzlů"));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 1), algorithm.getRealLineNumber(0, 10), Color.GREEN));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(1, 1), algorithm.getRealLineNumber(1, 11), Color.GREEN));
    tape.add(new AddAllVertexListInstruction(vertexList));
    tape.add(new EnableEdgesTypeInstruction());
    Instruction forLabel = new LabelInstruction();
    tape.add(forLabel);
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 1)));
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 2)));
    tape.add(new DequeueInstruction(vertexList));
    tape.add(new ActiveToRegisterInstruction(variableU));
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 3)));
    tape.add(new SetColorInstruction(stylesheet.getInitVertexStyle()));
    tape.add(new SetVertexEnableInstruction());
    tape.add(new SetVertexToolTipsInstruction());
    tape.add(new SetPathInstruction());
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 4)));
    tape.add(new PauseInstruction());
    tape.add(new LabelInstruction(forLabel, new Condition(){
      @Override
      public boolean isFulfil() {
        return (vertexList.isEmpty() == false);
      }  
    }));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 5))); 
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 1), algorithm.getRealLineNumber(0, 4), doneColor));
    tape.add(new SetLabelTextInstruction("Inicializace času"));
    tape.add(new PauseInstruction());
    tape.add(new ShowOrHideVariableInstruction("time", true));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 5), algorithm.getRealLineNumber(0, 5), doneColor));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 6)));
    tape.add(new AddAllVertexListInstruction(vertexList));
    Instruction forLabel2 = new LabelInstruction();
    tape.add(forLabel2);
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 6)));
    tape.add(new SelectNextNodeInstruction());
    tape.add(new SetSelectedCellFirstInstruction(vertexList));
    tape.add(new SetLabelTextInstruction("Na všechny dosud nenavštívené uzly\nzavolej metodu DFS-VISIT"));
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 7)));
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
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 8)));
    tape.add(new SetLabelTextInstruction("Volání metody DFS-VISIT"));
    tape.add(new PauseInstruction());
    Instruction retDfsVisitLabel = new LabelInstruction();
    tape.add(new ContextInstruction(contextStack, retDfsVisitLabel));
    Instruction dfsVisitLabel = new LabelInstruction();
    tape.add(new LabelInstruction(dfsVisitLabel, new Condition(){
      @Override
      public boolean isFulfil() {
        return true;
      }
    }));
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", true));
    tape.add(ifEnd);
    tape.add(new PauseInstruction());
    tape.add(retDfsVisitLabel);
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 9)));
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 10)));
    tape.add(new PauseInstruction());
    tape.add(new LabelInstruction(forLabel2, new Condition(){
      @Override
      public boolean isFulfil() {
        return (vertexList.isEmpty() == false);
      }  
    }));
    tape.add(new ClearQueueInstruction(vertexList));
    //konec
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(0, 10)));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(0, 1), algorithm.getRealLineNumber(0, 10), doneColor));
    tape.add(new SetColorHighlighInstruction(algorithm.getRealLineNumber(1, 1), algorithm.getRealLineNumber(1, 11), doneColor));
    tape.add(new SetLabelTextInstruction("Algoritmus DFS dokončen"));
    tape.add(new EndInstruction());
    
    //Dfs-Visit
    tape.add(dfsVisitLabel);
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", false));
    tape.add(new ShowOrHideVariableInstruction("v", false));
    tape.add(new RegisterToActiveInstruction(variableU));
    tape.add(new PushInstruction(visitStack));
    tape.add(new SetLabelTextInstruction("Začátek DFS-VISIT"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 0)));
    tape.add(new PauseInstruction());
    tape.add(new SetLabelTextInstruction("První navštívení uzlu"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 1)));
    tape.add(new PauseInstruction());
    tape.add(new SetLabelTextInstruction("Inkrementace času"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 2)));
    tape.add(new SetColorInstruction(stylesheet.getFirstPassVertexStyle()));
    tape.add(new PauseInstruction());
    tape.add(new SetLabelTextInstruction("Nastavení první časové známky"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 3)));
    tape.add(new ChangeTimeInstruction());
    tape.add(new PauseInstruction());
    tape.add(new SetTimeInstruction(true));
    tape.add(new MakeAdjListInstruction(vertexList, graph.isOriented()));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 4)));
    tape.add(new MakeUserAdjListInstruction());
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", true));
    Instruction forLabel3 = new LabelInstruction();
    Instruction forEndLabel3 = new LabelInstruction();
    tape.add(forLabel3);
    tape.add(new SetLabelTextInstruction("Projdi všechny uzly, které sousedí s právě zpracovávaným"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 4)));
    tape.add(new PauseInstruction());
    tape.add(new LabelInstruction(forEndLabel3, new Condition(){
      @Override
      public boolean isFulfil() {
        return (vertexList.isEmpty() == true);
      }
    }));
    tape.add(new ShowOrHideVariableInstruction("v", true));
    tape.add(new SetLabelTextInstruction("Pokud sousední uzel dosud nebyl navštíven\nnastav mu cestu a zavolej pro něj DFS-VISIT"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 5))); 
    tape.add(new DequeueInstruction(vertexList));
    tape.add(new ActiveToRegisterInstruction(variableV));
    tape.add(new ColorEdgeActiveAndRegisterInstruction(variableU, stylesheet.getHighlighEdgeStyle(), graph.isOriented()));
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 6)));
    Instruction ifEnd2 = new LabelInstruction();
    tape.add(new LabelInstruction(ifEnd2, new Condition(){
      @Override
      public boolean isFulfil() {
        return (((mxCell)activeVariable).getStyle().equals(stylesheet.getInitVertexStyle()) == false);
      }
    }));
    tape.add(new SetLabelTextInstruction("Nastavení cesty"));
    tape.add(new ColorEdgeActiveAndRegisterInstruction(variableU, stylesheet.getPathEdgeStyle(), graph.isOriented()));
    tape.add(new PauseInstruction());
    tape.add(new SetLabelTextInstruction("Volání metody DFS-VISIT pro sousední uzel"));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 7)));
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
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", true));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 8)));
    tape.add(new SelectDfsEdgeTypeInstruction());
    tape.add(new SetDfsEdgeTypeInstruction(variableU, stylesheet.getPathEdgeStyle(), stylesheet.getEdgeStyle()));
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 9)));
    tape.add(new PauseInstruction());
    tape.add(new LabelInstruction(forLabel3, new Condition(){
      @Override
      public boolean isFulfil() {
        return (vertexList.isEmpty() == false);
      }
    }));
    tape.add(forEndLabel3);
    tape.add(new ClearQueueInstruction(vertexList));
    tape.add(new ShowOrHideVariableInstruction("v", false));
    tape.add(new ShowOrHideVariableInstruction("Iterační seznam", false));
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 10)));
    tape.add(new RegisterToActiveInstruction(variableU));
    tape.add(new SetLabelTextInstruction("Druhé navštívení uzlu"));
    tape.add(new PauseInstruction());
    tape.add(new SetCursorInstruction(algorithm.getRealLineNumber(1, 11)));
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
    graphComponentLabel.setText("Algoritmus DFS");
    codePanel.setCurrentLine(1);
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
    timeVariable = 0;
    visitStack.clear();
    contextStack.clear();
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
   * Interaktivni instrukce pro uzivatelske vytvoreni seznamu sousedu
   */
  protected class MakeUserAdjListInstruction extends InteractiveInstruction {
    protected SelectCellsMouseAdapter adapter;
    
    public MakeUserAdjListInstruction() {
      adapter = new SelectCellsMouseAdapter(graphComponent, graph);
      adapter.setActivated(false);
      graphComponent.getGraphControl().addMouseListener(adapter);
    }

    protected boolean isSelectedCorrect(Deque<mxCell> selected, Deque<mxCell> adjList){
      if (selected.size() != adjList.size()){
        return false;
      }
      for (mxCell cell : adjList){
        if (selected.contains(cell) == false){
          return false;
        }
      }
      return true;
    } 
    
    @Override
    public void preform() {
      graphComponentLabel.setText(getTextForLabel("Vytvoř seznam sousedů pro uzel u\n[uživatelská akce]"));
      if (adapter.isActivated() == false){
        timer.setEnabled(false);
        adapter.setActivated(true);
      } else if (isSelectedCorrect(adapter.getSelectedNodes(), vertexList) != false){
        vertexList.clear();
        vertexList.addAll(adapter.getSelectedNodes());
        stop();
        return;
      } else {
        graphComponentLabel.setText(getTextForLabel("Špatně vytvořený seznam pro uzel u\n[uživatelská akce]"));
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
   * Interaktivni instrukce pro zvoleni typu hrany uzivatelem
   */
  protected class SelectDfsEdgeTypeInstruction extends InteractiveInstruction {
    protected SimulatorMouseAdapter adapter;
    protected JPopupMenu popup = new JPopupMenu();
    protected DfsEdgeType selectedType = DfsEdgeType.NOTHING;
    protected mxCell edge = null;
    protected ButtonGroup group;
    
    public SelectDfsEdgeTypeInstruction() {
      JRadioButtonMenuItem forward = new JRadioButtonMenuItem(new AbstractAction("Forward"){
        @Override
        public void actionPerformed(ActionEvent e) {
          selectedType = DfsEdgeType.FORWARD;
        }
      });
      JRadioButtonMenuItem back = new JRadioButtonMenuItem(new AbstractAction("Back"){
        @Override
        public void actionPerformed(ActionEvent e) {
          selectedType = DfsEdgeType.BACK;
        }
      });
      JRadioButtonMenuItem cross = new JRadioButtonMenuItem(new AbstractAction("Cross"){
        @Override
        public void actionPerformed(ActionEvent e) {
          selectedType = DfsEdgeType.CROSS;
        }
      });
      group = new ButtonGroup();
      group.add(forward);
      group.add(back);
      group.add(cross);
      popup.add(forward);
      popup.add(back);
      popup.add(cross);
      adapter = new SimulatorMouseAdapter(){

        @Override
        public void mousePressed(MouseEvent e) {
          if (activated && e.isPopupTrigger()) {
            showMenu(e);
          }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
          if (activated && e.isPopupTrigger()) {
            showMenu(e);
          }
        }

        public void showMenu(MouseEvent e) {
          mxCell cell = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
          if (cell != null && cell.isEdge()){
            if (cell == edge){
              popup.show(e.getComponent(),e.getX(), e.getY());
            }
          }
        }
        
      };
      adapter.setActivated(false);
      graphComponent.getGraphControl().addMouseListener(adapter);
    }
    
    protected mxCell getEdge(){
      for (int i = 0; i < graph.getModel().getEdgeCount(activeVariable); i++){
        mxCell cell = (mxCell) graph.getModel().getEdgeAt(activeVariable, i);
        
        mxCell target = (mxCell) cell.getTarget();
        mxCell source = (mxCell) cell.getSource();
        DfsGraphVertex targetValue = (DfsGraphVertex) target.getValue();
        DfsGraphVertex sourceValue = (DfsGraphVertex) source.getValue();
        DfsGraphEdge edgeValue = (DfsGraphEdge) cell.getValue();
        if ((target == activeVariable && source == registers[variableU] ||
             graph.isOriented() == false && source == activeVariable && target == registers[variableU]) && 
             cell.getStyle().equals(stylesheet.getHighlighEdgeStyle())){
          if (sourceValue.getNodePath() != null && sourceValue.getNodePath().getName().equals(targetValue.getName()) == true || 
              targetValue.getNodePath() != null && sourceValue.getName().equals(targetValue.getNodePath().getName()) == true){
            return null;
          }
          if (edgeValue.getEdgeType() != DfsEdgeType.NOTHING){
            return null;
          }
          return cell;
        }       
      }
      return null;
    }
    
    @Override
    public void preform() {
      if (adapter.isActivated() == false){
        this.edge = getEdge();
        if (this.edge != null){
          graphComponentLabel.setText(getTextForLabel("Vyber typ aktivní hrany\n[uživatelská akce, vyskakovací menu u hrany]"));
          timer.setEnabled(false);
          adapter.setActivated(true);
        }
      } else if (edge != null && DfsEdgeType.getType(edge, stylesheet.getPathEdgeStyle()) == selectedType){
        graphComponentLabel.setText(getTextForLabel("Typ hrany určen správně\n[uživatelská akce]"));
        graph.setSelectionCell(null);    
        stop(); 
        return;
      } else {
        graphComponentLabel.setText(getTextForLabel("Špatně vybraný typ aktivní hrany\n[uživatelská akce]"));
      }     
      pause = true;
    }

    @Override
    public void stop() {
      if (adapter.isActivated()){
        selectedType = DfsEdgeType.NOTHING;
        group.clearSelection();
        edge = null;        
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

}
