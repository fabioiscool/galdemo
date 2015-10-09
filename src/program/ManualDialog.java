package program;

import com.mxgraph.util.mxResources;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

/**
 * Dialog s napovedou
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class ManualDialog extends JDialog{
  
  public ManualDialog(Frame owner, boolean modal) {
    super(owner, modal);
    setTitle(mxResources.get("manual"));
    JTextPane textPane = new JTextPane();
    textPane.setEditable(false);
    try {
      textPane.setPage(Program.class.getResource("/resources/manual.html"));
    } catch (IOException ex) {
    }
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
    
    getContentPane().add(new JScrollPane(textPane), BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    
		getRootPane().setDefaultButton(closeButton);
    setSize(400, 400);
  }
  
    @Override
  protected JRootPane createRootPane(){
    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		JRootPane rootPane = super.createRootPane();
		rootPane.registerKeyboardAction(new ActionListener(){
      @Override
			public void actionPerformed(ActionEvent actionEvent){
				setVisible(false);
			}
		}, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    return rootPane;
	}
}
