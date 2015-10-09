package simulator.controllers;

import com.mxgraph.model.mxCell;
import java.util.Deque;
import java.util.LinkedList;
import javax.swing.JLabel;
import simulator.graphs.AlgorithmGraph;
import simulator.graphs.AlgorithmGraphComponent;
import simulator.graphs.GraphCell;
import simulator.graphs.dfs.DfsGraphEdge;
import simulator.graphs.dfs.DfsGraphEdge.DfsEdgeType;
import simulator.graphs.dfs.DfsGraphVertex;
import simulator.gui.VariablesPanel;
import simulator.gui.VisualizationPseudocodePanel;

/**
 * Tato ridici jednotka slouzi pro algoritmy na zakladu DFS.
 * Poskytuje zakladni instrukce specialne pro DFS.
 * Dovolujer take ukladani kontextu
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class AbstractDfsController extends AbstractController {
  
  /**
   * Rozhrani pro ulozeni kontekxtu
   */
  protected interface ContextObject {
    public void restoreContext();
    public Instruction getNextIns();
  }
  
  /**
   * Trida pro objekty ukladajici kontext
   */
  protected class DfsContextObject implements ContextObject{
    protected Deque<mxCell> vList = new LinkedList<>();
    protected mxCell vU;
    protected mxCell vV;
    protected mxCell activeV;
    protected Instruction nextIns;
    protected int tapeIndex;
    
    public DfsContextObject(Instruction next) { 
      this.nextIns = next;
      vU = (mxCell) getRegisterValue(variableU);
      vV = (mxCell) getRegisterValue(variableV);
      activeV = (mxCell) activeVariable;
      vList.addAll(vertexList);
      tapeIndex = tapePosition; 
    }
    
    @Override
    public void restoreContext() {
      setRegisterValue(vU, variableU);
      setRegisterValue(vV, variableV);
      activeVariable = activeV;
      vertexList.clear();
      vertexList.addAll(vList);
      if (nextIns != null){
        new SetTapePositionInstruction(tape.indexOf(nextIns)).preform();
      } else {
        tapePosition = tapeIndex;
      }
    }

    @Override
    public Instruction getNextIns() {
      return nextIns;
    }
    
  } 
  
  protected int timeVariable = 0;
  protected Integer variableV = 0;
  protected Integer variableU = 1; 
  protected Deque<mxCell> vertexList = new LinkedList<>();
  protected Deque<mxCell> visitStack = new LinkedList<>();
  protected Deque<ContextObject> contextStack = new LinkedList<>();
  
  /**
   * Inicializace
   * @param graphCom
   * @param graphComponentLabel
   * @param completeCodePanel
   * @param variablesPanel 
   */
  public AbstractDfsController(AlgorithmGraphComponent graphCom, JLabel graphComponentLabel, 
                               VisualizationPseudocodePanel completeCodePanel, VariablesPanel variablesPanel) {
    super(graphCom, graphComponentLabel, completeCodePanel, variablesPanel);
  }
  
  /**
   * Metoda vytvori novy objekt ve kterem je ulozeny aktualni kontext simulatoru
   * @param next
   * @return 
   */
  protected ContextObject getContext(Instruction next){
    return new DfsContextObject(next);
  }

  /**
   * Instrukce pro povoleni zobrazeni typu hrany
   */
  protected class EnableEdgesTypeInstruction extends BasicInstruction{

    @Override
    public void preform() {
      graph.traverseAllCells(new AlgorithmGraph.AlgorithmGraphCellVisitor(){
        @Override
        public boolean visit(mxCell cell, GraphCell node) {
          if (cell.isEdge()){
            ((DfsGraphEdge) cell.getValue()).setEnabled(true);
          }
          return true;
        }

        @Override
        public boolean allowEdge() {
          return true;
        }
      });
    }
  }
  
  /**
   * Instrukce pro ulozeni nebo obnovu kontextu
   */
  protected class ContextInstruction extends BasicInstruction {
    protected Deque<ContextObject> stack;
    protected Instruction next;
    
    public ContextInstruction(Deque<ContextObject> stack){
      this(stack, null);
    }
    
    public ContextInstruction(Deque<ContextObject> stack, Instruction next) {
      this.stack = stack;
      this.next = next;
    }
 
    @Override
    public void preform() {
      if (next != null){
        editBlock.addEdit(new ContextEditSave(stack, next));
      } else {
        editBlock.addEdit(new ContextEditRestore(stack));
      }
    }
    
  }
  
  /**
   * Editacni akce pro obnovu kontextu
   */
  protected final class ContextEditRestore extends AlgorithmUndoableEdit {
    protected Deque<ContextObject> stack;
    protected ContextObject context;
    //protected ContextObject actualContext;
    
    public ContextEditRestore(Deque<ContextObject> stack) {
      this.stack = stack;
      this.context = stack.getFirst();
      context.restoreContext();
      stack.remove(context);
      //this.actualContext = getContext(this.context.getNextIns());
    }
     
    @Override
    public void undo() {
      super.undo();
      stack.push(context);
      //int tmp = tapePosition;
      //actualContext.restoreContext();
      //tapePosition = tmp;
    }
    
    @Override
    protected void action() {
      int tmp = tapePosition;
      context.restoreContext();
      tapePosition = tmp;
      stack.remove(context);
    }
  }
  
  /**
   * Editacni akce pro ulozeni kontextu
   */
  protected final class ContextEditSave extends AlgorithmUndoableEdit {
    protected Deque<ContextObject> stack;
    protected ContextObject context;
    protected ContextObject actualContext;
    
    public ContextEditSave(Deque<ContextObject> stack, Instruction next) {
      this.stack = stack;
      context = getContext(next);
      this.actualContext = getContext(null);
      action();
    }
     
    @Override
    public void undo() {
      super.undo();
      stack.remove(context);
      int tmp = tapePosition;
      actualContext.restoreContext();
      tapePosition = tmp;
    }
    
    @Override
    protected void action() {
      stack.push(context);
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
   * Editacni akce pro nastaveni predchoziho uzlu v ceste
   */
  protected final class SetPathEdit extends AlgorithmUndoableEdit{
    protected DfsGraphVertex graphNode;
    protected DfsGraphVertex newPath;
    protected DfsGraphVertex oldPath;
    
    public SetPathEdit(mxCell node, mxCell newPath) {
      this.graphNode = (DfsGraphVertex) node.getValue();
      this.oldPath = graphNode.getNodePath();
      this.newPath = (newPath != null) ? (DfsGraphVertex) newPath.getValue() : null;
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
   * Instrukce pro nastaveni typu hrany
   */
  protected class SetDfsEdgeTypeInstruction extends BasicInstruction {
    protected int registerNum;
    protected String pathEdgeStyle;
    protected String edgeStyle;
    
    public SetDfsEdgeTypeInstruction(int registerNum, String pathEdgeStyle, String edgeStyle) {
      this.registerNum = registerNum;
      this.pathEdgeStyle = pathEdgeStyle;
      this.edgeStyle = edgeStyle;
    }

    @Override
    public void preform() {
      for (int i = 0; i < graph.getModel().getEdgeCount(activeVariable); i++){
        mxCell edge = (mxCell) graph.getModel().getEdgeAt(activeVariable, i);
        mxCell target = (mxCell) edge.getTarget();
        mxCell source = (mxCell) edge.getSource();
        DfsGraphVertex targetValue = (DfsGraphVertex) target.getValue();
        DfsGraphVertex sourceValue = (DfsGraphVertex) source.getValue();
        DfsGraphEdge edgeValue = (DfsGraphEdge) edge.getValue();
        if (edgeValue.getEdgeType() == DfsEdgeType.NOTHING){
          if ((target == activeVariable && source == registers[registerNum]) || 
              (graph.isOriented() == false && source == activeVariable && target == registers[registerNum])){
            if (graph.isOriented() == false && (sourceValue.getNodePath() != null && sourceValue.getNodePath().getName().equals(targetValue.getName()) == true || 
                targetValue.getNodePath() != null && sourceValue.getName().equals(targetValue.getNodePath().getName()) == true)){
              editBlock.addEdit(new ChangeEdgeStyleEdit(edge, pathEdgeStyle));
            } else {
              DfsEdgeType type = DfsEdgeType.getType(edge, pathEdgeStyle);
              editBlock.addEdit(new SetDfsEdgeTypeEdit(edgeValue, type));
              if (type != DfsEdgeType.NOTHING){
                editBlock.addEdit(new ChangeEdgeStyleEdit(edge, edgeStyle));
              }
            }
          }
        } else {
          editBlock.addEdit(new ChangeEdgeStyleEdit(edge, edgeStyle));
        }        
      }
    }
  }
  
  /**
   * Editacni akce pro nastaveni typu hrany
   */
  protected final class SetDfsEdgeTypeEdit extends AlgorithmUndoableEdit{
    protected DfsGraphEdge edge;
    protected DfsGraphEdge.DfsEdgeType oldType;
    protected DfsGraphEdge.DfsEdgeType newType;

    public SetDfsEdgeTypeEdit(DfsGraphEdge edge, DfsGraphEdge.DfsEdgeType newType) {
      this.edge = edge;
      this.newType = newType;
      this.oldType = edge.getEdgeType();
      action();
    }
    
    @Override
    public void undo() {
      super.undo();
      edge.setEdgeType(oldType);
    }

    @Override
    protected void action() {
      edge.setEdgeType(newType);
    }
    
  } 
   
  /**
   * Instrukce pro nastaveni casu
   */
  protected class SetTimeInstruction extends BasicInstruction {

    protected boolean first;
    
    public SetTimeInstruction(boolean first) {
      this.first = first;
    }

    @Override
    public void preform() {
      editBlock.addEdit(new SetTimeEdit((mxCell)activeVariable, timeVariable, first));
    }
  }
  
  /**
   * Editacni akce pro nastaveni casu
   */
  protected final class SetTimeEdit extends AlgorithmUndoableEdit{
    protected DfsGraphVertex graphNode;
    protected boolean first;
    protected Integer newTime;
    protected Integer oldTime;
    
    public SetTimeEdit(mxCell node, int time, boolean first) {
      this.graphNode = (DfsGraphVertex) node.getValue();
      this.newTime = time;
      this.first = first;
      if (this.first){
        this.oldTime = graphNode.getFirstTimestamp();
      } else {
        this.oldTime = graphNode.getSecondTimestamp();
      }
      action();
    }
    
    @Override
    public void undo() {
      super.undo();
      if (first){
        graphNode.setFirstTimestamp(oldTime);
      } else {
        graphNode.setSecondTimestamp(oldTime);
      }   
    }

    @Override
    protected void action() {
      if (first){
        graphNode.setFirstTimestamp(newTime);
      } else {
        graphNode.setSecondTimestamp(newTime);
      }   
    }
  } 
  
  /**
   * Instrukce pro zmenu casu, nastaveni na nulu nebo inkrementace
   */
  protected class ChangeTimeInstruction extends BasicInstruction {
    protected boolean zero;
    protected boolean inc;        

    public ChangeTimeInstruction() {
      this(false, true);
    }

    public ChangeTimeInstruction(boolean zero) {
      this(zero, false);
    }
    
    public ChangeTimeInstruction(boolean zero, boolean inc) {
      this.zero = zero;
      this.inc = inc;
    }
    

    @Override
    public void preform() {
      if (zero){
        editBlock.addEdit(new ZeroTimeEdit());
      } else if (inc){
        editBlock.addEdit(new IncTimeEdit());
      }
    }
    
  }
    
  /**
   * Editacni akce pro inkrementaci casu
   */
  protected final class IncTimeEdit extends AlgorithmUndoableEdit {
    
    public IncTimeEdit() {
      action();
    }
     
    @Override
    public void undo() {
      super.undo();
      timeVariable--;
    }
    
    @Override
    protected void action() {
      timeVariable++;
    }  
  }
  
  /**
   * Editacni akce pro nastaveni casu na nulu
   */
  protected final class ZeroTimeEdit extends AlgorithmUndoableEdit {
    protected int oldTime;
    
    public ZeroTimeEdit() {
      oldTime = timeVariable;
      action();
    }
     
    @Override
    public void undo() {
      super.undo();
      timeVariable = oldTime;
    }
    
    @Override
    protected void action() {
      timeVariable = 0;
    }  
  }
}
