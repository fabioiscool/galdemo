package simulator.graphs;

import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;
import java.util.ArrayList;
import java.util.List;

/**
 * Nedokonceny layout pro texty u hran.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class EdgeLabelLayout extends mxGraphLayout {

  public EdgeLabelLayout(mxGraph graph) {
    super(graph);
  }
  
  @Override
  public void execute(Object parent){
		mxGraphView view = graph.getView();
		mxIGraphModel model = graph.getModel();

		// Gets all vertices and edges inside the parent
		List<mxCellState> edges = new ArrayList<>();
		List<mxCellState> vertices = new ArrayList<>();
		int childCount = model.getChildCount(parent);

		for (int i = 0; i < childCount; i++){
			Object cell = model.getChildAt(parent, i);
			mxCellState state = view.getState(cell);

			if (state != null){
				if (((mxCell)state.getCell()).isVertex()){
					vertices.add(state);
				}
				else if (((mxCell)state.getCell()).isEdge()){
					edges.add(state);
				}
			}
		}

		updateLabels(edges, vertices);
	}
  
  protected void updateLabels(List<mxCellState> edges, List<mxCellState> vertices){
    mxIGraphModel model = graph.getModel();
    model.beginUpdate();
		try{
      for (mxCellState state : edges){
        //System.out.println(state.getAbsolutePoints() + " : " + state.getLabelBounds());
        mxRectangle labelBounds = state.getLabelBounds();
        System.out.println(labelBounds.intersectLine(state.getAbsolutePoints().get(0).getX(), state.getAbsolutePoints().get(0).getY(), 
                                  state.getAbsolutePoints().get(state.getAbsolutePoints().size()-1).getX(), state.getAbsolutePoints().get(state.getAbsolutePoints().size()-1).getY()));
        //labelBounds.setX(labelBounds.getX() + 20);
        //labelBounds.setY(labelBounds.getY());
        state.setLabelBounds(labelBounds);
      }
    } finally {
			model.endUpdate();
		}
  }
}
