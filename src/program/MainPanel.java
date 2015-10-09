package program;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;
import editor.EditorGraph;
import editor.EditorUndoManager;
import editor.GraphViewPanel;
import editor.InformationPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.logging.log4j.LogManager;
import program.actions.ActionLogger;
import program.actions.BasicActions;

/**
 * Hlavni panel editoru.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public final class MainPanel extends JPanel{
  private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(MainPanel.class.getSimpleName());
  
  protected GraphViewPanel graphPanel;
  protected EditorUndoManager editorUndoManager;
  protected boolean modified = false;
  protected File currentFile;
  
  protected mxEventSource.mxIEventListener changeTracker = new mxEventSource.mxIEventListener(){
    @Override
		public void invoke(Object source, mxEventObject evt){
			setModified(true);
		}
	};
  
  public MainPanel() {
    setLayout(new GridLayout(0,1));
    JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    add(pane);
    graphPanel = new GraphViewPanel();
    graphPanel.getGraph().getModel().addListener(mxEvent.CHANGE, changeTracker);
    final JPopupMenu popup = new JPopupMenu();
    JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(this.bindGraphAction(mxResources.get("EdgesLabel"), 
                                                       new ActionLogger(new BasicActions.HideEdgeLabelAction())));
    menuItem.setState(true);
    popup.add(menuItem);
    
    menuItem = new JCheckBoxMenuItem(this.bindGraphAction(mxResources.get("VerticesLabel"), 
                                                       new ActionLogger(new BasicActions.HideVertexLabelAction())));
    menuItem.setState(true);
    popup.add(menuItem);
    popup.addSeparator();
    JMenuItem menuItem1 = new JMenuItem(this.bindGraphAction(mxResources.get("ParallelLayout"), 
                                                       new ActionLogger(new BasicActions.ParallelEdgeLabelAction())));
    popup.add(menuItem1);
    graphPanel.getGraphComponent().getGraphControl().addMouseListener(new MouseAdapter(){

      @Override
      public void mousePressed(MouseEvent e) {
        showPopup(e);
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        showPopup(e);
      }
      
      private void showPopup(MouseEvent e) {
         if (e.isPopupTrigger()) {
           popup.show(e.getComponent(),e.getX(), e.getY());
         }
       }
    });
    
    editorUndoManager = new EditorUndoManager(graphPanel.getGraph());
    pane.setLeftComponent(new InformationPanel(graphPanel));
    pane.setRightComponent(graphPanel.getGraphComponent());
    pane.setResizeWeight(0.3);
    pane.setDividerLocation(250);
  }
  
  public EditorUndoManager getEditorUndoManager() {
    return editorUndoManager;
  }
  
  protected Action bindImpl(final Object sender, String name, final Action action, String iconUrl){
    logger.entry(sender, name, action, iconUrl);
    AbstractAction newAction = new AbstractAction(name, (iconUrl != null) ? 
      new ImageIcon(Program.class.getResource(iconUrl)) : null){
      
      @Override
			public void actionPerformed(ActionEvent e){
				action.actionPerformed(new ActionEvent(sender, e.getID(), 
                               e.getActionCommand()));
			}
		};
		newAction.putValue(Action.SHORT_DESCRIPTION, action.getValue(Action.SHORT_DESCRIPTION));
    return logger.exit(newAction); 
  }
  
  public Action bind(String name, final Action action){
    return bind(name, action, null);
  }
  
  public Action bind(String name, final Action action, String iconUrl){
		logger.entry(name, action, iconUrl);
    return logger.exit(bindImpl(this, name,action, iconUrl));
	}
  
  public Action bindGraphAction(String name, final Action action){
    return bindGraphAction(name, action, null);
  }
  
  public Action bindGraphAction(String name, final Action action, String iconUrl){
    logger.entry(name, action, iconUrl);
    return logger.exit(bindImpl(this.getGraphComponent(), name,action, iconUrl));
  }
  
  public void setLookAndFeel(String className){
    logger.entry(className);
    if (className.equals(UIManager.getLookAndFeel().getName())){
      return;
    }
    JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);
    assert (frame != null);
    try {
      UIManager.setLookAndFeel(className);
      SwingUtilities.updateComponentTreeUI(frame);
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
      Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
    }
    logger.exit();
	}
  
  public mxGraphComponent getGraphComponent() {
    return graphPanel.getGraphComponent();
  }
  
  public boolean isModified(){
		return modified;
	}
  
  public void setModified(boolean modified) {
		boolean oldValue = this.modified;
		this.modified = modified;
		if (oldValue != modified){
			updateTitle();
		}
	}
  
  public void updateTitle(){
		JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);

		if (frame != null){
			String title = (currentFile != null) ? currentFile.getAbsolutePath() : mxResources.get("newDiagram");

			if (modified){
				title += "*";
			}

			frame.setTitle(title + " - " + mxResources.get("mainTitle"));
		}
	}
  
  public EditorGraph getGraph() {
    return graphPanel.getGraph();
  }
  
  public void setGraph(mxGraph newGraph) {
    graphPanel.setGraph(newGraph);
  }
  
  public void setCurrentFile(File file){
		File oldValue = currentFile;
		currentFile = file;

		firePropertyChange("currentFile", oldValue, file);

		if (oldValue != file)
		{
			updateTitle();
		}
	}

  public File getCurrentFile() {
    return currentFile;
  }

  public GraphViewPanel getGraphPanel() {
    return graphPanel;
  }

}
