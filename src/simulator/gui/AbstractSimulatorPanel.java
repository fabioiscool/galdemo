package simulator.gui;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.LayerUI;
import simulator.algorithms.AbstractAlgorithm;
import simulator.controllers.AbstractController;
import simulator.graphs.AlgorithmGraph;
import simulator.graphs.AlgorithmGraphComponent;

/**
 * Obecny panel pro rozsirovani o konkretni simulator
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class AbstractSimulatorPanel extends JPanel{
  
  protected VisualizationPseudocodePanel codePanel;
  protected AlgorithmGraph graph;
  protected AbstractAlgorithm algorithm;
  protected AlgorithmGraphComponent graphComponent;
  protected JPopupMenu popup;
  protected AbstractController controller;
  protected VariablesPanel variablesPanel;
  protected JSplitPane codeAndVariablesPanel;
  protected JPanel graphPanel;
  protected JLabel label;
  protected PropertyChangeListener playPropertyChangeListener;
  
  public AbstractSimulatorPanel(final AlgorithmGraph graph, AbstractAlgorithm algorithm) {
    if (graph == null || algorithm == null){
      throw new NullPointerException();
    }
    
    this.algorithm = algorithm;
    this.graph = graph;
    this.setLayout(new GridLayout(0,1));
    graphComponent = getmxGraphComponent(graph);
    graph.getModel().addListener(mxEvent.CHANGE, new mxEventSource.mxIEventListener(){

      @Override
      public void invoke(Object sender, mxEventObject evt) {
        new mxParallelEdgeLayout(graph).execute(graph.getDefaultParent());
        graph.refresh();
      }
      
    });
    mxRubberband rubberBand = new mxRubberband(graphComponent);
    graphComponent.addMouseWheelListener(new MouseWheelListener() {
      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isControlDown()) {
          if (e.getWheelRotation() < 0) {
            graphComponent.zoomIn();
          }
          else {
            graphComponent.zoomOut();
          }
        }
      }
    });
    codePanel = new VisualizationPseudocodePanel(algorithm);
    variablesPanel = new VariablesPanel();
   
    codePanel.getControlPanel().getForwardButton().setAction(new AbstractAction("Forward",
            new ImageIcon(AbstractSimulatorPanel.class.getResource("/images/player_fwd_48.png"))) {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        controller.performNext();
      }
    });
    this.getInputMap().put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT, 0), "forward");
    this.getActionMap().put("forward", codePanel.getControlPanel().getForwardButton().getAction());
    
    codePanel.getControlPanel().getBackButton().setAction( new AbstractAction("Backward", 
            new ImageIcon(AbstractSimulatorPanel.class.getResource("/images/player_rew_48.png"))){
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        controller.performBefore();
      }
    });
    this.getInputMap().put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LEFT, 0), "backward");
    this.getActionMap().put("backward", codePanel.getControlPanel().getBackButton().getAction());
    
    codePanel.getControlPanel().getPlayButton().setAction(
      new AbstractAction("Play", new ImageIcon(AbstractSimulatorPanel.class.getResource("/images/player_play_48.png"))) {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          controller.runSimulation();
        }
      }
    );
    playPropertyChangeListener = new PropertyChangeListener(){

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        if ((Boolean)evt.getNewValue()){
          codePanel.getControlPanel().getPlayButton().setIcon(new ImageIcon(AbstractSimulatorPanel.class.getResource("/images/player_pause_48.png")));
        } else {
          codePanel.getControlPanel().getPlayButton().setIcon(new ImageIcon(AbstractSimulatorPanel.class.getResource("/images/player_play_48.png")));
        }
      }
      
    };
    codePanel.getControlPanel().getResetButton().setAction(
      new AbstractAction("Reset", new ImageIcon(AbstractSimulatorPanel.class.getResource("/images/player_stop_48.png"))) {
      
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          boolean running = controller.isTimerRunning();
          controller.setTimerStart(false);
          int n = JOptionPane.showConfirmDialog(codePanel, "Opravdu chcete začít simulaci od začátku?",
                                                "Restart Algoritmu", JOptionPane.YES_NO_OPTION);
          if (n == 0){
            controller.resetController();
          } else {
            controller.setTimerStart(running);
          }
        }
      }
    );
    codePanel.getControlPanel().getSpeedSlider().addChangeListener(new ChangeListener(){
      
      @Override
      public void stateChanged(ChangeEvent e) {
        JSlider slider = (JSlider)e.getSource();
        if (slider.getValueIsAdjusting() == false) {
          int delay = ControlPanel.getDelayBySliderValue(slider.getValue(), 
                                              slider.getMaximum(), slider.getMinimum());
          controller.setDelay(delay);
        }
      }

    });
    popup = new JPopupMenu();
    JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(new AbstractAction("Scroll v Grafu"){

      @Override
      public void actionPerformed(ActionEvent e) {
        controller.setScrollEnabled(((JCheckBoxMenuItem)e.getSource()).getState());
      }
      
    });
    menuItem.setState(true);
    popup.add(menuItem);

    menuItem = new JCheckBoxMenuItem(new AbstractAction("Scroll v Kódu"){
      
      @Override
      public void actionPerformed(ActionEvent e) {
        codePanel.setScrollEnabled(((JCheckBoxMenuItem)e.getSource()).getState());
      } 
      
    });
    menuItem.setState(true);
    popup.add(menuItem);
    popup.addSeparator();
    menuItem = new JCheckBoxMenuItem(new AbstractAction("Interaktivní mód"){
      
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.setInteractiveMode(((JCheckBoxMenuItem)e.getSource()).getState());
      } 
      
    });
    menuItem.setState(false);
    popup.add(menuItem);
    popup.addSeparator();
    JMenuItem menuItem1 = new JMenuItem(new AbstractAction("Vrátit Panely"){
      
      @Override
      public void actionPerformed(ActionEvent e) {
        variablesPanel.dockTables();
      } 
      
    });
    popup.add(menuItem1);
    
    LayerUI<JComponent> ui = new LayerUI<JComponent>() {
      @Override
        public void eventDispatched(AWTEvent e, JLayer<? extends JComponent> l) {
          if (e instanceof MouseEvent) {
            switch(e.getID()) {
              case MouseEvent.MOUSE_PRESSED: showPopup((MouseEvent) e); break;
              case MouseEvent.MOUSE_RELEASED: showPopup((MouseEvent) e); break;
            }
          }
       }
      
       private void showPopup(MouseEvent e) {
         if (e.isPopupTrigger()) {
           popup.show(e.getComponent(),e.getX(), e.getY());
         }
       }
    };
    JLayer<JComponent> layer = new JLayer<>(codePanel, ui);
    layer.setLayerEventMask(AWTEvent.MOUSE_EVENT_MASK);
    
    codeAndVariablesPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, variablesPanel, layer);
    codeAndVariablesPanel.setContinuousLayout(true);
    codeAndVariablesPanel.setResizeWeight(0.2);
    codeAndVariablesPanel.setDividerSize(6);
    
    graphPanel = new JPanel(new BorderLayout());
    label = getAlgorithmLabel();
    graphPanel.add(label, BorderLayout.NORTH);
    JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, graphPanel, codeAndVariablesPanel);
    pane.setContinuousLayout(true);
    pane.setResizeWeight(0.8);
    pane.setDividerSize(6);
    this.add(pane);
  }
  
  protected final JLabel getAlgorithmLabel(){
    JLabel newLabel = new JLabel("<html></html>", JLabel.CENTER){   
      //http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4345920
      //Uprava aby fungovalo PreferredSize jako minimum size
      @Override
      public Dimension getPreferredSize(){
        Dimension pSize = super.getPreferredSize();
        Dimension mSize = getMinimumSize();
        int wid, ht;

        wid = pSize.width < mSize.width  ? mSize.width : pSize.width;
        ht = pSize.height < mSize.height ? mSize.height: pSize.height;
        return new Dimension(wid, ht);
      }
    };
    Font labelFont = new Font("Serif", Font.PLAIN, 18);
    newLabel.setFont(labelFont);
    newLabel.setPreferredSize(new Dimension(Short.MAX_VALUE, newLabel.getFontMetrics(labelFont).getHeight() * 2 + 10));
    return newLabel;
  }
  
  public static AlgorithmGraphComponent getmxGraphComponent(AlgorithmGraph graph){
    AlgorithmGraphComponent gc = new AlgorithmGraphComponent(graph);
    gc.getViewport().setBackground(Color.WHITE);
    gc.setConnectable(false);
    gc.setToolTips(true);
    gc.setImportEnabled(false);
    gc.setEnterStopsCellEditing(true);
    return gc;
  }

  public VisualizationPseudocodePanel getCodePanel() {
    return codePanel;
  }

  public AlgorithmGraph getGraph() {
    return graph;
  }

  public AbstractAlgorithm getAlgorithm() {
    return algorithm;
  }

  public AlgorithmGraphComponent getGraphComponent() {
    return graphComponent;
  }

  public JPopupMenu getPopup() {
    return popup;
  }

  public AbstractController getController() {
    return controller;
  }

  public VariablesPanel getVariablesPanel() {
    return variablesPanel;
  }

  public JSplitPane getCodeAndVariablesPanel() {
    return codeAndVariablesPanel;
  }

  public JPanel getGraphPanel() {
    return graphPanel;
  }

  public JLabel getLabel() {
    return label;
  }

  public PropertyChangeListener getPlayPropertyChangeListener() {
    return playPropertyChangeListener;
  }

  public void setController(AbstractController controller) {
    this.controller = controller;
    controller.addTimerPropertyChangeListener(playPropertyChangeListener);
    controller.setDelay(ControlPanel.getStandardTime());
  }

}
