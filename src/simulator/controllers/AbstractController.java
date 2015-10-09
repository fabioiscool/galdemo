package simulator.controllers;

import com.mxgraph.model.mxCell;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;
import simulator.algorithms.AbstractAlgorithm;
import simulator.graphs.AlgorithmGraph;
import simulator.graphs.AlgorithmGraph.AlgorithmGraphCellVisitor;
import simulator.graphs.AlgorithmGraphComponent;
import simulator.graphs.AlgorithmGraphStylesheet;
import simulator.graphs.GraphCell;
import simulator.gui.TextLineNumber;
import simulator.gui.VariablesPanel;
import simulator.gui.VisualizationPseudocodePanel;

/**
 * Abstraktni trida pro ridici jednotky simulatoru.
 * Jsou zde implemnetace zakladnich instrukci pro navazuji simulatory
 * Trida poskytuje take zakladni metody pres kterou se ridici jednotka ovlada.
 * @author JVaradinek
 */
public abstract class AbstractController {
  
  protected Map<String, VariableObject> variables = new LinkedHashMap<>();
  
  /**
   * Rozhrani pro objekt instrukce
   */
  public interface Instruction {
    public void preform();
    public void stop();
    public int nextTapePos();
    public boolean isInteractive();
  }
  
  /**
   * Trida pro objekty editacni akce
   */
  public abstract static class AlgorithmUndoableEdit extends AbstractUndoableEdit{
    
    protected abstract void action();
    
    @Override
    public void redo() {
      super.redo();
      action();
    }
  }
  
  /**
   * Trida pro objekt, ktery uklada vice editacnich akci
   */
  public class CompoundEditBlock extends CompoundEdit{
    protected int firstTapeIndex;
    protected int lastTapeIndex;

    public int getFirstTapeIndex() {
      return firstTapeIndex;
    }

    public void setFirstTapeIndex(int firstTapeIndex) {
      this.firstTapeIndex = firstTapeIndex;
    }

    public int getLastTapeIndex() {
      return lastTapeIndex;
    }

    public void setLastTapeIndex(int lastTapeIndex) {
      this.lastTapeIndex = lastTapeIndex;
    }
    
  }
  
    
  /**
   * Trida pro interaktivni instrukce
   */
  protected abstract class InteractiveInstruction implements Instruction{
    
    @Override
    public boolean isInteractive() {
      return true;
    }
    
    @Override
    public int nextTapePos() {
      return tapePosition + 1;
    }
  }
  
  /**
   * Trida pro normalni instrukce
   */
  protected abstract class BasicInstruction implements Instruction{
    @Override
    public boolean isInteractive() {
      return false;
    }

    @Override
    public int nextTapePos() {
      return tapePosition + 1;
    }
    
    @Override
    public void stop(){}
  }
  
  /**
   * Rozhrani pro podminku
   */
  protected interface Condition{
    public boolean isFulfil();
  }
  
  protected Color doneColor = Color.red;
     
  protected List<Instruction> tape = new LinkedList<>();
  protected int tapePosition = 0;
  
  protected boolean pause = false;
  protected boolean end = false;
  protected boolean interactiveMode = false;
  
  protected CompoundEditBlock editBlock;
  protected UndoManager undoManager = new UndoManager();
  
  protected Object activeVariable;
  
  protected Object registers[] = new Object[8];
  
  protected Object lastScroll;
  protected boolean scrollEnabled = true;
  
  protected final AbstractAlgorithm algorithm;
  protected AlgorithmGraph graph;
  protected AlgorithmGraphStylesheet stylesheet;
  protected final VisualizationPseudocodePanel completeCodePanel;
  protected final TextLineNumber codePanel;
  protected final VariablesPanel variablesPanel;
  protected AlgorithmGraphComponent graphComponent;
  protected final JLabel graphComponentLabel;
   
  protected mxCell selectedNode = null;
  
  /**
   * Upraveny timer pro opakovani kroku
   */
  protected final ControllerTimer timer = new ControllerTimer(0, new ActionListener(){
    @Override
    public void actionPerformed(ActionEvent e) {
      if (doNext() == false){
        ((Timer)e.getSource()).stop();
      }
      if (codePanel.isDebugOnCurrentLine()){
        timer.stop();
        codePanel.scrollToCurrentLine();
      }
    }
  });
  
  /**
   * Provede zakladni incializaci
   * @param graphCom
   * @param graphComponentLabel
   * @param completeCodePanel
   * @param variablesPanel 
   */
  public AbstractController(final AlgorithmGraphComponent graphCom, final JLabel graphComponentLabel, 
                            final VisualizationPseudocodePanel completeCodePanel, 
                            final VariablesPanel variablesPanel) {
    this.completeCodePanel = completeCodePanel;
    this.algorithm = completeCodePanel.getAlgorithm();
    this.graphComponentLabel = graphComponentLabel;
    this.graphComponent = graphCom;
    this.graph = (AlgorithmGraph) graphComponent.getGraph();
    this.stylesheet = this.graph.getAlgorithmStylesheet();
    this.codePanel = completeCodePanel.getTln();
    this.variablesPanel = variablesPanel;
    addAllAdjLists();
    this.graph.traverseAllCells(new AlgorithmGraphCellVisitor(){

      @Override
      public boolean visit(mxCell cell, GraphCell value) {
        if (value.isSelected()){
          selectedNode = cell;
          return false;
        }
        return true;
      }

      @Override
      public boolean allowEdge() {
        return false;
      }
      
    });
    editBlock = new CompoundEditBlock();
    undoManager.setLimit(10000);
    timer.setRepeats(true);
  }
  
