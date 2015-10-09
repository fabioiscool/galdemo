package simulator.gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import simulator.algorithms.AlgorithmBase;


/**
 * Jedna se o modifikaci prevzane tridy z http://tips4java.wordpress.com/2009/05/23/text-component-line-number/
 * Puvodnim autorem je Rob Camick.
 * Modifikace provedl Jakub Varadinek
 */

/**
 *  This class will display line numbers for a related text component. The text
 *  component must use the same line height for each line. TextLineNumber
 *  supports wrapped lines and will highlight the line number of the current
 *  line in the text component.
 *
 *  This class was designed to be used as a component added to the row header
 *  of a JScrollPane.
 */
public class TextLineNumber extends JPanel implements MouseListener
{
	public final static float LEFT = 0.0f;
	public final static float CENTER = 0.5f;
	public final static float RIGHT = 1.0f;

  public final static Color defaultHighlightColor = Color.decode("#BBFFFF");
  
	private final static Border OUTER = new MatteBorder(0, 0, 0, 2, Color.GRAY);

	private final static int HEIGHT = Integer.MAX_VALUE - 1000000;

  //  Text component this TextTextLineNumber component is in sync with
	private JTextComponent component;

	//  Properties that can be changed

	private int borderGap;
	private Color currentLineForeground;
	private float digitAlignment;
	private int minimumDisplayDigits;
  private int currentLine;
  private int lastPrintedLine;
  
  private Set<Integer> debugLinesSet = new HashSet<>();
  
	//  Keep history information to reduce the number of times the component
	//  needs to be repainted
  private int lastDigits;
  private int lastHeight;
  private Element actualLineElement;
  
  int lastCurrentLine = 0;
	private Map<String, FontMetrics> fonts = new HashMap<>();
  private boolean scrollEnabled = true;
  private LineHighlighter painter;
	/**
	 *	Create a line number component for a text component. This minimum
	 *  display width will be based on 3 digits.
	 *
	 *  @param component  the related text component
	 */
	public TextLineNumber(JTextComponent component){
		this(component, 3, defaultHighlightColor);
	}
  
  public TextLineNumber(JTextComponent component, Color col){
		this(component, 3, col);
	}
  
  public TextLineNumber(JTextComponent component, int minimumDisplayDigits){
		this(component, minimumDisplayDigits, defaultHighlightColor);
	}
	/**
	 *	Create a line number component for a text component.
	 *
	 *  @param component  the related text component
	 *  @param minimumDisplayDigits  the number of digits used to calculate
	 *                               the minimum width of the component
	 */
	public TextLineNumber(final JTextComponent component, int minimumDisplayDigits, Color col){
		this.component = component;
    //component.addMouseListener(this);
    this.addMouseListener(this);
    /*CaretListener listener = new CaretListener() {
      @Override
      public void caretUpdate(CaretEvent caretEvent) {
        //System.out.println("dot:"+ caretEvent.getDot());
        //System.out.println("mark"+caretEvent.getMark());
        Point pt = component.getCaret().getMagicCaretPosition();
        //System.out.println(pt);
        if (pt != null){
          pt.y = pt.y + 100;
          Rectangle rect = new Rectangle(pt, new Dimension(1, 10));
          component.scrollRectToVisible(rect);
          component.invalidate();
        }
      }
    };

    this.component.addCaretListener(listener);*/
    
    
    currentLine = lastPrintedLine = 0;

    Highlighter hilit = new DefaultHighlighter();
    painter = new LineHighlighter(col);
    
    component.setHighlighter(hilit);
    try {
      hilit.addHighlight(0, 0, painter);
    } catch (BadLocationException ex) {
      Logger.getLogger(TextLineNumber.class.getName()).log(Level.SEVERE, null, ex);
    }
		setFont(component.getFont());

		setBorderGap(5);
		setCurrentLineForeground(Color.RED);
		setDigitAlignment(RIGHT);
		setMinimumDisplayDigits(minimumDisplayDigits);  
	}

	/**
	 *  Gets the border gap
	 *
	 *  @return the border gap in pixels
	 */
	public int getBorderGap(){
		return borderGap;
	}

