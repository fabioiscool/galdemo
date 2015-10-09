package simulator.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import simulator.gui.panels.AbstractAlgorithmPanel;

/**
 * Okno pro panely simulatoru.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class AlgorithmFrame{
  
  private AlgorithmFrame() {
  }
  
  public static JFrame getAlgorithmFrame(final AbstractAlgorithmPanel panel, String name) {
    final JFrame frame = new JFrame();
    frame.setTitle(name);
    frame.getContentPane().add(panel);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.addWindowListener(new WindowAdapter(){
      @Override
      public void windowClosing(WindowEvent e) {
        panel.stop();
        frame.dispose();
      }
    });
    frame.setSize(1024, 768);
    return frame;
  }
  
}