  /**
   * Vytvori tabulkovy zapis pomoci seznamu sousedu
   */
  private void addAllAdjLists(){
    for (Object o : graph.getChildVertices(graph.getDefaultParent())){
      mxCell vertex = (mxCell) o;
      Deque<mxCell> adjList = new LinkedList<>();
      for (int i = 0; i < graph.getModel().getEdgeCount(vertex); i++){
        mxCell edge = (mxCell) ((mxCell) graph.getModel().getEdgeAt(vertex, i));
        mxCell source = (mxCell) edge.getSource();
        mxCell target = (mxCell) edge.getTarget();
        if (source == vertex && adjList.contains(target) == false){
          adjList.addFirst(target);
        } else if (graph.isOriented() == false && target == vertex && adjList.contains(source) == false){
          adjList.addFirst(source);
        }
      }
      variablesPanel.addNodeAdjList(((GraphCell)vertex.getValue()).getName(), dequeToString(adjList));  
    }
  }
  
  /**
   * Metoda updatuje zobrazene promenne
   */
  protected void updateVariablesShow() {
    for (Entry<String, VariableObject> e : variables.entrySet()){
      if (e.getValue().isEnabled()){
        variablesPanel.addVariable(e.getKey(), e.getValue().toString());
      } else {
        variablesPanel.removeVariable(e.getKey());
      }
    }
  }
  
  /**
   * Uvede ridici jednotku do pocatecniho nastaveni
   */
  public void resetController(){
    end = false;
    timer.stop();
    timer.setEnabled(true);
    timer.setRepeats(true);
    graph.resetCells();
    this.graph.traverseAllCells(new AlgorithmGraphCellVisitor(){

      @Override
      public boolean visit(mxCell cell, GraphCell value) {
        if (cell == selectedNode){
          value.setSelected(true);
        } else {
          value.setSelected(false);
        }
        return true;
      }

      @Override
      public boolean allowEdge() {
        return false;
      }
      
    });
    graph.refresh(); 
    editBlock.die();
    editBlock = new CompoundEditBlock();
    undoManager.discardAllEdits(); 
    activeVariable = null;
    for (int i = 0; i < registers.length; i++){
      registers[i] = null;
    }
    lastScroll = null;
    pause = false;
    tapePosition = 0;
    codePanel.setCurrentLine(0);
    codePanel.clearAllHighlight();
    codePanel.clearAllhighlightStart();
    graph.refresh();
    codePanel.repaint();
    updateVariablesShow();
    for (Instruction ins : tape){
      ins.stop();
    }
  }

  /**
   * Nastavi delku prodlevy mezi kroky
   * @param delay 
   */
  public void setDelay(int delay){
    timer.setDelay(delay);
  }

  /**
   * pomocna metoda pro ziskani jmena
   * @param o prekpoklada se typ mxCell, aby se nemuselo pretezovat pri kazdem volani
   * @return 
   */
  protected String getNodeName(Object o){
    if (o == null || o instanceof mxCell == false){
      return "";
    }
    return ((GraphCell)((mxCell)o).getValue()).getName();
  }
  
  /**
   * Metoda prevede Deque<mxCell> do textove podoby jako seznam jmeno jednotlivych uzlu
   * @param q
   * @return 
   */
  public static String dequeToString(Deque<mxCell> q){
    if (q == null){
      return "[]";
    }
    Iterator<mxCell> it = q.iterator();
    if (! it.hasNext()){
        return "[]";
    }

    StringBuilder sb = new StringBuilder();
    sb.append('[');
    for (;;) {
      mxCell node = it.next();
      if (node.isVertex()){
        sb.append(((GraphCell) node.getValue()).getName());
      } else {
        mxCell source = (mxCell) node.getSource();
        mxCell target = (mxCell) node.getTarget();
        sb.append('(');
        sb.append(((GraphCell) source.getValue()).getName());
        sb.append(", ");
        sb.append(((GraphCell) target.getValue()).getName());
        sb.append(')');
      }
      if (! it.hasNext()){
        return sb.append(']').toString();
      }
      sb.append(',').append(' ');
    }
  }
  
  /**
   * Metoda provede vymazani vsech uzlu a hran z grafu
   * @param gc komponenta ve ktere je graf
   */
  protected void clearGraphComponent(AlgorithmGraphComponent gc){
    AlgorithmGraph gcGraph = gc.getGraph();
    for (Object o : gcGraph.getChildCells(gcGraph.getDefaultParent(), true, true)){
      gcGraph.getModel().remove(o);
    }
  }
  
  /**
   * Porovnanavani double cisel
   * @param first
   * @param second
   * @return 
   */
  protected boolean isDoublesEquals(double first, double second){
    if ((first == Double.POSITIVE_INFINITY && second == Double.POSITIVE_INFINITY) ||
         (Math.abs(second - first) <= 0.000001)){
          return true;
    }
    return false;
  }
  
  /**
   * Metoda nastavi zda se ma provade scroll v grafu - najizdeni na aktivniho
   * @param scrollEnabled 
   */
  public void setScrollEnabled(boolean scrollEnabled) {
    this.scrollEnabled = scrollEnabled;
  }
  
