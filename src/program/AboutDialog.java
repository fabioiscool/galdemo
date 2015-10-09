package program;

import com.mxgraph.util.mxResources;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Okno s informacema o programu
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class AboutDialog extends JDialog{
  
  private static Logger logger = LogManager.getLogger(AboutDialog.class.getName());
  
  public AboutDialog(Frame owner, boolean modal) {
    super(owner, modal);
    logger.entry(owner, modal);
    
    setTitle(mxResources.get("aboutDialogTitle"));
    JPanel mainPane = new JPanel(new BorderLayout());
    //mainPane.add(new JPanel(), BorderLayout.CENTER);
    StyleContext sc = new StyleContext();
    final DefaultStyledDocument doc = new DefaultStyledDocument(sc);
    JTextPane pane = new JTextPane(doc);

    final Style hStyle = sc.addStyle("heading", null);
    hStyle.addAttribute(StyleConstants.Foreground, Color.red);
    hStyle.addAttribute(StyleConstants.FontSize, 16);
    hStyle.addAttribute(StyleConstants.FontFamily, Font.SANS_SERIF);
    hStyle.addAttribute(StyleConstants.Bold, true);
    
    final Style bStyle = sc.addStyle("bold", null);
    bStyle.addAttribute(StyleConstants.Foreground, Color.black);
    bStyle.addAttribute(StyleConstants.FontSize, 14);
    bStyle.addAttribute(StyleConstants.FontFamily, Font.SANS_SERIF);
    bStyle.addAttribute(StyleConstants.Bold, true);
    
    Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    
    JTextPane textPane = new JTextPane(doc);
    textPane.setFont(f);
    textPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createMatteBorder(2, 2, 2, 2, Color.GRAY), BorderFactory
				.createEmptyBorder(2, 2, 2, 2)));
    textPane.setText("Simulátor grafových algoritmů\ns podporou interaktivního režimu\nBakalářská práce na FIT VUT\n\n"
            +        "Ikony pro program byly převzaty:\nz knihovny JGraphX a ze stránky http://www.iconfinder.com/"
            +        "\n\nAutor: Jakub Varadinek\nRok: 2013");

    doc.setParagraphAttributes(0, 50, hStyle, false);
    
    doc.setCharacterAttributes(185, 7, bStyle, false);
    doc.setCharacterAttributes(208, 4, bStyle, false);
    textPane.setEditable(false);  
    
    mainPane.add(textPane, BorderLayout.CENTER);
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createMatteBorder(1, 0, 0, 0, Color.GRAY), BorderFactory
				.createEmptyBorder(16, 8, 8, 8)));
		
		JButton closeButton = new JButton(mxResources.get("aboutClose"));
		closeButton.addActionListener(new ActionListener()
		{
      @Override
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		});
		buttonPanel.add(closeButton);
    
    getContentPane().add(mainPane, BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    
		getRootPane().setDefaultButton(closeButton);
    setResizable(false);
		setSize(400, 400);
    
    logger.exit();
  }

  @Override
  protected JRootPane createRootPane(){
		logger.entry();
    
    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		JRootPane rootPane = super.createRootPane();
		rootPane.registerKeyboardAction(new ActionListener(){
      @Override
			public void actionPerformed(ActionEvent actionEvent){
				setVisible(false);
			}
		}, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    
    return logger.exit(rootPane);
	}
}