	/**
	 *  The border gap is used in calculating the left and right insets of the
	 *  border. Default value is 5.
	 *
	 *  @param borderGap  the gap in pixels
	 */
	public void setBorderGap(int borderGap){
		this.borderGap = borderGap;
		Border inner = new EmptyBorder(0, borderGap, 0, borderGap);
		setBorder( new CompoundBorder(OUTER, inner) );
		lastDigits = 0;
		setPreferredWidth();
	}

  public boolean isScrollEnabled() {
    return scrollEnabled;
  }

  public void setScrollEnabled(boolean scrollEnabled) {
    this.scrollEnabled = scrollEnabled;
  }

  public void clearAllHighlight(){
    painter.clearAllHighlight();
  }
  
  public void highlightLine(int lineNum, Color color){
    painter.highlightLine(lineNum, color);
  }
  
  public void clearAllhighlightStart(){
    painter.clearAllhighlightStart();
  }
  
  public Map<Integer, Color> getAllhighlightStart(){
    return painter.getAllhighlightStart();
  }
  
  public void highlightLineStart(int lineNum, Color color){
    painter.highlightLineStart(lineNum, color);
    //component.repaint();
  }
  
  public void highlightLineStartArea(int lineNumStart, int lineNumEnd, Color color){
    for (int i = lineNumStart; i <= lineNumEnd; i++){
      painter.highlightLineStart(i, color);
    }
  }
  
  public void setActualLineHighlightColor(Color actualLineHighlightColor) {
    painter.setActualLineHighlightColor(actualLineHighlightColor);
  }

  public Color getActualLineHighlightColor() {
    return painter.getActualLineHighlightColor();
  }
  
	/**
	 *  Gets the current line rendering Color
	 *
	 *  @return the Color used to render the current line number
	 */
	public Color getCurrentLineForeground(){
		return currentLineForeground == null ? getForeground() : currentLineForeground;
	}
  
  public int getCurrentLine(){ 
    return currentLine;
  }
  
  public void setCurrentLine(int line){ 
    if (line < 0 || line > component.getDocument().getDefaultRootElement().getElementCount()) {
      throw new IllegalArgumentException();
    }
    
    currentLine = line; 
    painter.setActualLine(currentLine);
  }
  
  public void moveFromCurrentLine(int count){
    setCurrentLine(currentLine+count);
  }
  
	/**
	 *  The Color used to render the current line digits. Default is Coolor.RED.
	 *
	 *  @param currentLineForeground  the Color used to render the current line
	 */
	public void setCurrentLineForeground(Color currentLineForeground){
		this.currentLineForeground = currentLineForeground;
	}

	/**
	 *  Gets the digit alignment
	 *
	 *  @return the alignment of the painted digits
	 */
	public float getDigitAlignment(){
		return digitAlignment;
	}

	/**
	 *  Specify the horizontal alignment of the digits within the component.
	 *  Common values would be:
	 *  <ul>
	 *  <li>TextLineNumber.LEFT
	 *  <li>TextLineNumber.CENTER
	 *  <li>TextLineNumber.RIGHT (default)
	 *	</ul>
	 *  @param currentLineForeground  the Color used to render the current line
	 */
	public void setDigitAlignment(float digitAlignment){
		this.digitAlignment =
			digitAlignment > 1.0f ? 1.0f : digitAlignment < 0.0f ? -1.0f : digitAlignment;
	}

	/**
	 *  Gets the minimum display digits
	 *
	 *  @return the minimum display digits
	 */
	public int getMinimumDisplayDigits(){
		return minimumDisplayDigits;
	}

	/**
	 *  Specify the mimimum number of digits used to calculate the preferred
	 *  width of the component. Default is 3.
	 *
	 *  @param minimumDisplayDigits  the number digits used in the preferred
	 *                               width calculation
	 */
	public void setMinimumDisplayDigits(int minimumDisplayDigits){
		this.minimumDisplayDigits = minimumDisplayDigits;
		setPreferredWidth();
	}

  public double getLinesNumbersPartWidth(){
    return this.getSize().getWidth() - component.getSize().getWidth();
  }
  
	/**
	 *  Calculate the width needed to display the maximum line number
	 */
	private void setPreferredWidth(){
		Element root = component.getDocument().getDefaultRootElement();
		int lines = root.getElementCount();
		int digits = Math.max(String.valueOf(lines).length(), minimumDisplayDigits);

		//  Update sizes when number of digits in the line number changes

		if (lastDigits != digits)
		{
			lastDigits = digits;
			FontMetrics fontMetrics = getFontMetrics( getFont() );
			int width = fontMetrics.charWidth( '0' ) * (digits);
			Insets insets = getInsets();
			int preferredWidth = insets.left + insets.right + width + 15;
			Dimension d = getPreferredSize();
			d.setSize(preferredWidth, HEIGHT);
			setPreferredSize( d );
			setSize( d );
		}
	}

