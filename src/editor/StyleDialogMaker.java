package editor;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import program.MainPanel;
import simulator.graphs.BasicStylesheet;

/**
 * Trida poskytuje metodu pro vyvtoreni dialogu na zmenu stylu
 * @author JVaradinek
 */
public class StyleDialogMaker {

  /**
   * Rozsirena trida JDialog 
   */
  public static class StyleDialog extends JDialog {
    private mxGraph graph;
    
    public StyleDialog(Frame owner, boolean modal, mxGraph graph) {
      super(owner, modal);
      this.graph = graph;
    }

    public void setStyles(mxStylesheet sheet) {
      graph.setStylesheet(BasicStylesheet.getFromMxStylesheet(sheet));
      graph.refresh();
    }

  }
  
  private StyleDialogMaker() {
  }
  
  /**
   * @param frame okno editoru
   * @param editor
   * @return Metoda vrati dialog pro editor
   */
  public static StyleDialog getStyleDialog(JFrame frame, final MainPanel editor){
    final mxGraph graph = new mxGraph();
    graph.setStylesheet(BasicStylesheet.getFromMxStylesheet(editor.getGraph().getStylesheet()));
    final StyleDialog styleDialog = new StyleDialog(frame, true, graph); 
    mxGraphComponent comp = new mxGraphComponent(graph);
    comp.setDragEnabled(false);
    graph.setAllowDanglingEdges(false);
    graph.setAllowNegativeCoordinates(false);
    graph.setCellsDeletable(false);
    graph.setCellsEditable(false);
    graph.setCellsDisconnectable(false);
    comp.getViewport().setBackground(Color.WHITE);
    comp.setBackground(Color.WHITE);
    Set<String> availableStyles = BasicStylesheet.getAllStyles();
    final Object v1 = graph.insertVertex(graph.getDefaultParent(), null, "Style", 20, 50, 80, 80, BasicStylesheet.VERTEX_STYLE);
    final Object v2 = graph.insertVertex(graph.getDefaultParent(), null, "V Style", 300, 50, 80, 80, BasicStylesheet.VERTEX_STYLE);
    final Object edge = graph.insertEdge(graph.getDefaultParent(), null, "100", v1, v2, BasicStylesheet.EDGE_STYLE);
    graph.refresh(); 
    availableStyles.remove("defaultVertex");
    availableStyles.remove("defaultEdge");
    JPanel maniPanel = new JPanel(new BorderLayout());
    final JPanel settingsPanel = new JPanel(new BorderLayout());
    final JTextField styleField = new JTextField();
    final JComboBox<String> styleChooser = new JComboBox<>(availableStyles.toArray(new String[0]));
    styleField.getDocument().addDocumentListener(new DocumentListener() {
      
      @Override
      public void changedUpdate(DocumentEvent e) {
        func();
      }
      @Override
      public void removeUpdate(DocumentEvent e) {
        func();
      }
      @Override
      public void insertUpdate(DocumentEvent e) {
        func();
      }

      public void func() {
        //System.out.println(editor.getGraph().getStylesheet().getStyles().get(styleChooser.getSelectedItem().toString()));
        Map<String, Map<String, Object>> styles = graph.getStylesheet().getStyles();
        styles.put(styleChooser.getSelectedItem().toString(), BasicStylesheet.stringToStyle(styleField.getText()));
        graph.refresh();
      }
    });
    styleChooser.setToolTipText(BasicStylesheet.getDesc(styleChooser.getSelectedItem().toString()));
    if (BasicStylesheet.isVertexStyle(styleChooser.getSelectedItem().toString())){
      ((mxCell) v1).setStyle(styleChooser.getSelectedItem().toString());
    } else if (BasicStylesheet.isEdgeStyle(styleChooser.getSelectedItem().toString())){
      ((mxCell) edge).setStyle(styleChooser.getSelectedItem().toString());
    }
    styleChooser.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent e) {
        String style = styleChooser.getSelectedItem().toString();
        ((mxCell) v1).setStyle(BasicStylesheet.VERTEX_STYLE);
        ((mxCell) edge).setStyle(BasicStylesheet.EDGE_STYLE);
        if (BasicStylesheet.isVertexStyle(style)){
          ((mxCell) v1).setStyle(style);
        } else if (BasicStylesheet.isEdgeStyle(style)){
          ((mxCell) edge).setStyle(style);
        }
        styleChooser.setToolTipText(BasicStylesheet.getDesc(style));
        styleField.setText(BasicStylesheet.styleToString(graph.getStylesheet().getStyles().get(style)));
        graph.refresh();
      }
      
    });
    styleField.setText(BasicStylesheet.styleToString(graph.getStylesheet().getStyles().get(styleChooser.getSelectedItem().toString())));
    settingsPanel.add(styleChooser, BorderLayout.WEST);
    settingsPanel.add(styleField,BorderLayout.CENTER);
    maniPanel.add(settingsPanel, BorderLayout.NORTH);
    maniPanel.add(comp, BorderLayout.CENTER);
    JPanel buttonPanel = new JPanel(new FlowLayout());
    JButton reset = new JButton("Reset");
    reset.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent e) {
        graph.setStylesheet(BasicStylesheet.getFromMxStylesheet(editor.getGraph().getStylesheet()));
        styleField.setText(BasicStylesheet.styleToString(graph.getStylesheet().getStyles().get(styleChooser.getSelectedItem().toString())));
        graph.refresh();
      }
      
    });
    JButton save = new JButton("Save");  
    save.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent e) {
        editor.getGraph().setStylesheet(BasicStylesheet.getFromMxStylesheet(graph.getStylesheet()));
        editor.getGraph().refresh();
        styleDialog.setVisible(false);
      }
      
    });
    buttonPanel.add(reset);
    buttonPanel.add(save);
    maniPanel.add(buttonPanel, BorderLayout.SOUTH);
    styleDialog.getContentPane().add(maniPanel);
    styleDialog.setResizable(true); 
    styleDialog.setSize(450, 300);
    styleDialog.setLocationRelativeTo(editor.getGraphComponent());
    return styleDialog;
  }
  
}
