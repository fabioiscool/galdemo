package editor;

import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUndoableEdit.mxUndoableChange;
import com.mxgraph.view.mxGraph;
import java.util.List;

/**
 * Undo mnager pro editor grafu. Nektere upravy prevzaty z examplu knihovny JGraphX
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class EditorUndoManager {
  protected mxUndoManager undoManager;
  
  protected mxEventSource.mxIEventListener undoHandler = new mxEventSource.mxIEventListener(){
    @Override
		public void invoke(Object source, mxEventObject evt){
			undoManager.undoableEditHappened((mxUndoableEdit) evt.getProperty("edit"));
		}
	};

	/**
	 * 
	 */
	protected mxEventSource.mxIEventListener changeTracker = new mxEventSource.mxIEventListener(){
    @Override
		public void invoke(Object source, mxEventObject evt){
			//mainPanel.setModified(true);
		}
	};

  public EditorUndoManager(final mxGraph graph) {
    undoManager = new mxUndoManager();
    // Do not change the scale and translation after files have been loaded
		graph.setResetViewOnRootChange(false);

		// Updates the modified flag if the graph model changes
		graph.getModel().addListener(mxEvent.CHANGE, changeTracker);

		// Adds the command history to the model and view
		graph.getModel().addListener(mxEvent.UNDO, undoHandler);
		graph.getView().addListener(mxEvent.UNDO, undoHandler);

		// Keeps the selection in sync with the command history
		mxIEventListener undoListner = new mxIEventListener(){
      @Override
			public void invoke(Object source, mxEventObject evt){
				List<mxUndoableChange> changes = ((mxUndoableEdit) evt
						.getProperty("edit")).getChanges();
				graph.setSelectionCells(graph
						.getSelectionCellsForChanges(changes));
			}
		};
		undoManager.addListener(mxEvent.UNDO, undoListner);
		undoManager.addListener(mxEvent.REDO, undoListner);
  }
 
  public void addListener(String eventName, mxIEventListener listener){
    undoManager.addListener(eventName, listener);
  }
  
  public void undo(){
    undoManager.undo();
  }
  
  public void redo(){
    undoManager.redo();
  }
  
  public boolean canRedo(){
    return undoManager.canRedo();
  }
  
  public boolean canUndo(){
    return undoManager.canUndo();
  }

  public void clear() {
    undoManager.clear();
  }

}
