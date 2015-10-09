package simulator.controllers;

import com.mxgraph.model.mxCell;
import java.util.Deque;
import java.util.LinkedList;
import javax.swing.JLabel;
import simulator.graphs.AlgorithmGraph;
import simulator.graphs.AlgorithmGraph.AlgorithmGraphCellVisitor;
import simulator.graphs.AlgorithmGraphComponent;
import simulator.graphs.GraphCell;
import simulator.graphs.bellmanford.BellmanFordGraphVertex;
import simulator.graphs.bellmanford.BellmanFordGraphEdge;
import simulator.gui.VariablesPanel;
import simulator.gui.VisualizationPseudocodePanel;

/**
 * Ridici jednotka jako nastavba pro algoritmy hledajici nejkratsi cesty
 * Poskytuje zakladni spolecne isntrukce a promenne
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class AbstractShortestPathController extends AbstractController {

  protected Deque<mxCell> cellList = new LinkedList<>();
  protected Integer variableS = 0;
  protected Integer variableV = 1;
  protected Integer variableU = 2; 
  
  /**
   * Inicializace
   * @param graphCom
   * @param graphComponentLabel
   * @param completeCodePanel
   * @param variablesPanel 
   */
  public AbstractShortestPathController(AlgorithmGraphComponent graphCom, JLabel graphComponentLabel, VisualizationPseudocodePanel completeCodePanel, VariablesPanel variablesPanel) {
    super(graphCom, graphComponentLabel, completeCodePanel, variablesPanel);
  }
  
  /**
   * Instrukce pro inicilizaci
   */
  protected class InitInstruction extends BasicInstruction {
    protected String style;
    protected boolean wasSelected = false;
    
    public InitInstruction(String style) {
      this.style = style;
    }

    @Override
    public void preform() {
      graph.traverseAllCells(new AlgorithmGraph.AlgorithmGraphCellVisitor(){
        
        @Override
        public boolean visit(mxCell vertex, GraphCell node) {
          if (node.isSelected()){
            wasSelected = true;
            editBlock.addEdit(new SetColorEdit(style, vertex));
            setRegisterValue(vertex, variableS);
          }
          return true;
        }
        
        @Override
        public boolean allowEdge() {
          return false;
        }

      });
      if (wasSelected == false){
        mxCell vertex = (mxCell) graph.getChildVertices(graph.getDefaultParent())[0];
        editBlock.addEdit(new SetColorEdit(style, vertex));
        setRegisterValue(vertex, variableS);
      }
    }
  }
  
  /**
   * Instrukce pro vyznaceni cest v grafu
   */
  protected class ColorPathsInstruction extends BasicInstruction {
    protected String pathStyle;
    protected String normalStyle;
    
    public ColorPathsInstruction(String pathStyle, String normalStyle) {
      this.pathStyle = pathStyle;
      this.normalStyle = normalStyle;
    }
    
    @Override
    public void preform() {
      graph.traverseAllCells(new AlgorithmGraphCellVisitor(){

        @Override
        public boolean visit(mxCell cell, GraphCell value) {
          if (cell.isEdge()){
            BellmanFordGraphVertex source = (BellmanFordGraphVertex) cell.getSource().getValue();
            BellmanFordGraphVertex target = (BellmanFordGraphVertex) cell.getTarget().getValue();
            if (target.getNodePath() == source){
              editBlock.addEdit(new SetColorEdit(pathStyle, cell));
            } else {
              editBlock.addEdit(new SetColorEdit(normalStyle, cell));
            }
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
   * Instrukce pro nastaveni predchoziho uzlu v ceste
   */
  protected class SetPathInstruction extends BasicInstruction {

    protected Integer regNum1 = null;
    protected Integer regNum2 = null;
    
    public SetPathInstruction(){}

    public SetPathInstruction(int regNum){
      this.regNum1 = regNum;
    }
    
    public SetPathInstruction(int regNum1, int regNum2){
      this.regNum1 = regNum1;
      this.regNum2 = regNum2;
    }
    
    @Override
    public void preform() {
      if (regNum1 == null && regNum2 == null){
        editBlock.addEdit(new SetPathEdit((mxCell)activeVariable, null));
      } else if (regNum1 != null && regNum2 == null){
        editBlock.addEdit(new SetPathEdit((mxCell)activeVariable, (mxCell)getRegisterValue(regNum1)));
      } else {
        editBlock.addEdit(new SetPathEdit((mxCell)getRegisterValue(regNum1), (mxCell)getRegisterValue(regNum2)));
      }
    }
  }
  
  /**
   * Editacni akce pro nastaveni predchozilo uzlu v ceste
   */
  protected final class SetPathEdit extends AlgorithmUndoableEdit{
    protected BellmanFordGraphVertex graphNode;
    protected BellmanFordGraphVertex newPath;
    protected BellmanFordGraphVertex oldPath;
    
    public SetPathEdit(mxCell node, mxCell newPath) {
      this.graphNode = (BellmanFordGraphVertex) node.getValue();
      this.oldPath = graphNode.getNodePath();
      this.newPath = (newPath != null) ? (BellmanFordGraphVertex) newPath.getValue() : null;
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
   * Instrukce vlozeni hrany do aktivniho
   */
  protected class EdgeToActiveInstruction extends BasicInstruction {
    protected int sourceReg;
    protected int targetReg;
    
    public EdgeToActiveInstruction(int sourceReg, int targetReg) {
      this.sourceReg = sourceReg;
      this.targetReg = targetReg;
    }
    
    @Override
    public void preform() {
      editBlock.addEdit(new EdgeToActiveEdit((mxCell)getRegisterValue(sourceReg), 
                                             (mxCell)getRegisterValue(targetReg)));
    }
  }
  
  /**
   * Editacni akce pro vlozeni hrany do aktivniho
   */
  protected final class EdgeToActiveEdit extends AlgorithmUndoableEdit{
    protected mxCell oldActive;
    protected mxCell edge;
    
    public EdgeToActiveEdit(mxCell source, mxCell target) {
      action();
      edge = (mxCell) graph.getEdgesBetween(source, target, true)[0];
      action();
    }
    
    @Override
    public void undo() {
      super.undo();
      activeVariable = oldActive;
    }

    @Override
    protected void action() {
      activeVariable = edge;
    }
  }
  
  /**
   * Instrukce pro vlozeni hrany do registru
   */
  protected class EdgeToRegistersInstruction extends BasicInstruction {
    protected int register1;
    protected int register2;
    
    public EdgeToRegistersInstruction(int register1, int register2) {
      this.register1 = register1;
      this.register2 = register2;
    }

    @Override
    public void preform() {
      editBlock.addEdit(new EdgeToRegistersEdit((mxCell)activeVariable, register1, register2));
    }
    
  }
  
  /**
   * Editacni akce pro vlozeni hrany do registru
   */
  protected final class EdgeToRegistersEdit extends AlgorithmUndoableEdit {
    protected int register1;
    protected int register2;
    protected Object register1Value;
    protected Object register2Value;
    protected mxCell edge;
    
    public EdgeToRegistersEdit(mxCell edge, int register1, int register2) {
      this.register1 = register1;
      this.register2 = register2;
      this.edge = edge;
      this.register1Value = getRegisterValue(register1);
      this.register2Value = getRegisterValue(register2);
      action();
    }

    @Override
    public void undo() {
      super.undo();
      setRegisterValue(register1Value, register1);
      setRegisterValue(register2Value, register2);
    }

    @Override
    protected void action() {
      setRegisterValue(edge.getSource(), register1);
      setRegisterValue(edge.getTarget(), register2);
    }
  }
  
  /**
   * Instrukce pro nastaveni vzdalenosti
   */
  protected class SetDistanceInstruction extends BasicInstruction {
    protected Integer regIndex1;
    protected Integer regIndex2;
    protected Double value;

    public SetDistanceInstruction(Double value){
      this.value = value;
      regIndex1 = null;
    }
    
    public SetDistanceInstruction(int regIndex, Double value){
      this.value = value;
      this.regIndex1 = regIndex;
    }
    
    public SetDistanceInstruction(int regIndex1, int regIndex2){
      this.regIndex1 = regIndex1;
      this.regIndex2 = regIndex2;
    }
    
    @Override
    public void preform() {
      if (regIndex1 == null){
        editBlock.addEdit(new SetDistanceEdit((mxCell) activeVariable, value));
      } else if (regIndex1 != null && value != null){
        editBlock.addEdit(new SetDistanceEdit((mxCell) getRegisterValue(regIndex1), value));
      } else if (regIndex1 != null && regIndex2 != null){
        BellmanFordGraphEdge edge = (BellmanFordGraphEdge) ((mxCell)activeVariable).getValue();
        BellmanFordGraphVertex node = (BellmanFordGraphVertex) ((mxCell)getRegisterValue(regIndex2)).getValue();
        Double newValue = node.getDistance() + edge.getDistance();
        editBlock.addEdit(new SetDistanceEdit((mxCell) getRegisterValue(regIndex1), newValue));
      }
    }
  }
  
  /**
   * Editacni akce pro nastaveni vzdalenosti
   */
  protected final class SetDistanceEdit extends AlgorithmUndoableEdit{
    protected BellmanFordGraphVertex graphNode;
    protected Double newValue;
    protected Double oldValue;
    
    public SetDistanceEdit(mxCell node, Double newValue) {
      this.graphNode = (BellmanFordGraphVertex) node.getValue();
      this.newValue = newValue;
      this.oldValue = graphNode.getDistance();
      action();
    }

    @Override
    public void undo() {
      super.undo();
      graphNode.setDistance(oldValue);
    }

    @Override
    protected void action() {
      graphNode.setDistance(newValue);
    }
  }
}
