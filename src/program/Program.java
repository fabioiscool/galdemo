package program;

import com.mxgraph.util.mxResources;
import java.awt.BorderLayout;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.logging.log4j.LogManager;

/**
 * Hlavni spousteci trida aplikace pro demonstraci grafovych algoritmu
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public final class Program extends JFrame{
  public static final String FULLSCREEN_PROPERTY = "Fullscreen";
  public static final String MAXIMIZE_PROPERTY = "Maximize";
  
  private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(Program.class.getName());
  protected ToolBar toolBar;
  protected MenuBar menuBar;
  protected boolean fullscreen = false;
  protected DisplayMode dispModeOld;
  protected boolean maximize = false;
  
  static {
    mxResources.add("resources/czech_language");
	}

  public void about(){
    logger.entry();
    if (fullscreen == true){
      return;
    }
    
    AboutDialog about = new AboutDialog(this, true);

    int x = this.getX() + (this.getWidth() - about.getWidth()) / 2;
    int y = this.getY() + (this.getHeight() - about.getHeight()) / 2;
    about.setLocation(x, y);

    about.setVisible(true);
    
    logger.exit();
  }
  
  public void manual(){
    ManualDialog manual = new ManualDialog(this, true);

    int x = this.getX() + (this.getWidth() - manual.getWidth()) / 2;
    int y = this.getY() + (this.getHeight() - manual.getHeight()) / 2;
    manual.setLocation(x, y);

    manual.setVisible(true);
  }
  
  public void showOrHideLabels(){
    logger.entry();
    toolBar.showOrHideLabels();
    logger.exit();
  }
  
  /**
   * Prevzata metoda z internetu.
   * Kvui problemum s dialogy se fullscreen neda aktivovat.
   * ponechano v programu.
   */
  /**
   * Method allows changing whether this window is displayed in fullscreen or
   * windowed mode.
   * @param fullscreen true = change to fullscreen,
   *                   false = change to windowed
   */
  public void setFullscreen(boolean fullscreen){
      //get a reference to the device.
      GraphicsDevice device  = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
      DisplayMode dispMode = device.getDisplayMode();
      //save the old display mode before changing it.
      dispModeOld = device.getDisplayMode();

      if( this.fullscreen != fullscreen )
      { //are we actually changing modes.
          //change modes.
          this.fullscreen = fullscreen;
          // toggle fullscreen mode
          if( !fullscreen )
          {
              //change to windowed mode.
              //set the display mode back to the what it was when
              //the program was launched.
              device.setDisplayMode(dispModeOld);
              //hide the frame so we can change it.
              setVisible(false);
              //remove the frame from being displayable.
              dispose();
              //put the borders back on the frame.
              setUndecorated(false);
              //needed to unset this window as the fullscreen window.
              device.setFullScreenWindow(null);
              //recenter window
              setLocationRelativeTo(null);
              setResizable(true);

              //reset the display mode to what it was before
              //we changed it.
              setVisible(true);

          }
          else
          { //change to fullscreen.
              //hide everything
              setVisible(false);
              //remove the frame from being displayable.
              dispose();
              //remove borders around the frame
              setUndecorated(true);
              //make the window fullscreen.
              device.setFullScreenWindow(this);
              //attempt to change the screen resolution.
              device.setDisplayMode(dispMode);
              setResizable(false);
              setAlwaysOnTop(false);
              //show the frame
              setVisible(true);
          }
          //make sure that the screen is refreshed.
          repaint();
          this.firePropertyChange(FULLSCREEN_PROPERTY, !this.fullscreen, this.fullscreen);
      }
  }
  
  /**
   * Maximalizace okna aplikace, nedela fullscreen
   * @param maximize 
   */
  public void setMaximize(boolean maximize){
    setFullscreen(false);
    if (this.maximize != maximize){
      this.maximize = maximize;
      if (this.maximize == true){
        setExtendedState(JFrame.MAXIMIZED_BOTH);
      } else {
        setExtendedState(JFrame.NORMAL);
      }
      this.firePropertyChange(MAXIMIZE_PROPERTY, !this.maximize, this.maximize);
    }
  }

  public boolean isFullScreen() {
    return fullscreen;
  }

  public boolean isMaximize() {
    return maximize;
  }
  
  private static void createAndShowGUI() {
    Program program = new Program();
    program.setTitle(mxResources.get("mainTitle"));
    final MainPanel mainPanel = new MainPanel();
    program.toolBar = new ToolBar(mainPanel, JToolBar.HORIZONTAL);
    program.menuBar = new MenuBar(mainPanel);
    program.addPropertyChangeListener(program.menuBar);
    
    mainPanel.getEditorUndoManager().addListener(null, program.toolBar);
    mainPanel.getEditorUndoManager().addListener(null, program.menuBar);
    program.setJMenuBar(program.menuBar);
    program.add(program.toolBar, BorderLayout.NORTH);
    JPanel bottomPanel = new JPanel();
    program.add(bottomPanel, BorderLayout.SOUTH);
    program.getContentPane().add(mainPanel);
    mainPanel.updateTitle();
    program.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    program.addWindowListener(new WindowAdapter(){
      
      @Override
      public void windowClosing(WindowEvent e) {
        /*if (!mainPanel.isModified() || JOptionPane.showConfirmDialog(
          mainPanel, mxResources.get("loseChanges"), mxResources.get("warningSaveTitle"), 
          JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
          System.exit(0);
        }*/
      }
      
    });
    program.setSize(1024, 768);
    program.setVisible(true);
  }
  
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
      Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
    }
    UIManager.put("OptionPane.cancelButtonText", "Zrušit");
    UIManager.put("OptionPane.noButtonText", "Ne");
    UIManager.put("OptionPane.okButtonText", "Ok");
    UIManager.put("OptionPane.yesButtonText", "Ano");
    JFrame.setDefaultLookAndFeelDecorated(true);
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        createAndShowGUI();
      }
    });
  }
}
