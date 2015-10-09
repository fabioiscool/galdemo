package simulator.gui;

import editor.GraphIO;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import simulator.graphs.AlgorithmGraph;
import simulator.gui.panels.*;

/**
 * Trida pro prime spusteni simulatoru - pouze pro testovaci ucely
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class mainFrame extends JFrame{
  
  public static void createAndShowGUI(){
    mainFrame frame = new mainFrame();
    AlgorithmGraph graph = null;
    try {
      graph = GraphIO.readGraph(Paths.get("C:\\BP\\DijkstraGraph2.xml"));
    } catch (IOException | SAXException | ParserConfigurationException | GraphIO.GraphIOBadXmlException ex) {
      Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
    }
    frame.getContentPane().add(new BellmanFordPanel(graph));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1024, 768);
    frame.setVisible(true);
  }
  
  /*public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
      Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
    }
    JDialog.setDefaultLookAndFeelDecorated(true);
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        createAndShowGUI();
      }
    });
  }*/
}