  /**
   * spusti simulaci a aktivuje timer pro kroky
   */
  public void runSimulation(){
    if (timer.isEnable() == false){
      performNext();
      timer.start();
    } else if (timer.isRunning()){
      timer.stop();
    } else {
      timer.start();
    }
  }
  
  private transient final java.beans.PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);
  
  public void addTimerPropertyChangeListener(PropertyChangeListener listener) {
    timer.addPropertyChangeListener(listener);
  }

  public void removeTimerPropertyChangeListener(PropertyChangeListener listener) {
    timer.removePropertyChangeListener(listener);
  }
  
  /**
   * Spusti nebo vypne timer
   * @param start 
   */
  public void setTimerStart(boolean start){
    if (start){
      timer.start();
    } else {
      timer.stop();
    }
  }
  
  /**
   * @return je zapnuty timer
   */
  public boolean isTimerRunning(){
    return timer.isRunning();
  }
  
  /**
   * Metoda provede posunti v ramci scroll komponenty na aktivni uzel/hranu v grafu
   */
  protected void scrollToActive(){
    if (scrollEnabled && activeVariable instanceof mxCell && lastScroll != activeVariable){
      lastScroll = activeVariable;
     // graph.setSelectionCell(activeVariable);
      mxCell cell = (mxCell) activeVariable; 
      double centerX = cell.getGeometry().getCenterX();
      double centerY = cell.getGeometry().getCenterY();
      Rectangle viewRect = graphComponent.getViewport().getViewRect();
      //viewRect.x = (int) centerX - viewRect.width / 2;
      //viewRect.y = (int) centerY - viewRect.height / 2;
      //graphComponent.getViewport().scrollRectToVisible(viewRect); 
      Point p = new Point((int)centerX - viewRect.width / 2, (int)centerY - viewRect.height / 2);
      if (p.getX() < 0){
        p.x = 0;
      }
      if (p.getY() < 0){
        p.y = 0;
      }
      graphComponent.getViewport().setViewPosition(p);
    }
  }
  
  /**
   * Provedeni dalsiho kroku
   * @return 
   */
  protected boolean nextStep(){
    if (undoManager.canRedo()){
      undoManager.redo();
      //System.out.println("redo");
    } else if (end == false){
      pause = false;
      while (pause == false && tapePosition < tape.size()){
        Instruction ins = tape.get(tapePosition);
        if (interactiveMode || ins.isInteractive() == false){
          ins.preform();
          tapePosition = ins.nextTapePos();
        } else {
          tapePosition++;
        }
      }
      if (tapePosition == tape.size()){
        //codePanel.setCurrentLine(0);
        return false;
      }
      return true;
    }
    //System.out.println("nextStep: " + getNodeName(activeVariable));
    return undoManager.canRedo();
  }
  
  /**
   * Metoda provede dalsi krok a nasledne aktualizuje zobrazeni
   * @return 
   */
  protected boolean doNext(){
    boolean ret = nextStep();
    graph.refresh();
    codePanel.repaint();
    scrollToActive();
    updateVariablesShow();
    //System.out.println(tapePosition + ":" + tape.size());
    return ret;
  }
  
  /**
   * Metoda pro volani mimo tuto tridu, provede jeden krok dopredu a prerusuje pripadny beh timeru
   * @return 
   */
  public boolean performNext(){
    timer.stop();
    return doNext();
  }
  
  /**
   * @return Metoda vraci true pokud je mozne provest dalsi krok
   */
  public boolean canDoNext(){
    if (undoManager.canRedo() || (end == false && tapePosition < tape.size())){
      return true;
    }
    return false;
  }
  
  /**
   * @return Metoda vraci true pokud je mozne provest krok dozadu
   */
  public boolean canDoBefore(){
    if (tapePosition < tape.size() && tape.get(tapePosition).isInteractive() == true){
      if (tape.get(tapePosition).nextTapePos() == tapePosition){
        return false;
      }
    } 
    if (undoManager.canUndo() == false){
      return false;
    }
    return true;
  }
  
  /**
   * Provede krok dozadu a aktualizuje zobrazeni
   * @return vraci false pokud nelze provest
   */
  protected boolean doBefore(){
    if (canDoBefore()){
      undoManager.undo();
    } else {
      return false;
    }
    graph.refresh();
    codePanel.repaint();
    updateVariablesShow();
    //System.out.println("nextStep: " + getNodeName(activeVariable));
    return true;
  }
  
  /**
   * Provede krok dozadu a vypne timer
   * @return 
   */
  public boolean performBefore(){
    timer.stop();
    return doBefore();
  }

  /**
   * Zapne/vypne interaktivni mod
   * @param interactiveMode 
   */
  public void setInteractiveMode(boolean interactiveMode) {
    if (this.interactiveMode && interactiveMode == false && tapePosition < tape.size()){
      Instruction ins = tape.get(tapePosition);
      if (ins.isInteractive()){
        ins.stop();
      }
    }  
    this.interactiveMode = interactiveMode;
  }
  
  /**
   * Nastavi hodnotu do registru podle indexu
   * @param newValue
   * @param index 
   */
  protected void setRegisterValue(Object newValue, int index){
    registers[index] = newValue;
  } 
  
  /**
   * Ziska hodnotu z registru podle indexu
   * @param index
   * @return 
   */
  protected Object getRegisterValue(int index){
    return registers[index];
  } 
  
  /**
   * Instrukce konce simulace
   */
  protected class EndInstruction extends BasicInstruction {
    @Override
    public void preform() {
      editBlock.addEdit(new SetCursorEdit(0)); 
      editBlock.setLastTapeIndex(tapePosition);
      editBlock.end();
      undoManager.addEdit(editBlock);
      pause = true;
      end = true;
    }  

    @Override
    public int nextTapePos() {
      return tape.size();
    } 
    
  }
  
  /**
   * Instrukce pro vyber pocatecniho uzlu
   */
  protected class SelectStartNodeInstruction extends InteractiveInstruction {
    protected SelectCellsMouseAdapter adapter;
 
    public SelectStartNodeInstruction() {
      adapter = new SelectCellsMouseAdapter(1, graphComponent, graph);
      adapter.setActivated(false);
      graphComponent.getGraphControl().addMouseListener(adapter);
    }
    
    @Override
    public void preform() {
      graphComponentLabel.setText(getTextForLabel("Vyber počáteční uzel a klikni pokračovat\n[uživatelská akce]"));
      if (adapter.isActivated() == false){
        timer.setEnabled(false);
        adapter.setActivated(true);
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
   * Instrukce prove aktivaci vrcholu v grafu - zobrazeni textoveho obsahu
   */
  protected class SetVertexEnableInstruction extends BasicInstruction {

    @Override
    public void preform() {
      editBlock.addEdit(new SetEnableEdit((mxCell)activeVariable));
    }
  }
  
  /**
   * Instrukce aktivuje zobrazeni tooltip u vrcholu
   */
  protected class SetVertexToolTipsInstruction extends BasicInstruction {

    @Override
    public void preform() {
      editBlock.addEdit(new SetEnableToolTipsEdit((mxCell)activeVariable));
    }
  }
  
  /**
   * Instrukce vymaze prvky z fronty
   */
  protected class ClearQueueInstruction extends BasicInstruction {
    protected Deque<mxCell> q;

    public ClearQueueInstruction(Deque<mxCell> q) {
      this.q = q;
    }
 
    @Override
    public void preform() {
      editBlock.addEdit(new ClearQueueEdit(q));
    }
    
  }
  
  /**
   * Editacni akce pro vymazani prvku z fronty
   */
  protected final class ClearQueueEdit extends AlgorithmUndoableEdit{
    protected Deque<mxCell> q;
    protected Deque<mxCell> oldQ;
    
    public ClearQueueEdit(Deque<mxCell> q) {
      this.q = q;
      this.oldQ = new LinkedList<>();
      this.oldQ.addAll(q);
      action();
    }

    @Override
    public void undo() throws CannotUndoException {
      super.undo();
      q.clear();
      q.addAll(oldQ);
    }

    @Override
    protected void action() {
      q.clear();
    }
    
  }
  
  /**
   * Instrukce provede vlozeni prvku na konec fronty
   */
  protected class PushInstruction extends BasicInstruction {
    protected Deque<mxCell> s;
    
    public PushInstruction(Deque<mxCell> s) {
      this.s = s;
    }
    
    @Override
    public void preform() {
      editBlock.addEdit(new EnqueueEdit(this.s, false));
    }
    
  }
  
  /**
   * Instrukce provede vlozeni prvku na zacatek fronty
   */
  protected class EnqueueInstruction extends BasicInstruction {
    protected Deque<mxCell> q;
    
    public EnqueueInstruction(Deque<mxCell> q) {
      this.q = q;
    }
    
    @Override
    public void preform() {
      editBlock.addEdit(new EnqueueEdit(this.q, true));
    }
    
  }
  
  /**
   * Instrukce provede odebrani posledniho prvku z fronty
   */
  protected class DequeueInstruction extends BasicInstruction {
    protected Deque<mxCell> q;
    
    public DequeueInstruction(Deque<mxCell> q) {
      this.q = q;
    }
    
    @Override
    public void preform() {
      editBlock.addEdit(new DequeueEdit(this.q));
    }
    
  }
  
  /**
   * Instrukce provede vlozeni vsech hran do fronty
   */
  protected class AddAllEdgesListInstruction extends BasicInstruction{
    protected Deque<mxCell> q;
    
    public AddAllEdgesListInstruction(Deque<mxCell> q){
      this.q = q;
    }

    @Override
    public void preform() {
      editBlock.addEdit(new AddAllCellToListEdit(this.q, false, true));
    }
    
  }
  
  /**
   * Instrukce provede vlozeni vsech uzlu do fronty
   */
  protected class AddAllVertexListInstruction extends BasicInstruction{
    protected Deque<mxCell> q;
    
    public AddAllVertexListInstruction(Deque<mxCell> q){
      this.q = q;
    }

    @Override
    public void preform() {
      editBlock.addEdit(new AddAllCellToListEdit(this.q, true, false));
    }
    
  }
    
  /**
   * Instrukce vlozi do fronty uzly i hrany
   */
  protected final class AddAllCellToListEdit extends AlgorithmUndoableEdit{
    protected Deque<mxCell> q;
    protected boolean vertices;
    protected boolean edges;
    
    public AddAllCellToListEdit(Deque<mxCell> q, boolean vertices, boolean edges) {
      this.q = q;
      this.vertices = vertices;
      this.edges = edges;
      action();
    }
     
    @Override
    public void undo() {
      super.undo();
      this.q.clear();
    }
    
    @Override
    protected void action() {
      this.q.clear();
      graph.traverseAllCells(new AlgorithmGraph.AlgorithmGraphCellVisitor(){
        @Override
        public boolean visit(mxCell cell, GraphCell node) {
          if ((cell.isVertex() && vertices) || (cell.isEdge() && edges)){
            q.add(cell);
          }
          return true;
        }
        
        @Override
        public boolean allowEdge() {
          return edges;
        }
      });
    }
    
  }
  
  /**
   * Instrukce nastavi hodnotu do registru jako Integer
   */
  protected class SetIntRegisterInstruction extends BasicInstruction {
    protected Integer value;
    protected int registerIndex;
    
    public SetIntRegisterInstruction(int registerIndex, Integer value) {
      this.value = value;
      this.registerIndex = registerIndex;
    }
    
    @Override
    public void preform() {
      editBlock.addEdit(new SetIntRegisterEdit(registerIndex, value));
    }
    
  }
  
  /**
   * Instrukce inkrementuje hodnotu v registru, pokud je to integer
   */
  protected class IncIntRegisterInstruction extends BasicInstruction {
    protected int registerIndex;
    
    public IncIntRegisterInstruction(int registerIndex) {
      this.registerIndex = registerIndex;
    }
    
    @Override
    public void preform() {
      Integer value = (Integer) getRegisterValue(registerIndex) + 1;
      editBlock.addEdit(new SetIntRegisterEdit(registerIndex, value));
    }
    
  }
  
  /**
   * Editacni akce pro nastaveni integer hodnoty do registru
   */
  protected final class SetIntRegisterEdit extends AlgorithmUndoableEdit{
    protected Integer newValue;
    protected Integer oldValue;
    protected int registerIndex;
    
    public SetIntRegisterEdit(int registerIndex, Integer newValue) {
      this.newValue = newValue;
      this.oldValue = (Integer) getRegisterValue(registerIndex);
      this.registerIndex = registerIndex;
      action();
    }
    
    @Override
    public void undo() {
      super.undo();
      setRegisterValue(oldValue, registerIndex);
    }

    @Override
    protected void action() {
      setRegisterValue(newValue, registerIndex);
    }
  }
  
  /**
   * Instrukce vytvori seznam sousedu a vlozi ho do fronty
   */
  protected class MakeAdjListInstruction extends BasicInstruction {
    protected Deque<mxCell> q;
    protected boolean oriented;
    
    public MakeAdjListInstruction(Deque<mxCell> q, boolean oriented){
      this.q = q;
      this.oriented = oriented;
    }
    
    @Override
    public void preform() {
      editBlock.addEdit(new MakeAdjListEdit(this.q, this.oriented));
    } 
  }
  
  /**
   * Instrukce pro ukonceni kroku
   */
  protected class PauseInstruction extends BasicInstruction {
    protected boolean bigPause = false;

    public PauseInstruction setBigPause(boolean bigPause) {
      this.bigPause = bigPause;
      return this;
    }
    
    @Override
    public void preform() {
      editBlock.setLastTapeIndex(tapePosition);
      editBlock.end();
      undoManager.addEdit(editBlock);
      editBlock = new CompoundEditBlock();
      editBlock.setFirstTapeIndex(tapePosition);
      pause = true;
    }
  }
  
  /**
   * Instrukce pro nastaveni pozice v pasce
   */
  protected class SetTapePositionInstruction extends BasicInstruction {
    protected int tapePos;

    public SetTapePositionInstruction(int tapePos) {
      this.tapePos = tapePos;
    }
    
    @Override
    public void preform() {
      tapePosition = this.tapePos;
    }
  }
  
  /**
   * Instrukce pro nastaveni stylu k aktivni bunce
   */
  protected class SetColorInstruction extends BasicInstruction {
    protected String style;

    public SetColorInstruction(String style) {
      this.style = style;
    }
    
    @Override
    public void preform() {
      editBlock.addEdit(new SetColorEdit(style, (mxCell)activeVariable));
    }
  }
    
  /**
   * Instrukce nasttaveni cisla radku pro panel pseudokodu
   */
  protected class SetCursorInstruction extends BasicInstruction {
    protected int cursorPos;

    public SetCursorInstruction(int cursorPos) {
      this.cursorPos = cursorPos;
    }
    
    @Override
    public void preform() {
      editBlock.addEdit(new SetCursorEdit(cursorPos)); 
    }
  }
  
  /**
   * Instrukce pro nastaveni popisu
   */
  protected class SetLabelTextInstruction extends BasicInstruction {
    protected String text;

    public SetLabelTextInstruction(String text) {
      this.text = text;
    }
    
    @Override
    public void preform() {
      editBlock.addEdit(new SetLabelTextEdit(text)); 
    }
  }
  
  /**
   * Instrukce navesti pro algoritmus
   */
  protected class LabelInstruction extends BasicInstruction {
    
    protected Instruction jump;
    protected Condition condition;
    
    public LabelInstruction() {
      this.jump = null;
    }
    
    public LabelInstruction(Instruction jump, Condition condition) {
      this.jump = jump;
      this.condition = condition;
    }
    
    @Override
    public void preform() {
      if (jump != null){
        if (condition == null || condition.isFulfil()){
          new SetTapePositionInstruction(tape.indexOf(jump)).preform();
        }
      }
    }
  }

  /**
   * Instrukce presunu registru do aktivniho
   */
  protected class RegisterToActiveInstruction extends BasicInstruction {
    protected int registerIndex;
    
    public RegisterToActiveInstruction(int registerIndex) {
      this.registerIndex = registerIndex;
    }

    @Override
    public void preform() {
      editBlock.addEdit(new MoveRegisterToActive(registerIndex));
    }
    
  }
  
  /**
   * Instrukce pro nastaveni stylu hrany mezi vrcholem v registru a aktivnim
   */
  protected class ColorEdgeActiveAndRegisterInstruction extends BasicInstruction {
    protected String newStyle;
    protected int registerNum;
    protected boolean oriented;
    
    public ColorEdgeActiveAndRegisterInstruction(int registerNum, String newStyle, boolean oriented) {
      this.newStyle = newStyle;
      this.registerNum = registerNum;
      this.oriented = oriented;
    }

    @Override
    public void preform() {
      for (int i = 0; i < graph.getModel().getEdgeCount(activeVariable); i++){
        mxCell edge = (mxCell) graph.getModel().getEdgeAt(activeVariable, i);
        mxCell source = (mxCell) edge.getTarget();
        mxCell target = (mxCell) edge.getSource();   
        if (getRegisterValue(registerNum) == activeVariable){
          if (source == activeVariable && target == activeVariable){
            editBlock.addEdit(new ChangeEdgeStyleEdit(edge, newStyle));
          }
        } else if (target == getRegisterValue(registerNum) || 
                  (oriented == false && source == getRegisterValue(registerNum))){
          editBlock.addEdit(new ChangeEdgeStyleEdit(edge, newStyle));
        } 
      }
    } 
  }
  
  /**
   * Instrukce nastavi vrchol s priznakem select djako prvni ve fronte
   */
  protected class SetSelectedCellFirstInstruction extends BasicInstruction {
    protected Deque<mxCell> q;       

    public SetSelectedCellFirstInstruction(Deque<mxCell> q) {
      this.q = q;
    }
    
    @Override
    public void preform() {
      graph.traverseAllCells(new AlgorithmGraphCellVisitor(){
        @Override
        public boolean visit(mxCell vertex, GraphCell node) { 
         if (node.isSelected() && q.contains(vertex)){
           editBlock.addEdit(new SetSelectedCellFirstEdit(q, vertex));
           return false;
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
   * Editacni akce pro nastaveni selected vrcholu jako prvniho
   */
  protected final class SetSelectedCellFirstEdit extends AlgorithmUndoableEdit{
    protected Deque<mxCell> q;
    protected mxCell cell;
    protected Deque<mxCell> oldQ;
    
    public SetSelectedCellFirstEdit(Deque<mxCell> q, mxCell cell) {
      this.q = q;
      this.cell = cell;
      oldQ = new LinkedList<>();
      oldQ.addAll(q);
      action();
    }
    
    @Override
    public void undo() {
      super.undo();
      q.clear();
      q.addAll(oldQ);
    }

    @Override
    protected void action() {
      if (q.contains(cell)){
        q.remove(cell);
        q.addFirst(cell);
      }
    }
    
  } 
  
  /**
   * Instrukce pro nastaveni aktivniho grafu
   */
  protected class SetNewActiveGraphInstruction extends BasicInstruction {
    protected AlgorithmGraph newGraph = null;;
    protected AlgorithmGraphComponent newComp = null;
    
    public SetNewActiveGraphInstruction(AlgorithmGraphComponent newComp) {
      this.newComp = newComp;
    }
    
    public SetNewActiveGraphInstruction(AlgorithmGraph newGraph) {
      this.newGraph = newGraph;
    }

    @Override
    public void preform() {
      if (newComp == null){
        editBlock.addEdit(new SetNewActiveGraphEdit(newGraph));
      } else {
        editBlock.addEdit(new SetNewActiveGraphEdit(newComp.getGraph()));
        editBlock.addEdit(new SetNewActiveGraphComponentEdit(newComp));
      }
    } 
  }
  
  /**
   * Instrukce pro presunuti aktivniho do registru
   */
  protected class ActiveToRegisterInstruction extends BasicInstruction {
    protected int registerIndex;
    
    public ActiveToRegisterInstruction(int registerIndex) {
      this.registerIndex = registerIndex;
    }

    @Override
    public void preform() {
      editBlock.addEdit(new MoveActiveToRegister(registerIndex, activeVariable));
    }
    
  }
  
  /**
   * Instrukce pro schovani nebo ukazani obsahu promenne v panelu promennych
   */
  protected class ShowOrHideVariableInstruction extends BasicInstruction {
    protected boolean show;
    protected String variable;
    
    public ShowOrHideVariableInstruction(String variable, boolean show) {
      this.show = show;
      this.variable = variable;
    }
    
    @Override
    public void preform() {
      editBlock.addEdit(new ShowOrHideVariableEdit(variables.get(variable), show));
    }
  }
  
  /**
   * Instrukce pro nastaveni barvy na cislo radku
   */
  protected class SetColorHighlighInstruction extends BasicInstruction {
    protected int start;
    protected int end;
    protected Color c;
    
    public SetColorHighlighInstruction(int start, int end, Color c) {
      this.start = start;
      this.end = end;
      this.c = c;  
    }

    @Override
    public void preform() {
      editBlock.addEdit(new SetColorHighlighEdit(start, end, c));
    }
    
  }
  
  /**
   * Editacni akce pro aktivovani vrcholu
   */
  protected final class SetEnableEdit extends AlgorithmUndoableEdit{
    protected GraphCell graphNode;
    protected boolean enable;
    
    public SetEnableEdit(mxCell node) {
      this.graphNode = (GraphCell) node.getValue();
      this.enable = graphNode.isEnabled();
      action();
    }

    @Override
    public void undo() {
      super.undo();
      graphNode.setEnabled(enable);
    }

    @Override
    protected void action() {
      graphNode.setEnabled(!enable);
    }
  }
  
  /**
   * Editacni akce pro nastaveni nove aktivni komponenty pro graf
   */
  protected final class SetNewActiveGraphComponentEdit extends AlgorithmUndoableEdit{
    protected AlgorithmGraphComponent newGraph;
    protected AlgorithmGraphComponent oldGraph;
    
    public SetNewActiveGraphComponentEdit(AlgorithmGraphComponent newGraph) {
      this.oldGraph = graphComponent;
      this.newGraph = newGraph;
      action();
    }

    @Override
    public void undo() {
      super.undo();
      graphComponent = oldGraph;
    }

    @Override
    protected void action() {
      graphComponent = newGraph;
    }
  }
  
  /**
   * Editacni akce pro nastaveni noveho aktivniho grafu
   */
  protected final class SetNewActiveGraphEdit extends AlgorithmUndoableEdit{
    protected AlgorithmGraph newGraph;
    protected AlgorithmGraph oldGraph;
    
    public SetNewActiveGraphEdit(AlgorithmGraph newGraph) {
      this.oldGraph = graph;
      this.newGraph = newGraph;
      action();
    }

    @Override
    public void undo() {
      super.undo();
      graph = oldGraph;
    }

    @Override
    protected void action() {
      graph = newGraph;
    }
  } 
  
  /**
   * Editacni akce pro povoleni tooltips
   */
  protected final class SetEnableToolTipsEdit extends AlgorithmUndoableEdit{
    protected GraphCell graphNode;
    protected boolean toolTips;
    
    public SetEnableToolTipsEdit(mxCell node) {
      this.graphNode = (GraphCell) node.getValue();
      this.toolTips = graphNode.isToolTipsEnabled();
      action();
    }

    @Override
    public void undo() {
      super.undo();
      graphNode.setToolTipsEnabled(toolTips);
    }

    @Override
    protected void action() {
      graphNode.setToolTipsEnabled(!toolTips);
    }
  }

  /**
   * @param text
   * @return Metoda vraci text pro popis v html
   */
  protected String getTextForLabel(String text){
    return "<html><div style=\"text-align: center;\">" + text.replaceAll("\n", "<br>") +"</html>";
  }
  
  /**
   * Editacni akce pro nastaveni popisu
   */
  protected final class SetLabelTextEdit extends AlgorithmUndoableEdit{
    protected String oldText;
    protected String newText;
    
    public SetLabelTextEdit(String newText) {
      this.oldText = graphComponentLabel.getText();
      this.newText = getTextForLabel(newText);
      action();
    }

    @Override
    public void undo() {
      super.undo();
      graphComponentLabel.setText(oldText);
    }

    @Override
    protected void action() {
      graphComponentLabel.setText(newText);
    }
  }
  
  /**
   * Editacni akce pro nastaveni stylu bunky
   */
  protected final class SetColorEdit extends AlgorithmUndoableEdit{
    protected mxCell node;
    protected String oldStyle;
    protected String newStyle;
    
    public SetColorEdit(String newStyle, mxCell node) {
      this.node = node;
      this.newStyle = newStyle;
      this.oldStyle = node.getStyle();
      action();
    }

    @Override
    public void undo() {
      super.undo();
      graph.setCellStyle(oldStyle, new Object[]{node});
    }

    @Override
    protected void action() {
      graph.setCellStyle(newStyle, new Object[]{node});
    }
  }
  
  /**
   * Editacni akce pro nastaveni pozice v panelu pseudokodu
   */
  protected final class SetCursorEdit extends AlgorithmUndoableEdit{
    protected int oldPosition;
    protected int newPosition;

    public SetCursorEdit(int newPosition) {
      
      this.newPosition = newPosition;
      this.oldPosition = codePanel.getCurrentLine();
      action();
    }

    @Override
    protected void action(){
      codePanel.setCurrentLine(newPosition);
    }
    
    @Override
    public void undo() {
      super.undo();
      codePanel.setCurrentLine(oldPosition);
    }
  }
  
  /**
   * Editacni akce pro presun aktivniho do registru
   */
  protected final class MoveActiveToRegister extends AlgorithmUndoableEdit{
    protected Object oldRegister;
    protected Object active;
    protected int registerNumber;
    
    public MoveActiveToRegister(int registerNumber, Object active) {
      this.oldRegister = registers[registerNumber];
      this.registerNumber = registerNumber;
      this.active = active;
      action();
    }

    @Override
    public void undo() {
      super.undo();
      registers[registerNumber] = oldRegister;
    }

    @Override
    protected void action() {
      registers[registerNumber] = this.active;
    }
  }
  
  /**
   * Editacni akce pro presun registru do aktivniho
   */
  protected final class MoveRegisterToActive extends AlgorithmUndoableEdit{
    protected Object oldActive;
    protected int registerNumber;
    
    public MoveRegisterToActive(int registerNumber) {
      this.oldActive = activeVariable;
      this.registerNumber = registerNumber;
      action();
    }

    @Override
    public void undo() {
      super.undo();
      activeVariable = oldActive;
    }

    @Override
    protected void action() {
      activeVariable = registers[registerNumber];
    }
  }
  
  /**
   * Editacni akce pro zmenu stylu u hrany
   */
  protected final class ChangeEdgeStyleEdit extends AlgorithmUndoableEdit{
    protected mxCell edge;
    protected String newStyle;
    protected String oldStyle;
    
    public ChangeEdgeStyleEdit(mxCell edge, String newStyle) {
      this.edge = edge;
      this.newStyle = newStyle;
      this.oldStyle = edge.getStyle();
      action();
    }
    
    @Override
    public void undo() {
      super.undo();
      edge.setStyle(oldStyle);
    }
    
    @Override
    protected void action() {
      edge.setStyle(newStyle);
    }
    
  }
  
  /**
   * Editacni akce pro nastaveni zvyrazneni v panelu pseudokodu
   */
  protected final class SetColorHighlighEdit extends AlgorithmUndoableEdit{
    protected int start;
    protected int end;
    protected Color c;
    protected Map<Integer, Color> lastHighlight;
            
    public SetColorHighlighEdit(int start, int end, Color c){
      this.start = start;
      this.end = end;
      this.c = c;
      this.lastHighlight = codePanel.getAllhighlightStart();
      action();
    }
    
    @Override
    public void undo() {
      super.undo();
      for (Entry<Integer, Color> e : lastHighlight.entrySet()){
        codePanel.highlightLineStart(e.getKey(), e.getValue());
      }
    }
    
    @Override
    protected void action() {
      codePanel.highlightLineStartArea(start, end, c);
    }
    
  }
  
  /**
   * Editacni akce pro vytvoreni seznamu sousedu
   */
  protected final class MakeAdjListEdit extends AlgorithmUndoableEdit{

    protected Deque<mxCell> q;
    protected mxCell oldActive;
    protected boolean oriented;
    
    public MakeAdjListEdit(Deque<mxCell> q, boolean oriented) {
      this.q = q;
      this.oldActive = (mxCell) activeVariable;
      this.oriented = oriented;
      action();
    }
    
    private void addAllVerticesToAdjlist(mxCell vertex){
      q.clear();
      for (int i = 0; i < graph.getModel().getEdgeCount(vertex); i++){
        mxCell edge = (mxCell) ((mxCell) graph.getModel().getEdgeAt(vertex, i));
        mxCell source = (mxCell) edge.getSource();
        mxCell target = (mxCell) edge.getTarget();
        if (source == vertex){
          q.addFirst(target);
        } else if (oriented == false && target == vertex){
          q.addFirst(source);
        }
      }
    }
    
    @Override
    public void undo() {
      super.undo();
      q.clear();
    }

    @Override
    protected void action() {
      q.clear();
      addAllVerticesToAdjlist(oldActive);
    }
  }
  
  /**
   * Editacni akce pro vlozeni na konec fronty
   */
  protected final class EnqueueEdit extends AlgorithmUndoableEdit{

    protected mxCell oldActive;
    protected Deque<mxCell> q;
    protected boolean last;
            
    public EnqueueEdit(Deque<mxCell> q, boolean last) {
      this.q = q;
      this.last = last;
      oldActive = (mxCell) activeVariable;
      action();
    }

    @Override
    public void undo() {
      super.undo();
      this.q.remove(oldActive);
    }

    @Override
    protected void action() {
      if (last){
        this.q.addLast(oldActive);
      } else {
        this.q.addFirst(oldActive);
      }
    }
  }
  
  /**
   * Editacni akce pro odebrani z fronty
   */
  protected final class DequeueEdit extends AlgorithmUndoableEdit{

    protected mxCell oldActive;
    protected mxCell oldFirstInQueue;
    protected Deque<mxCell> q;
    
    public DequeueEdit(Deque<mxCell> q) {
      this.q = q;
      
      oldActive = (mxCell) activeVariable;
      oldFirstInQueue = this.q.getFirst();
      action();
    }
    
    @Override
    public void undo() {
      super.undo();
      this.q.addFirst(oldFirstInQueue);
      activeVariable = oldActive;
    }

    @Override
    protected void action() {
      activeVariable = this.q.removeFirst();
    }
  }
  
  /**
   * Editacni akce pro skyti/zobrazeni obsahu promenne
   */
  protected final class ShowOrHideVariableEdit extends AlgorithmUndoableEdit{
    protected boolean show;
    protected boolean showBefore;
    protected VariableObject variable;
    
    public ShowOrHideVariableEdit(VariableObject variable, boolean show) {
      this.variable = variable;
      this.show = show;
      this.showBefore = variable.isShow();
      action();
    }
    
    @Override
    public void undo() {
      super.undo();
      variable.setShow(showBefore);
    }
    
    @Override
    protected void action() {
      variable.setShow(show);
    }
    
  }
  
}