  /*  Prevzata metoda, link: http://tips4java.wordpress.com/2009/01/04/center-line-in-scroll-pane/
	 *  Attempt to center the line containing the caret at the center of the
	 *  scroll pane.
	 *
	 *  @param component the text component in the sroll pane
	 */
  public static void centerLineInScrollPane(JTextComponent component){
		Container container = SwingUtilities.getAncestorOfClass(JViewport.class, component);

		if (container == null) {
      return;
    }
    try {
      Rectangle r = component.modelToView(component.getCaretPosition());
      JViewport viewport = (JViewport)container;
			int extentHeight = viewport.getExtentSize().height;
			int viewHeight = viewport.getViewSize().height;

			int y = Math.max(0, r.y - (extentHeight / 2));
			y = Math.min(y, viewHeight - extentHeight);

			viewport.setViewPosition(new Point(0, y));
    } catch (BadLocationException ex) {
      Logger.getLogger(TextLineNumber.class.getName()).log(Level.SEVERE, null, ex);
    }
	}
  
  protected void positionCaret(int offset, int realLineNum){
    if (currentLine != lastCurrentLine){
      lastCurrentLine = currentLine;
      if (realLineNum != 1){
        component.setCaretPosition(offset+1);
      }
      else {
        component.setCaretPosition(0);
      }
      centerLineInScrollPane(component);
    }
  }
  
  protected void paintActiveLine(Graphics2D g2d, int x, int y){
    Color oldColor = g2d.getColor();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    Polygon polygon = new Polygon();
    polygon.addPoint(12, y-11);
    polygon.addPoint(12, y+1);
    polygon.addPoint(20, y-5);
    g2d.setColor( Color.GREEN );
    g2d.fillPolygon(polygon);
    g2d.fillRect(5, y-8, 7, 6);   
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
    g2d.setColor(oldColor);
  }
  
  protected void paintDebugSymbol(Graphics2D g2d, int x, int y){
    Color oldColor = g2d.getColor();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    double radius = 5.0;
    x = 28;
    y = y - 5;
    Ellipse2D circle = new Ellipse2D.Double(x - radius, y - radius, 2.0 * radius, 2.0 * radius);
    g2d.setColor(Color.RED);
    g2d.draw(circle);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
    g2d.fill(circle);
    g2d.setColor(oldColor);
  }
  
	/**
	 *  Draw the line numbers
	 */
	@Override
	public void paintComponent(Graphics g){
    super.paintComponent(g);
    component.repaint();
    Graphics2D g2d = (Graphics2D) g;
    Font setFont = g2d.getFont();
    //g2d.setFont(new Font(setFont.getFamily(), setFont.getStyle(), 12));
		//	Determine the width of the space available to draw the line number
		FontMetrics fontMetrics = component.getFontMetrics( component.getFont() );
    
		Insets insets = getInsets();
		int availableWidth = getSize().width - insets.left - insets.right;

		//  Determine the rows to draw within the clipped bounds.

		//Rectangle clip = g.getClipBounds();
		int rowStartOffset = 0; //component.viewToModel( new Point(0, clip.y) );
     
		//int endOffset = component.viewToModel( new Point(0, clip.y + clip.height) );
    int endOffset = component.getDocument().getLength();
    actualLineElement = null;
		while (rowStartOffset <= endOffset){
			try{
        Element root = component.getDocument().getDefaultRootElement();
        final Element line = root.getElement(root.getElementIndex(rowStartOffset));
        int lineNumber = getLineNumber(line);
        final int realLineNum = getRealLineNumber(line);
        if(lastPrintedLine != realLineNum && realLineNum != 0){
          String strLine = getTextLineNumber(lineNumber);
          int stringWidth = fontMetrics.stringWidth(strLine);
          int x = getOffsetX(availableWidth, stringWidth) + insets.left;
          int y = getOffsetY(rowStartOffset, fontMetrics);
          if (realLineNum != 0 && realLineNum == currentLine){
            paintActiveLine(g2d, x, y);
            g2d.setColor(getCurrentLineForeground()); 
            actualLineElement = line;
          } else {
            g2d.setColor(getForeground());
          }
          if (realLineNum != 0 && debugLinesSet.contains(realLineNum)){
            paintDebugSymbol(g2d, x, y);
          } else {
            //  Get the line number as a string and then determine the
            //  "X" and "Y" offsets for drawing the string.
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.drawString(strLine, x, y);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
          }
          lastPrintedLine = realLineNum;
        }
        //  Move to the next row
        rowStartOffset = Utilities.getRowEnd(component, rowStartOffset) + 1;
			}
			catch (BadLocationException ex) {
        Logger.getLogger(TextLineNumber.class.getName()).log(Level.SEVERE, null, ex);
      }
		}
    scrollToCurrentLine();
    lastPrintedLine = 0;
	}

