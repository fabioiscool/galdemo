package program;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.view.mxGraphView;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.TransferHandler;
import program.actions.ActionLogger;
import program.actions.BasicActions;
import program.actions.BasicActions.NewAction;
import program.actions.BasicActions.OpenAction;
import program.actions.BasicActions.RedoAction;
import program.actions.BasicActions.SaveAction;
import program.actions.BasicActions.UndoAction;

/**
 * Toolbar programu
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class ToolBar extends JToolBar implements mxEventSource.mxIEventListener{
  
  private static final Insets margins = new Insets(0, 0, 0, 0);
  protected boolean hideLabels = true;
  protected ToolBarButton redoButton;
  protected ToolBarButton undoButton;
  private boolean ignoreZoomChange = false;
  
  public class ToolBarButton extends JButton {
      
    public ToolBarButton(Action a) {
      super(a);
      //this((Icon) a.getValue(Action.SMALL_ICON));
      //setText((String) a.getValue(Action.NAME));
      setMargin(margins);
      setVerticalTextPosition(BOTTOM);
      setHorizontalTextPosition(CENTER);
      setHideActionText(hideLabels);
      setFocusPainted(false);
      setToolTipText((String) a.getValue(Action.NAME));
    }   
  }
  
  public ToolBar(final MainPanel mainPanel, int orientation) {
    super(orientation);
    //setRollover(true);
		setFloatable(false);
    setBorder(BorderFactory.createCompoundBorder(BorderFactory
            .createEmptyBorder(3, 3, 3, 3), getBorder()));
    add(new ToolBarButton(mainPanel.bind(mxResources.get("new"), 
                          new ActionLogger(new NewAction()), "/images/new.gif")));
    add(new ToolBarButton(mainPanel.bind(mxResources.get("open"), 
                          new ActionLogger(new OpenAction()), "/images/open.gif")));
    add(new ToolBarButton(mainPanel.bind(mxResources.get("save"),  
                          new ActionLogger(new SaveAction(false)), "/images/save.gif")));

		addSeparator();
   
		add(new ToolBarButton(mainPanel.bindGraphAction(mxResources.get("cut"), 
                          new ActionLogger(TransferHandler.getCutAction()), "/images/cut.gif")));
		add(new ToolBarButton(mainPanel.bindGraphAction(mxResources.get("copy"), 
                          new ActionLogger(TransferHandler.getCopyAction()), "/images/copy.gif")));
		add(new ToolBarButton(mainPanel.bindGraphAction(mxResources.get("paste"), 
                          new ActionLogger(TransferHandler.getPasteAction()), "/images/paste.gif")));

		addSeparator();

    add(new ToolBarButton(mainPanel.bindGraphAction(mxResources.get("delete"), 
                          new ActionLogger(mxGraphActions.getDeleteAction()), "/images/delete.gif")));

		addSeparator();

    undoButton = (ToolBarButton) add(new ToolBarButton(mainPanel.bind(mxResources.get("undo"), 
                  new ActionLogger(new UndoAction()), "/images/undo.gif")));
    undoButton.setEnabled(false);
    redoButton = (ToolBarButton) add(new ToolBarButton(mainPanel.bind(mxResources.get("redo"), 
                  new ActionLogger(new RedoAction()), "/images/redo.gif")));
    redoButton.setEnabled(false);
    
		addSeparator();
    final mxGraphView view = mainPanel.getGraphComponent().getGraph()
				.getView();
		/*final JComboBox zoomCombo = new JComboBox(new Object[] { "400%",
				"200%", "150%", "100%", "75%", "50%", mxResources.get("page"),
				mxResources.get("width"), mxResources.get("actualSize") });*/
    final JComboBox<String> zoomCombo = new JComboBox<>(new String[] { "400%",
				"200%", "150%", "100%", "75%", "50%", mxResources.get("actualSize") });
    zoomCombo.setName("Zoom");
		zoomCombo.setEditable(true);
		zoomCombo.setMinimumSize(new Dimension(75, 0));
		zoomCombo.setPreferredSize(new Dimension(75, 0));
		zoomCombo.setMaximumSize(new Dimension(75, 100));
		zoomCombo.setMaximumRowCount(9);
		add(zoomCombo);
    addSeparator();
		// Sets the zoom in the zoom combo the current value
		mxEventSource.mxIEventListener scaleTracker = new mxEventSource.mxIEventListener()
		{
			/**
			 * 
			 */
      @Override
			public void invoke(Object sender, mxEventObject evt)
			{
				ignoreZoomChange = true;

				try
				{
					zoomCombo.setSelectedItem((int) Math.round(100 * view.getScale())+ "%");
				}
				finally
				{
					ignoreZoomChange = false;
				}
			}
		};

		// Installs the scale tracker to update the value in the combo box
		// if the zoom is changed from outside the combo box
		view.getGraph().getView().addListener(mxEvent.SCALE, scaleTracker);
		view.getGraph().getView().addListener(mxEvent.SCALE_AND_TRANSLATE, scaleTracker);

		// Invokes once to sync with the actual zoom value
		scaleTracker.invoke(null, null);

		zoomCombo.addActionListener(new ActionListener()
		{
			/**
			 * 
			 */
      @Override
			public void actionPerformed(ActionEvent e)
			{
				mxGraphComponent graphComponent = mainPanel.getGraphComponent();

				// Zoomcombo is changed when the scale is changed in the diagram
				// but the change is ignored here
				if (!ignoreZoomChange)
				{
					String zoom = zoomCombo.getSelectedItem().toString();

					/*if (zoom.equals(mxResources.get("page")))
					{
						graphComponent.setPageVisible(true);
						graphComponent.setZoomPolicy(mxGraphComponent.ZOOM_POLICY_PAGE);
					}
					else if (zoom.equals(mxResources.get("width")))
					{
						graphComponent.setPageVisible(true);
						graphComponent.setZoomPolicy(mxGraphComponent.ZOOM_POLICY_WIDTH);
					}*/
					if (zoom.equals(mxResources.get("actualSize")))
					{
						graphComponent.zoomActual();
					}
					else
					{
						try
						{
							zoom = zoom.replace("%", "");
							double scale = Math.min(16, Math.max(0.01,Double.parseDouble(zoom) / 100));
							graphComponent.zoomTo(scale, graphComponent.isCenterZoom());
						}
						catch (Exception ex)
						{
							JOptionPane.showMessageDialog(mainPanel, ex.getMessage());
						}
					}
				}
			}
		});
    add(new ToolBarButton(mainPanel.bindGraphAction(mxResources.get("setSelected"), 
                          new ActionLogger(new BasicActions.SetCellSelectedAction()), "/images/preferences.gif")));
  }
  
  public void showOrHideLabels(){
    hideLabels = !hideLabels;
    for (Component c : this.getComponents()){
      if (c instanceof ToolBarButton){
        ToolBarButton button = (ToolBarButton) c;
        button.setHideActionText(hideLabels);
      } else if (c instanceof JComboBox){
        JComboBox cb = (JComboBox) c;
        cb.setMaximumSize(cb.getSize());
      }
    }
  }
  
  @Override
  public void invoke(Object sender, mxEventObject evt) {
    if (sender != null && sender instanceof mxUndoManager){
      mxUndoManager undoManager = (mxUndoManager) sender;
      redoButton.setEnabled(undoManager.canRedo());
      undoButton.setEnabled(undoManager.canUndo());
    }
  }
}
