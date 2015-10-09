package program;

import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUndoManager;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.actions.ActionLogger;
import program.actions.BasicActions;
import program.actions.BasicActions.AboutAction;
import program.actions.BasicActions.BellmanFordSimulatorAction;
import program.actions.BasicActions.BfsSimulatorAction;
import program.actions.BasicActions.DfsSimulatorAction;
import program.actions.BasicActions.DijkstraSimulatorAction;
import program.actions.BasicActions.ExitAction;
import program.actions.BasicActions.NewAction;
import program.actions.BasicActions.OpenAction;
import program.actions.BasicActions.RedoAction;
import program.actions.BasicActions.SaveAction;
import program.actions.BasicActions.SccSimulatorAction;
import program.actions.BasicActions.SetCellSelectedAction;
import program.actions.BasicActions.SetStyleAction;
import program.actions.BasicActions.ShowManualAction;
import program.actions.BasicActions.ToolBarLabelsAction;
import program.actions.BasicActions.TopologicalSortSimulatorAction;
import program.actions.BasicActions.UndoAction;
import program.actions.BasicActions.ZoomAction;

/**
 * Hlavni menu programu
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class MenuBar extends JMenuBar implements mxEventSource.mxIEventListener, PropertyChangeListener{
  
  private static Logger logger = LogManager.getLogger(MenuBar.class.getName());
  protected MainPanel mainPanel;
  protected JMenuItem undoItem;
  protected JMenuItem redoItem;
  protected JMenuItem aboutItem;
  protected JMenuItem fullscreenItem;
  
  public MenuBar(MainPanel mainPanel) {
    logger.entry(mainPanel);
    
    if (mainPanel == null){
      throw new NullPointerException();
    }
    this.mainPanel = mainPanel;
    addFileItems();
    addEditMenu();
    addViewMenu();
    addSimulateMenu();
    addWindowMenu();
    addHelpMenu();
    
    logger.exit();
  }
  
  private void addFileItems(){
    logger.entry();
    
    JMenu menu = this.add(new JMenu(mxResources.get("file")));
    menu.add(mainPanel.bind(mxResources.get("newGraph"), new ActionLogger(new NewAction()), "/images/new.gif"))
            .setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, 
                                                   java.awt.event.InputEvent.SHIFT_MASK | 
                                                   java.awt.event.InputEvent.CTRL_MASK));
    
		menu.add(mainPanel.bind(mxResources.get("openFile"), new ActionLogger(new OpenAction()), "/images/open.gif"))
            .setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, 
                                                   java.awt.event.InputEvent.SHIFT_MASK | 
                                                   java.awt.event.InputEvent.CTRL_MASK));
    
    menu.addSeparator();

		menu.add(mainPanel.bind(mxResources.get("save"), new ActionLogger(new SaveAction(false)), "/images/save.gif"))
            .setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
		menu.add(mainPanel.bind(mxResources.get("saveAs"), new ActionLogger(new SaveAction(true)), "/images/saveas.gif"));  
    
    menu.addSeparator();
		menu.add(mainPanel.bind(mxResources.get("exit"), new ExitAction()));
    
    logger.exit();
  }
  
  private void addEditMenu(){
    logger.entry();
    
    JMenu menu = this.add(new JMenu(mxResources.get("edit")));
    
    undoItem = menu.add(mainPanel.bind(mxResources.get("undo"), new ActionLogger(new UndoAction()), "/images/undo.gif"));
    undoItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z,  
                                                   java.awt.event.InputEvent.CTRL_MASK));
    undoItem.setEnabled(false);
    
		redoItem = menu.add(mainPanel.bind(mxResources.get("redo"), new ActionLogger(new RedoAction()), "/images/redo.gif"));
    redoItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y,  
                                                   java.awt.event.InputEvent.CTRL_MASK));
    redoItem.setEnabled(false);
    
		menu.addSeparator();

		menu.add(mainPanel.bindGraphAction(mxResources.get("cut"), new ActionLogger(TransferHandler.getCutAction()), "/images/cut.gif"))
            .setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X,  
                                                   java.awt.event.InputEvent.CTRL_MASK));
		menu.add(mainPanel.bindGraphAction(mxResources.get("copy"), new ActionLogger(TransferHandler.getCopyAction()), "/images/copy.gif"))
            .setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C,  
                                                   java.awt.event.InputEvent.CTRL_MASK));
		menu.add(mainPanel.bindGraphAction(mxResources.get("paste"), new ActionLogger(TransferHandler.getPasteAction()), "/images/paste.gif"))
            .setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V,  
                                                   java.awt.event.InputEvent.CTRL_MASK));

		menu.addSeparator();

		menu.add(mainPanel.bindGraphAction(mxResources.get("delete"), new ActionLogger(mxGraphActions.getDeleteAction()), "/images/delete.gif"))
            .setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
		menu.addSeparator();

		menu.add(mainPanel.bindGraphAction(mxResources.get("selectAll"), new ActionLogger(mxGraphActions.getSelectAllAction())))
            .setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A,  
                                                   java.awt.event.InputEvent.CTRL_MASK));
    menu.add(mainPanel.bindGraphAction(mxResources.get("selectAllEdges"), new ActionLogger(mxGraphActions.getSelectEdgesAction())));
    menu.add(mainPanel.bindGraphAction(mxResources.get("selectAllVertices"), new ActionLogger(mxGraphActions.getSelectVerticesAction())));
		menu.add(mainPanel.bindGraphAction(mxResources.get("selectNone"), new ActionLogger(mxGraphActions.getSelectNoneAction())));

		menu.addSeparator();
    menu.add(mainPanel.bindGraphAction(mxResources.get("setSelected"), new ActionLogger(new SetCellSelectedAction())))
            .setAccelerator(KeyStroke.getKeyStroke(' '));
		menu.add(mainPanel.bindGraphAction(mxResources.get("edit"), new ActionLogger(mxGraphActions.getEditAction())));
    menu.add(mainPanel.bindGraphAction(mxResources.get("setStyle"), new ActionLogger(new SetStyleAction())));
    logger.exit();
  }
  
  private void addViewMenu(){
    logger.entry();
    
    JMenu menu = this.add(new JMenu(mxResources.get("view")));
    JMenu submenu = (JMenu) menu.add(new JMenu(mxResources.get("zoom")));
    submenu.add(mainPanel.bind("400%", new ZoomAction(4)));
		submenu.add(mainPanel.bind("200%", new ZoomAction(2)));
		submenu.add(mainPanel.bind("150%", new ZoomAction(1.5)));
		submenu.add(mainPanel.bind("100%", new ZoomAction(1)));
		submenu.add(mainPanel.bind("75%", new ZoomAction(0.75)));
		submenu.add(mainPanel.bind("50%", new ZoomAction(0.5)));
    menu.add(submenu);
    menu.addSeparator();
    /*fullscreenItem = menu.add(new JCheckBoxMenuItem(mainPanel.bind(mxResources.get("fullScreen"), new ActionLogger(new FullScreenAction()))));
    fullscreenItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 
                                  java.awt.event.InputEvent.SHIFT_MASK | 
                                  java.awt.event.InputEvent.ALT_MASK));*/
    menu.add(mainPanel.bind(mxResources.get("maximize"), new ActionLogger(new BasicActions.MaximizeAction())));
    logger.exit();
  }
  
  private void addSimulateMenu(){
    logger.entry();
    
    JMenu menu = this.add(new JMenu(mxResources.get("simulate")));
    menu.add(mainPanel.bind(mxResources.get("BFS"), new ActionLogger(new BfsSimulatorAction())));
    menu.addSeparator();
    menu.add(mainPanel.bind(mxResources.get("DFS"), new ActionLogger(new DfsSimulatorAction())));
    menu.add(mainPanel.bind(mxResources.get("TopologicalSort"), new ActionLogger(new TopologicalSortSimulatorAction())));
    menu.add(mainPanel.bind(mxResources.get("SCC"), new ActionLogger(new SccSimulatorAction())));
    menu.addSeparator();
    menu.add(mainPanel.bind(mxResources.get("BellmanFord"), new ActionLogger(new BellmanFordSimulatorAction())));
    menu.add(mainPanel.bind(mxResources.get("Dijkstra"), new ActionLogger(new DijkstraSimulatorAction())));
    logger.exit();
  }
  
  private void addWindowMenu(){
    logger.entry();
    
    JMenu menu = this.add(new JMenu(mxResources.get("window")));
    menu.add(new JCheckBoxMenuItem(mainPanel.bind(mxResources.get("toolBarLabel"), new ActionLogger(new ToolBarLabelsAction()))));
    menu.addSeparator();

    UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
    ButtonGroup buttonGroup = new ButtonGroup();
		for (int i = 0; i < lafs.length; i++){
			final String className = lafs[i].getClassName();
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(new AbstractAction(lafs[i].getName()){
        @Override
				public void actionPerformed(ActionEvent e){
					mainPanel.setLookAndFeel(className);
				}
			});
      if (lafs[i].getName().equals(UIManager.getLookAndFeel().getName())){
        item.setSelected(true);
      }
      buttonGroup.add(item);
      menu.add(item);
		}
    logger.exit();
  }
  
  private void addHelpMenu(){
    logger.entry();
    
    JMenu menu = this.add(new JMenu(mxResources.get("help")));
    aboutItem = menu.add(mainPanel.bind(mxResources.get("about"), new ActionLogger(new AboutAction())));
    menu.add(mainPanel.bind(mxResources.get("manual"), new ActionLogger(new ShowManualAction())));
    logger.exit();
  }

  @Override
  public void invoke(Object sender, mxEventObject evt) {
    logger.entry(sender, evt);
    if (sender != null && sender instanceof mxUndoManager){
      mxUndoManager undoManager = (mxUndoManager) sender;
      redoItem.setEnabled(undoManager.canRedo());
      undoItem.setEnabled(undoManager.canUndo());
    }
    logger.exit();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    logger.entry(evt);
    switch (evt.getPropertyName()){
      case Program.FULLSCREEN_PROPERTY: aboutItem.setEnabled(!(boolean) evt.getNewValue()); break;
      //case Program.MAXIMIZE_PROPERTY: fullscreenItem.setSelected(false); break;
    }
    logger.exit();
  }
  
}
