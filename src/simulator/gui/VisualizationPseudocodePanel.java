package simulator.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import simulator.algorithms.AbstractAlgorithm;

/**
 * Panel pro pseudokod
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class VisualizationPseudocodePanel extends JPanel{
  protected ControlPanel controlPanel = new ControlPanel();
  protected TextLineNumber tln;
  protected AbstractAlgorithm algorithm;
  protected JTextPane textPane;
  
  public VisualizationPseudocodePanel(final AbstractAlgorithm algorithm) {
    this.algorithm = algorithm;
   // this.setLayout(new MigLayout("", "[grow, fill][]", "[grow,fill][]"));
    this.setLayout(new BorderLayout());
    textPane = new JTextPane(algorithm);
    textPane.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(textPane);
    tln = new TextLineNumber(textPane, 2);
    scrollPane.setRowHeaderView(tln);
    this.add(scrollPane, BorderLayout.CENTER);
    this.add(controlPanel, BorderLayout.SOUTH);
    //System.out.println(textPane.getCaretListeners()[0].toString());
  }

  public AbstractAlgorithm getAlgorithm() {
    return algorithm;
  }
  
  public ControlPanel getControlPanel() {
    return controlPanel;
  }

  public TextLineNumber getTln() {
    return tln;
  }
  
  public void setScrollEnabled(boolean scrollEnabled) {
    tln.setScrollEnabled(scrollEnabled);
  }
  
  public void clearAllHighlights(){
    tln.clearAllHighlight();
  }
  
  public void highlightLine(int part, int line, Color color){
    tln.highlightLine(algorithm.getRealLineNumber(part, line), color);
  }
  
  public void setActualLineHighlightColor(Color actualLineHighlightColor) {
    tln.setActualLineHighlightColor(actualLineHighlightColor);
  }
}
