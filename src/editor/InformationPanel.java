package editor;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import simulator.graphs.GraphVertexBasicData;
import simulator.gui.VariablesPanel.VariablesTableModel;

/**
 * Informaci panel poskytuje zapis grafu v tabulkove podobe
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class InformationPanel extends JPanel{
  
    public static String dequeToString(Deque<mxCell> q){
      if (q == null){
        return "[]";
      }
      Iterator<mxCell> it = q.iterator();
      if (! it.hasNext()){
          return "[]";
      }

      StringBuilder sb = new StringBuilder();
      sb.append('[');
      for (;;) {
        mxCell node = it.next();
        if (node.isVertex()){
          sb.append(((GraphVertexBasicData) node.getValue()).getName());
        } else {
          mxCell source = (mxCell) node.getSource();
          mxCell target = (mxCell) node.getTarget();
          sb.append('(');
          sb.append(((GraphVertexBasicData) source.getValue()).getName());
          sb.append(", ");
          sb.append(((GraphVertexBasicData) target.getValue()).getName());
          sb.append(')');
        }
        if (! it.hasNext()){
          return sb.append(']').toString();
        }
        sb.append(',').append(' ');
      }
    }
    
    public InformationPanel(final GraphViewPanel graphPanel) {
      this.setLayout(new GridLayout(1,0));
      JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
      final VariablesTableModel tableModelNodeMatrix = new VariablesTableModel(new String[]{"Uzel", "Seznam sousedů"});
      final JTable nodeMatrixTable = new JTable(tableModelNodeMatrix);
      nodeMatrixTable.addKeyListener(new KeyListener(){

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
            ((JTable)e.getSource()).clearSelection();
          }
          if (e.getKeyCode() == KeyEvent.VK_DELETE){
            if (nodeMatrixTable.getSelectedRow() >= 0){
              String name = tableModelNodeMatrix.getValueAt(nodeMatrixTable.getSelectedRow(), 0).toString();
              for (Object o : graphPanel.getGraph().getChildVertices(graphPanel.getGraph().getDefaultParent())){
                mxCell vertex = (mxCell) o;
                String vertexName = ((GraphVertexBasicData) vertex.getValue()).getName();
                if (name.equals(vertexName)){
                  graphPanel.getGraph().removeCells(new Object[]{o});
                  break;
                }   
              }
            }
          }
        }

      });
      nodeMatrixTable.getColumnModel().getColumn(0).setMinWidth(50);
      DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
      centerRenderer.setHorizontalAlignment(JLabel.CENTER);
      nodeMatrixTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
      nodeMatrixTable.getColumnModel().getColumn(1).setMinWidth(90);
      nodeMatrixTable.getColumnModel().getColumn(1).setPreferredWidth(Short.MAX_VALUE);
      Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
      nodeMatrixTable.setFont(f);
      nodeMatrixTable.setRowHeight(nodeMatrixTable.getFontMetrics(f).getHeight());

      graphPanel.getGraph().addListener(null, new mxEventSource.mxIEventListener(){
        
        @Override
        public void invoke(Object sender, mxEventObject evt) {
          switch (evt.getName()){
            case mxEvent.REPAINT:
            //case mxEvent.LABEL_CHANGED:
            //case mxEvent.CELLS_REMOVED: 
            //case mxEvent.CELLS_ADDED: 
              tableModelNodeMatrix.getData().clear();
              for (Object o : graphPanel.getGraph().getChildVertices(graphPanel.getGraph().getDefaultParent())){
                mxCell vertex = (mxCell) o;
                String name = ((GraphVertexBasicData) vertex.getValue()).getName();
                Deque<mxCell> adjList = new LinkedList<>();
                for (int i = 0; i < graphPanel.getGraph().getModel().getEdgeCount(vertex); i++){
                  mxCell edge = (mxCell) ((mxCell) graphPanel.getGraph().getModel().getEdgeAt(vertex, i));
                  mxCell source = (mxCell) edge.getSource();
                  mxCell target = (mxCell) edge.getTarget();
                  if (source == vertex && adjList.contains(target) == false){
                    adjList.addFirst(target);
                  } else if (graphPanel.getGraph().isOriented() == false && target == vertex && adjList.contains(source) == false){
                    adjList.addFirst(source);
                  }
                }
                Map<String, String> newRow = new HashMap<>();
                newRow.put(tableModelNodeMatrix.getColumnName(0), name);
                newRow.put(tableModelNodeMatrix.getColumnName(1), dequeToString(adjList));
                tableModelNodeMatrix.getData().put(name, newRow);
              } 
              tableModelNodeMatrix.fireTableDataChanged();
              break;  
          }
        }
      });
      nodeMatrixTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

        @Override
        public void valueChanged(ListSelectionEvent e) {
          if (nodeMatrixTable.getSelectedRow() >= 0){
            String name = tableModelNodeMatrix.getValueAt(nodeMatrixTable.getSelectedRow(), 0).toString();
            for (Object o : graphPanel.getGraph().getChildVertices(graphPanel.getGraph().getDefaultParent())){
              mxCell vertex = (mxCell) o;
              String vertexName = ((GraphVertexBasicData) vertex.getValue()).getName();
              if (name.equals(vertexName)){
                graphPanel.getGraph().setSelectionCell(o);
                break;
              }   
            }
          }
        }
        
      });
      JPanel tablePanel = new JPanel(new GridLayout(0,1));
      tablePanel.add(new JScrollPane(nodeMatrixTable));
      splitPane.setTopComponent(tablePanel);
      JPanel graphOutlinePanel = new JPanel(new GridLayout(0,1));
      graphOutlinePanel.add(graphPanel.getGraphOutline());
      splitPane.setBottomComponent(graphOutlinePanel);
      splitPane.setContinuousLayout(true);
      this.add(splitPane);
    }
  
}
