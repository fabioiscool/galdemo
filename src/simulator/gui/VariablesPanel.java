package simulator.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicToolBarUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

/**
 * Panel promennych pro simulator
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class VariablesPanel extends JPanel{
  
  class VariablesToolBarUI extends BasicToolBarUI{
    private JDialog dialog;
    private Dimension toolBarPreferredSize;
    private Dimension newBarPreferredSize = new Dimension(300,100);

    public void returnPanel(){
      toolBar.setPreferredSize(toolBarPreferredSize);
      this.setFloating(false, null);
    }
    
    @Override
    protected RootPaneContainer createFloatingWindow(JToolBar toolbar) {
      Window window = SwingUtilities.getWindowAncestor(toolbar);
      /*if (window instanceof Frame) {
        dialog = new JDialog((Frame)window, toolbar.getName(), false);
      } else if (window instanceof Dialog) {
        dialog = new JDialog((Dialog)window, toolbar.getName(), false);
      } else {*/
        dialog = new JDialog((Frame)null, toolbar.getName(), false);
      //}

      dialog.getRootPane().setName("ToolBar.FloatingWindow");
      dialog.setTitle(toolbar.getName());
      dialog.setResizable(true); 
      dialog.setAlwaysOnTop(false);
      dialog.addWindowListener(new WindowAdapter(){

        @Override
        public void windowClosing(WindowEvent e) {
          toolBar.setPreferredSize(toolBarPreferredSize);
        }

      });
      WindowListener wl = createFrameListener();
      dialog.addWindowListener(wl);
      return dialog;
    }

    @Override
    protected void dragTo(Point position, Point origin) {
      if (this.toolBar.getPreferredSize().equals(newBarPreferredSize) == false){
        toolBarPreferredSize = this.toolBar.getPreferredSize();
        this.toolBar.setPreferredSize(newBarPreferredSize);
      }
      super.dragTo(position, origin);
    }
  }
  
  public static class VariablesTableModel extends DefaultTableModel {
    private String[] columnNames;
    private Map<String, Map<String, String>> data = new LinkedHashMap<>();

    public VariablesTableModel(String[] columnNames) {
      this.columnNames = columnNames;
    }

    public Map<String, Map<String, String>> getData() {
      return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
      this.data = data;
    }
    
    @Override
    public boolean isCellEditable(int row, int col){ 
      if (col == 1){
        return true; 
      }
      return false;
    }

    @Override
    public int getRowCount() {
      return (data == null) ? 0 : data.size();
    }

    @Override
    public int getColumnCount() {
      return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      int count = 0;
      for (Map<String, String> i : data.values()){
        if (count == rowIndex){
          return i.get(columnNames[columnIndex]);
        }
        count++;
      }
      return null;
    }

    @Override
    public String getColumnName(int col) {
      return columnNames[col];
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
    }
  }
  
  private JTable varTable;
  private JTable nodeMatrixTable;
  private VariablesTableModel tableModelVar;
  private VariablesTableModel tableModelNodeMatrix;
  private JTextField textField = new JTextField();
  private JToolBar toolBar1;
  private JToolBar toolBar2; 
  
  public class MyTableCellEditor extends DefaultCellEditor implements TableCellEditor {

    public MyTableCellEditor() {
      super(textField);
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
      JTextField textField = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, rowIndex, WIDTH);
      textField.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
      textField.setEditable(false);
      //textField.setBackground(table.getSelectionBackground());
      return textField;
    }

    @Override
    public Object getCellEditorValue() {
      return null;
    }
    
    @Override
    public void cancelCellEditing() {
      super.cancelCellEditing();
    }
  }

  public VariablesPanel() {
    this.setLayout(new BorderLayout());
    tableModelVar = new VariablesTableModel(new String[]{"Jméno proměnné", "Hodnota"});
    varTable = new JTable(tableModelVar);
    varTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tableModelNodeMatrix = new VariablesTableModel(new String[]{"Uzel", "Seznam sousedů"});
    nodeMatrixTable = new JTable(tableModelNodeMatrix);
    //JPanel toolsBarPanel = new JPanel();
    toolBar1 = new JToolBar();
    toolBar1.setRollover(false);
    toolBar1.setUI(new VariablesToolBarUI());
    toolBar2 = new JToolBar();
    toolBar2.setRollover(false);
    toolBar2.setUI(new VariablesToolBarUI());
    toolBar1.add(new JScrollPane(varTable));
    toolBar2.add(new JScrollPane(nodeMatrixTable));
    Box box = Box.createVerticalBox();
    JPanel tp1 = new JPanel(new GridLayout(1,0));
    tp1.add(toolBar1);
    box.add(tp1);
    box.add(Box.createVerticalStrut(5));
    JPanel tp2 = new JPanel(new GridLayout(1,0));
    tp2.add(toolBar2);
    box.add(tp2);
    this.add(box, BorderLayout.CENTER);
    tablesSetting(varTable, tableModelVar);
    tablesSetting(nodeMatrixTable, tableModelNodeMatrix);
  }

  private void tablesSetting(final JTable table, final VariablesTableModel model){
    table.setSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
    table.getColumnModel().getColumn(1).setCellEditor(new MyTableCellEditor());
    table.getColumnModel().getColumn(0).setMinWidth(95);
    table.getColumnModel().getColumn(1).setMinWidth(95);
    table.getColumnModel().getColumn(1).setPreferredWidth(Short.MAX_VALUE);
    Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
    table.setFont(f);
    table.setRowHeight(table.getFontMetrics(f).getHeight());
    model.addTableModelListener(new TableModelListener(){
      @Override
      public void tableChanged(TableModelEvent e) {
        if (model.getValueAt(table.getSelectedRow(), table.getSelectedColumn()) != null){
          textField.setText(model.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString());
        }
      }    
    });
    table.addKeyListener(new KeyListener(){

      @Override
      public void keyTyped(KeyEvent e) {}

      @Override
      public void keyPressed(KeyEvent e) {}

      @Override
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
          ((JTable)e.getSource()).clearSelection();
        }
      }
      
    });
  }
  
  public void dockTables(){   
    ((VariablesToolBarUI) toolBar1.getUI()).returnPanel();
    ((VariablesToolBarUI) toolBar2.getUI()).returnPanel();
  }
  
  public void addNodeAdjList(String nodeName, String adjList){
    addData(tableModelNodeMatrix, nodeName, adjList);
  }
  
  public void addVariable(String name){
    addVariable(name, null);
  }
  
  public void addVariable(String name, String val){
    addData(tableModelVar, name, val);
  }
  
  private void addData(VariablesTableModel model, String data1, String data2){
    if (model.getData().containsKey(data1)){
      model.getData().get(data1).put(model.getColumnName(1), data2);
    } else {
      Map<String, String> newRow = new HashMap<>();
      newRow.put(model.getColumnName(0), data1);
      newRow.put(model.getColumnName(1), data2);
      model.getData().put(data1, newRow);
    }
    model.fireTableDataChanged();
  }
  
  public void removeVariable(String name){
    if (tableModelVar.getData().containsKey(name)){
      tableModelVar.getData().remove(name);
      tableModelVar.fireTableDataChanged();
    }
  }
}
