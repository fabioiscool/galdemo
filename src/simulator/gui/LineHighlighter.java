package simulator.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import simulator.algorithms.AlgorithmBase;

/**
 * Zvyrazni pozadovanou cast radku, zadanou barvou.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class LineHighlighter implements Highlighter.HighlightPainter{
  protected int actualLine = 0;
  protected Color actualLineHighlightColor;
  protected Map<Integer, Color> highlighedLines = new HashMap<>();
  protected Map<Integer, Color> highlighedStartOfLines = new HashMap<>();
  protected boolean resetCarretsetting = true;
  
  public LineHighlighter(Color col) {
    actualLineHighlightColor = col;
  }
  
  protected void paintBeginHighlight(Graphics g, JTextComponent c, Element line) throws BadLocationException{
     TextUI mapper = c.getUI();
     Rectangle r0 = mapper.modelToView(c, line.getStartOffset());
     Rectangle r1 = mapper.modelToView(c, line.getEndOffset());
     /*Object indent = line.getAttributes().getAttribute(StyleConstants.LeftIndent);
     if (indent == null){
       return;
     }*/
     g.fillRect(0, r0.y, 10, r1.y - r0.y);
  }
  
  protected void paintWholeHighlight(Graphics g, JTextComponent c, Element line) throws BadLocationException{
     TextUI mapper = c.getUI();
     Rectangle r0 = mapper.modelToView(c, line.getStartOffset());
     Rectangle r1 = mapper.modelToView(c, line.getEndOffset());
     g.fillRect(0, r0.y, c.getWidth(), r1.y - r0.y);
  }

  @Override
  public void paint(Graphics g, int p0, int p1, Shape bounds, JTextComponent c) {
    try {
      Element root = c.getDocument().getDefaultRootElement();
      int rowStartOffset = root.getStartOffset();
      int endOffset = root.getEndOffset();
      while (rowStartOffset <= endOffset){
        Element line = root.getElement(root.getElementIndex(rowStartOffset));
        Object lineNumO = line.getAttributes().getAttribute(AlgorithmBase.algorithmRealLineAttribut);
        if (lineNumO != null){
          int lineNum = (Integer) lineNumO;
          if (lineNum == actualLine && actualLineHighlightColor != null){
            g.setColor(actualLineHighlightColor);
            paintWholeHighlight(g, c, line);
          }
          if (highlighedStartOfLines.containsKey(lineNum)){
            g.setColor(highlighedStartOfLines.get(lineNum));
            paintBeginHighlight(g, c, line);
          } else if (highlighedLines.containsKey(lineNum)){
            g.setColor(highlighedLines.get(lineNum));
            paintWholeHighlight(g, c, line);
          }
        }
        rowStartOffset = line.getEndOffset() + 1;
      }
    } catch (BadLocationException ex) {
      Logger.getLogger(LineHighlighter.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  public void clearAllHighlight(){
    highlighedLines.clear();
  }
  
  public void highlightLine(int lineNum, Color color){
    highlighedLines.put(lineNum, color);
  }
  
  public void clearAllhighlightStart(){
    highlighedStartOfLines.clear();
  }
  
  public Map<Integer, Color> getAllhighlightStart(){
    Map<Integer, Color> ret = new HashMap<>();
    ret.putAll(highlighedStartOfLines);
    return ret;
  }
  
  public void highlightLineStart(int lineNum, Color color){
    highlighedStartOfLines.put(lineNum, color);
  }
  
  public void setActualLineHighlightColor(Color actualLineHighlightColor) {
    this.actualLineHighlightColor = actualLineHighlightColor;
  }

  public Color getActualLineHighlightColor() {
    return actualLineHighlightColor;
  }

  public int getActualLine() {
    return actualLine;
  }

  public void setActualLine(int actualLine) {
    resetCarretsetting = true;
    this.actualLine = actualLine;
  }
}
