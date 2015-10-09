package program.actions;

import com.mxgraph.analysis.mxAnalysisGraph;
import com.mxgraph.analysis.mxGraphStructure;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import editor.GraphIO;
import editor.GraphViewPanel;
import editor.StyleDialogMaker;
import editor.StyleDialogMaker.StyleDialog;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import program.MainPanel;
import program.Program;
import simulator.graphs.AlgorithmGraph;
import simulator.graphs.BasicStylesheet;
import simulator.graphs.GraphEdgeData;
import simulator.graphs.GraphVertexData;
import simulator.gui.AlgorithmFrame;
import simulator.gui.panels.BellmanFordPanel;
import simulator.gui.panels.BfsPanel;
import simulator.gui.panels.DfsPanel;
import simulator.gui.panels.DijkstraPanel;
import simulator.gui.panels.SccPanel;
import simulator.gui.panels.TopologicalSortPanel;

/**
 * Akce v teto tride slouzi pro hlavni menu a toolbar
 * Inspirace a castecne prebirani kodu z examplu knihovny JGraphX
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class BasicActions {
  
  /**
   * Akce pro ukonceni aplikace
   */
  public static class ExitAction extends GraphComponentAction{
    
    @Override
		public void actionPerformed(ActionEvent e){
      MainPanel editor = getMainPanel(e);
      
      if (!editor.isModified() || JOptionPane.showConfirmDialog(
          editor, mxResources.get("loseChanges"), mxResources.get("warningSaveTitle"), 
          JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
        System.exit(0);
        //getProgramFrame(e).dispose();
      }
		}
    
	}
  
  /**
   * Akce pro zobrazeni dialogu about
   */
  public static class AboutAction extends GraphComponentAction{
    
    @Override
		public void actionPerformed(ActionEvent e){
      getProgramFrame(e).about();
		}
	}
  
  /**
   * Akce pro zobrazeni manualu
   */
  public static class ShowManualAction extends GraphComponentAction{
    
    @Override
		public void actionPerformed(ActionEvent e){
      getProgramFrame(e).manual();
		}
	}
  /**
   * Akce pro vytvoreni noveho grafu
   */
  public static class NewAction extends GraphComponentAction{
    
    @Override
		public void actionPerformed(ActionEvent e){
      MainPanel editor = getMainPanel(e);

			if (editor != null){
				if (!editor.isModified() || 
             JOptionPane.showConfirmDialog(editor, mxResources.get("loseChanges")) == JOptionPane.YES_OPTION){
          editor.getGraphPanel().clearGraph();
					editor.getEditorUndoManager().clear();

					editor.setModified(false);
					editor.setCurrentFile(null);
					editor.getGraphComponent().zoomAndCenter();
				}
			}
		}
	}
  
  /**
   * Akce pro otevreni grafu ze souboru
   */
  public static class OpenAction extends GraphComponentAction{
		protected String lastDir;
    
    private boolean checkGraphNames(AlgorithmGraph loadedGraph){
      for (Object o1 : loadedGraph.getChildVertices(loadedGraph.getDefaultParent())){
        for (Object o2 : loadedGraph.getChildVertices(loadedGraph.getDefaultParent())){
          if (o1 != o2){
            GraphVertexData data1 = (GraphVertexData) ((mxCell) o1).getValue();
            GraphVertexData data2 = (GraphVertexData) ((mxCell) o2).getValue();
            if (data1.getName().isEmpty() || data1.getName().equalsIgnoreCase(data2.getName())){
              return false;
            }
          }
        }
      }
      return true;
    }
    
    private boolean checkGraphCells(AlgorithmGraph loadedGraph){
      for (Object o : loadedGraph.getChildEdges(loadedGraph.getDefaultParent())){
        mxCell cell = (mxCell) o;
        if (cell.isVertex()){
          if (cell.getValue() instanceof GraphVertexData == false){
            return false;
          }
        } else {
          if (cell.getValue() instanceof GraphEdgeData == false){
            return false;
          }
          if (cell.getSource() == null || cell.getTarget() == null){
            return false;
          }
        }
      }
      return true;
    }
    
    @Override
		public void actionPerformed(ActionEvent e){ 
			MainPanel editor = getMainPanel(e);
      if (!editor.isModified() || JOptionPane.showConfirmDialog(
          editor, mxResources.get("loseChanges"), mxResources.get("warningSaveTitle"), 
          JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
        mxGraph graph = editor.getGraphComponent().getGraph();

        if (graph != null){
          String wd = (lastDir != null) ? lastDir : System.getProperty("user.dir");

          JFileChooser fc = new JFileChooser(wd);

          FileFilter defaultFilter = new FileFilter(){

            @Override
            public boolean accept(File file){
              return file.isDirectory() || file.getName().toLowerCase().endsWith(".xml"); 
            }

            @Override
            public String getDescription() {
              return mxResources.get("FileDesc") + " (.xml)";
            }
          };
          fc.addChoosableFileFilter(defaultFilter);
          fc.setFileFilter(defaultFilter);

          int rc = fc.showDialog(null, mxResources.get("openFile"));

          if (rc == JFileChooser.APPROVE_OPTION){
            lastDir = fc.getSelectedFile().getParent();
            try{
              AlgorithmGraph loadedGraph = GraphIO.readGraph(Paths.get(fc.getSelectedFile().getAbsolutePath()));      
              if (checkGraphNames(loadedGraph) == false){
                JOptionPane.showMessageDialog(
                  editor.getGraphComponent(),
                  mxResources.get("badVertexNameLoadError"),
                  mxResources.get("error"),
                  JOptionPane.ERROR_MESSAGE);
                return;
              }
              if (checkGraphCells(loadedGraph) == false){
                JOptionPane.showMessageDialog(
                  editor.getGraphComponent(),
                  mxResources.get("badCellLoadError"),
                  mxResources.get("error"),
                  JOptionPane.ERROR_MESSAGE);
                return;
              }
              editor.setGraph(loadedGraph);
              editor.setCurrentFile(fc.getSelectedFile());

              editor.setModified(false);
              editor.getEditorUndoManager().clear();
              editor.getGraphComponent().zoomAndCenter();
            }
            catch (IOException | SAXException | ParserConfigurationException | GraphIO.GraphIOBadXmlException ex){
              JOptionPane.showMessageDialog(
                  editor.getGraphComponent(),
                  ex.getMessage(),
                  mxResources.get("error"),
                  JOptionPane.ERROR_MESSAGE);
            }
          }
        }
      }
    }
	}
  
  /**
   * Akce pro ulozeni grafu do souboru
   */
  public static class SaveAction extends GraphComponentAction{
    protected boolean showDialog;
    protected String lastDir = null;
    
    public SaveAction(boolean showDialog){
			this.showDialog = showDialog;
		}
    
    @Override
		public void actionPerformed(ActionEvent e){
      MainPanel editor = getMainPanel(e);
      mxGraphComponent graphComponent = editor.getGraphComponent();
		  mxGraph graph = graphComponent.getGraph();
      String filename = null;
		  boolean dialogShown = false;
      if (showDialog || editor.getCurrentFile() == null){
        String wd;
        if (lastDir != null){
          wd = lastDir;
        } else if (editor.getCurrentFile() != null){
          wd = editor.getCurrentFile().getParent();
        } else {
          wd = System.getProperty("user.dir");
        }
        JFileChooser fc = new JFileChooser(wd);
        FileFilter defaultFilter = new FileFilter(){

          @Override
          public boolean accept(File file){
            return file.isDirectory() || file.getName().toLowerCase().endsWith(".xml"); 
          }

          @Override
          public String getDescription() {
            return mxResources.get("FileDesc") + " (.xml)";
          }
        };
        fc.addChoosableFileFilter(defaultFilter);
        fc.setFileFilter(defaultFilter);
        int rc = fc.showDialog(null, mxResources.get("save"));
        dialogShown = true;

        if (rc != JFileChooser.APPROVE_OPTION){
          return;
        } else{
          lastDir = fc.getSelectedFile().getParent();
        }
        
        filename = fc.getSelectedFile().getAbsolutePath();
        if (!filename.toLowerCase().endsWith(".xml"))
        {
          filename += ".xml";
        }
        if (new File(filename).exists()
							&& JOptionPane.showConfirmDialog(graphComponent,
									mxResources.get("overwriteExistingFile")) != JOptionPane.YES_OPTION){
					return;
				} 
        
      } else {
        filename = editor.getCurrentFile().getAbsolutePath();
      }
      try{
        String ext = filename.substring(filename.lastIndexOf('.') + 1);
        if (ext.equalsIgnoreCase("xml")){
          GraphIO.writeGraph(graph, Paths.get(filename));
          editor.setModified(false);
          editor.setCurrentFile(new File(filename));
        }

      } catch (IOException ex){
        JOptionPane.showMessageDialog(
            editor.getGraphComponent(),
            ex.getMessage(),
            mxResources.get("error"),
            JOptionPane.ERROR_MESSAGE);
      }
		}
	}
  
  /**
   * Akce pro vraceni o jednu editaci zpet
   */
  public static class UndoAction extends GraphComponentAction{
    
    @Override
		public void actionPerformed(ActionEvent e){
      getMainPanel(e).getEditorUndoManager().undo();
		}
	}
  
  /**
   * Akce pro opakovane vykonani jedne editace
   */
  public static class RedoAction extends GraphComponentAction{
    
    @Override
		public void actionPerformed(ActionEvent e){
      getMainPanel(e).getEditorUndoManager().redo();
		}
	}
  
  /**
   * Akce pro zobrazeni popisku u toolbaru
   */
  public static class ToolBarLabelsAction extends GraphComponentAction{
    
    @Override
		public void actionPerformed(ActionEvent e){
      
      getProgramFrame(e).showOrHideLabels();
		}
	}
  
  /**
   * Akce pro fullscreen
   */
  public static class FullScreenAction extends GraphComponentAction{
    
    @Override
		public void actionPerformed(ActionEvent e){
      Program p = getProgramFrame(e);
      p.setFullscreen(!p.isFullScreen());
		}
	}
  
  /**
   * Akce pro maximalizaci okna aplikace
   */
  public static class MaximizeAction extends GraphComponentAction{
    
    @Override
		public void actionPerformed(ActionEvent e){
      Program p = getProgramFrame(e);
      p.setMaximize(!p.isMaximize());
		}
	}
  
  /**
   * Akce pro nastaveni stylu, vyvola dialog nataveni
   */
  public static class SetStyleAction extends GraphComponentAction {
    protected StyleDialog styleDialog = null;
    
    @Override
    public void actionPerformed(ActionEvent e) {
      if (styleDialog == null){
        styleDialog = StyleDialogMaker.getStyleDialog(getProgramFrame(e), getMainPanel(e));
      } 
      if (styleDialog.isVisible() == false){
        styleDialog.setStyles(getMainPanel(e).getGraphComponent().getGraph().getStylesheet());
        styleDialog.setVisible(true);
      }
    }
    
  }
  
  /**
   * Akce pro zoom v grafu
   */
  public static class ZoomAction extends GraphComponentAction{
    
    private double zoom;

    public ZoomAction(double zoom) {
      this.zoom = zoom;
    }

    public double getZoom() {
      return zoom;
    }

    public void setZoom(double zoom) {
      this.zoom = zoom;
    }
    
    @Override
		public void actionPerformed(ActionEvent e){
      if (zoom > 0){
        mxGraphComponent mxGC = getMainPanel(e).getGraphComponent();
        mxGC.zoomTo(zoom, mxGC.isCenterZoom());
      }
		}
	}
 
  /**
   * Akce pro oznaceni uzlu jako pocatecniho
   */
  public static class SetCellSelectedAction extends GraphComponentAction{
    
    @Override
		public void actionPerformed(ActionEvent e){
      MainPanel editor = getMainPanel(e);
      mxCell selected = (mxCell) editor.getGraph().getSelectionCell();
      if (selected != null){
        if (selected.isVertex()){
          editor.getGraph().getModel().beginUpdate();
          for (Object o : editor.getGraph().getChildVertices(editor.getGraph().getDefaultParent())){
            mxCell cell = (mxCell) o;
            GraphVertexData data = (GraphVertexData) cell.getValue();
            if (cell == selected && data.getSelected() == false){
              editor.getGraph().getModel().setValue(cell, new GraphVertexData(data.getName(), true));
              editor.getGraph().getModel().setStyle(cell,  BasicStylesheet.SELECTED_VERTEX_STYLE);
            } else if (data.getSelected()){
              editor.getGraph().getModel().setValue(cell, new GraphVertexData(data.getName(), false));
              editor.getGraph().getModel().setStyle(cell,  BasicStylesheet.VERTEX_STYLE);
            }  
          }
          editor.getGraph().getModel().endUpdate();
          editor.getGraph().refresh();
        } else {
          JOptionPane.showMessageDialog(
                  editor.getGraphComponent(),
                  mxResources.get("SelectedEdgeError"),
                  mxResources.get("error"),
                  JOptionPane.ERROR_MESSAGE);
        }
      }
    }
  }
  
  /**
   * Metoda vypise dialog s chybou
   * @param editor 
   */
  private static void simulatorGraphError(MainPanel editor){
    JOptionPane.showMessageDialog(
            editor.getGraphComponent(),
            mxResources.get("SimulatorInputGraphError"),
            mxResources.get("error"),
            JOptionPane.ERROR_MESSAGE);
  }
  
  /**
   * Akce pro spusteni simulatoru BFS
   */
  public static class BfsSimulatorAction extends GraphComponentAction{
    
    private boolean isGraphOk(AlgorithmGraph clonedGraph){
      Object[] vertices = clonedGraph.getChildVertices(clonedGraph.getDefaultParent());
      if (vertices.length == 0){
        return false;
      }
      return true;
    }
    
    @Override
		public void actionPerformed(ActionEvent e){
      MainPanel editor = getMainPanel(e);
      final AlgorithmGraph clonedGraph = new AlgorithmGraph();
      mxStylesheet stylesheet = BasicStylesheet.getFromMxStylesheet(editor.getGraph().getStylesheet());
      clonedGraph.addCells(editor.getGraph().cloneCells(editor.getGraph().getChildCells(editor.getGraph().getDefaultParent())));
      clonedGraph.setStylesheet(stylesheet);
      if (isGraphOk(clonedGraph)){
        JFrame frame =  AlgorithmFrame.getAlgorithmFrame(new BfsPanel(clonedGraph), mxResources.get("BfsSimulatorFrame"));
        frame.setVisible(true);
      } else {
        simulatorGraphError(editor);
      }
		}
	}
  
  /**
   * Akce pro spusteni simulatoru DFS
   */
  public static class DfsSimulatorAction extends GraphComponentAction{
    
    private boolean isGraphOk(AlgorithmGraph clonedGraph){
      Object[] vertices = clonedGraph.getChildVertices(clonedGraph.getDefaultParent());
      if (vertices.length == 0){
        return false;
      }
      return true;
    }
    
    @Override
		public void actionPerformed(ActionEvent e){
      MainPanel editor = getMainPanel(e);
      final AlgorithmGraph clonedGraph = new AlgorithmGraph();
      mxStylesheet stylesheet = BasicStylesheet.getFromMxStylesheet(editor.getGraph().getStylesheet());
      clonedGraph.addCells(editor.getGraph().cloneCells(editor.getGraph().getChildCells(editor.getGraph().getDefaultParent())));
      clonedGraph.setStylesheet(stylesheet);
      if (isGraphOk(clonedGraph)){
        JFrame frame =  AlgorithmFrame.getAlgorithmFrame(new DfsPanel(clonedGraph), mxResources.get("DfsSimulatorFrame"));
        frame.setVisible(true);
      } else {
        simulatorGraphError(editor);
      }
		}
	}
  
  /**
   * Akce pro spusteni simulatoru SCC
   */
  public static class SccSimulatorAction extends GraphComponentAction{
    
    private boolean isGraphOk(AlgorithmGraph clonedGraph){
      Object[] vertices = clonedGraph.getChildVertices(clonedGraph.getDefaultParent());
      if (vertices.length == 0){
        return false;
      }
      return true;
    }
    
    @Override
		public void actionPerformed(ActionEvent e){
      MainPanel editor = getMainPanel(e);
      final AlgorithmGraph clonedGraph = new AlgorithmGraph();
      mxStylesheet stylesheet = BasicStylesheet.getFromMxStylesheet(editor.getGraph().getStylesheet());
      clonedGraph.addCells(editor.getGraph().cloneCells(editor.getGraph().getChildCells(editor.getGraph().getDefaultParent())));
      clonedGraph.setStylesheet(stylesheet);
      if (isGraphOk(clonedGraph)){
        JFrame frame =  AlgorithmFrame.getAlgorithmFrame(new SccPanel(clonedGraph), mxResources.get("SccSimulatorFrame"));
        frame.setVisible(true);
      } else {
        simulatorGraphError(editor);
      }
		}
	}
  
  /**
   * Akce pro spusteni simulatoru topologickeho usporadani
   */
  public static class TopologicalSortSimulatorAction extends GraphComponentAction{
    
    private boolean isGraphOk(AlgorithmGraph clonedGraph){
      Object[] vertices = clonedGraph.getChildVertices(clonedGraph.getDefaultParent());
      if (vertices.length == 0){
        return false;
      }
      return true;
    }
    
    @Override
		public void actionPerformed(ActionEvent e){
      MainPanel editor = getMainPanel(e);
      final AlgorithmGraph clonedGraph = new AlgorithmGraph();
      mxStylesheet stylesheet = BasicStylesheet.getFromMxStylesheet(editor.getGraph().getStylesheet());
      clonedGraph.addCells(editor.getGraph().cloneCells(editor.getGraph().getChildCells(editor.getGraph().getDefaultParent())));
      clonedGraph.setStylesheet(stylesheet);
      if (isGraphOk(clonedGraph)){
        mxAnalysisGraph aGraph = new mxAnalysisGraph();
        aGraph.setGraph(clonedGraph);
        if (mxGraphStructure.isCyclicDirected(aGraph)){
          int ret = JOptionPane.showConfirmDialog(
            editor.getGraphComponent(),
            mxResources.get("SimulatorTopoSortNonValidGraph"),
            mxResources.get("warning"),
            JOptionPane.YES_NO_OPTION);
          if (ret != 0){
            return;
          }
        }
        JFrame frame =  AlgorithmFrame.getAlgorithmFrame(new TopologicalSortPanel(clonedGraph), mxResources.get("TopologicalSortSimulatorFrame"));
        frame.setVisible(true);
      } else {
        simulatorGraphError(editor);
      }
		}
	}
  
  /**
   * Akce pro spusteni simulatoru Dijkstrova algoritmu
   */
  public static class DijkstraSimulatorAction extends GraphComponentAction{
    
    private boolean isGraphOk(AlgorithmGraph clonedGraph){
      Object[] vertices = clonedGraph.getChildVertices(clonedGraph.getDefaultParent());
      if (vertices.length == 0){
        return false;
      }
      return true;
    }
    
    private boolean isNegativeEdgeInGraph(AlgorithmGraph clonedGraph){
      for(Object o : clonedGraph.getChildEdges(clonedGraph.getDefaultParent())){
        GraphEdgeData edge = (GraphEdgeData) ((mxCell) o).getValue();
        if (edge.getDistance() < 0.0){
          return true;
        }
      }
      return false;
    }
    
    @Override
		public void actionPerformed(ActionEvent e){
      MainPanel editor = getMainPanel(e);
      final AlgorithmGraph clonedGraph = new AlgorithmGraph();
      mxStylesheet stylesheet = BasicStylesheet.getFromMxStylesheet(editor.getGraph().getStylesheet());
      clonedGraph.addCells(editor.getGraph().cloneCells(editor.getGraph().getChildCells(editor.getGraph().getDefaultParent())));
      clonedGraph.setStylesheet(stylesheet);
      if (isGraphOk(clonedGraph)){
        if (isNegativeEdgeInGraph(clonedGraph)){
          int ret = JOptionPane.showConfirmDialog(
            editor.getGraphComponent(),
            mxResources.get("SimulatorDijkstraNonValidGraph"),
            mxResources.get("warning"),
            JOptionPane.YES_NO_OPTION);
          if (ret != 0){
            return;
          }  
        }
        JFrame frame = AlgorithmFrame.getAlgorithmFrame(new DijkstraPanel(clonedGraph), mxResources.get("DijkstraSimulatorFrame"));
        frame.setVisible(true);
      } else {
        simulatorGraphError(editor);
      }
		}
	}
  
  /**
   * Akce pro spusteni simulatoru Bellman-Fordova algoritmu
   */
  public static class BellmanFordSimulatorAction extends GraphComponentAction{
    
    private boolean isGraphOk(AlgorithmGraph clonedGraph){
      Object[] vertices = clonedGraph.getChildVertices(clonedGraph.getDefaultParent());
      if (vertices.length == 0){
        return false;
      }
      return true;
    }
    
    @Override
		public void actionPerformed(ActionEvent e){
      MainPanel editor = getMainPanel(e);
      final AlgorithmGraph clonedGraph = new AlgorithmGraph();
      mxStylesheet stylesheet = BasicStylesheet.getFromMxStylesheet(editor.getGraph().getStylesheet());
      clonedGraph.addCells(editor.getGraph().cloneCells(editor.getGraph().getChildCells(editor.getGraph().getDefaultParent())));
      clonedGraph.setStylesheet(stylesheet);
      if (isGraphOk(clonedGraph)){
        JFrame frame =  AlgorithmFrame.getAlgorithmFrame(new BellmanFordPanel(clonedGraph), mxResources.get("BellmanFordSimulatorFrame"));
        frame.setVisible(true);
      } else {
        simulatorGraphError(editor);
      }
		}
	}
  
  /**
   * Akce pro schovani obsahu hran
   */
  public static class HideEdgeLabelAction extends GraphComponentAction{
    
    @Override
		public void actionPerformed(ActionEvent e){
      GraphViewPanel graphView = getMainPanel(e).getGraphPanel();
      graphView.setEdgeLabel(!graphView.isEdgeLabel());
		}
	}
  
  /**
   * Akce pro schovani obsahu vrcholu
   */
  public static class HideVertexLabelAction extends GraphComponentAction{
    
    @Override
		public void actionPerformed(ActionEvent e){
      GraphViewPanel graphView = getMainPanel(e).getGraphPanel();
      graphView.setVertexLabel(!graphView.isVertexLabel());
		}
	}
  
  /**
   * Akce pro znovu vykresleni paralelnich hran
   */
  public static class ParallelEdgeLabelAction extends GraphComponentAction{
    
    @Override
		public void actionPerformed(ActionEvent e){
      GraphViewPanel graphView = getMainPanel(e).getGraphPanel();
      graphView.doParallelEdgeLayout();
		}
	}
}