  public void scrollToCurrentLine(){
    if (scrollEnabled){
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          if (actualLineElement != null){
            positionCaret(actualLineElement.getEndOffset(), getRealLineNumber(actualLineElement));
          }
        }
      });     
    }
  }
  
  protected int getRealLineNumber(Element line){
    Object lineNum = line.getAttributes().getAttribute(AlgorithmBase.algorithmRealLineAttribut);
    return (int) (lineNum == null ? 0 : lineNum);
  }
  
  /**
	 *	Get the line number to be drawn. The empty string will be returned
	 *  when a line of text has wrapped.
	 */
	protected int getLineNumber(Element line){ 
    Object lineNum = line.getAttributes().getAttribute(AlgorithmBase.algorithmNumLineAttribut);
    return (int) (lineNum == null ? 0 : lineNum);
	}
  
	protected String getTextLineNumber(int index){
		if (index != 0) {
      return String.valueOf(index);
    }
		else {
      return "";
    }
	}

	/**
	 *  Determine the X offset to properly align the line number when drawn
	 */
	private int getOffsetX(int availableWidth, int stringWidth){
		return (int)((availableWidth - stringWidth) * digitAlignment);
	}

	/**
	 *  Determine the Y offset for the current row
	 */
	private int getOffsetY(int rowStartOffset, FontMetrics fontMetrics) throws BadLocationException{
		//  Get the bounding rectangle of the row

		Rectangle r = component.modelToView( rowStartOffset );
		int lineHeight = fontMetrics.getHeight();
		int y = r.y + r.height;
		int descent = 0;

		//  The text needs to be positioned above the bottom of the bounding
		//  rectangle based on the descent of the font(s) contained on the row.

		if (r.height == lineHeight){  // default font is being used
			descent = fontMetrics.getDescent();
		}
		else { // We need to check all the attributes for font changes
			Element root = component.getDocument().getDefaultRootElement();
			int index = root.getElementIndex( rowStartOffset );
			Element line = root.getElement( index );

			for (int i = 0; i < line.getElementCount(); i++)
			{
				Element child = line.getElement(i);
				AttributeSet as = child.getAttributes();
				String fontFamily = (String)as.getAttribute(StyleConstants.FontFamily);
				Integer fontSize = (Integer)as.getAttribute(StyleConstants.FontSize);
				String key = fontFamily + fontSize;

				FontMetrics fm = fonts.get( key );

				if (fm == null)
				{
					Font font = new Font(fontFamily, Font.PLAIN, fontSize);
					fm = component.getFontMetrics( font );
					fonts.put(key, fm);
				}

				descent = Math.max(descent, fm.getDescent());
			}
		}

		return y - descent;
	}

  public boolean isDebugOnCurrentLine(){
    return debugLinesSet.contains(currentLine);
  }
  
  @Override
  public void mouseClicked(MouseEvent e) {
    if (e.getClickCount() % 2 == 0){
      int offset = component.viewToModel(e.getPoint());
      Element root = component.getDocument().getDefaultRootElement();
      int index = root.getElementIndex(offset);
			Element line = root.getElement(index);
      if (line != null){
        int realLineNum = getRealLineNumber(line);
        if (debugLinesSet.contains(realLineNum)){
          debugLinesSet.remove(realLineNum);
        } else {
          debugLinesSet.add(realLineNum);
        }
        repaint();
      }
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {}

  @Override
  public void mouseReleased(MouseEvent e) {}

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}
}
