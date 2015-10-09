package program.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import program.MainPanel;
import program.Program;

/**
 * Trida pro akce poskytujici zakladni metody na ziskani potrebnych prvku
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public abstract class GraphComponentAction extends AbstractAction{
   
  protected static Component getComponentFromActionEvent(ActionEvent e) {
    if (e.getSource() instanceof Component) {
      return (Component) e.getSource();
    }
    return null;
  }
  
  public static Program getProgramFrame(ActionEvent e) {
    Component component = getComponentFromActionEvent(e);
    if (component != null) {
      return (Program) SwingUtilities.windowForComponent((Component) e.getSource());
    }
    return null;
  }
  
  public static MainPanel getMainPanel(ActionEvent e) {
		Component component = getComponentFromActionEvent(e);
    if (component != null){
			while (component != null
					&& !(component instanceof MainPanel)) {
				component = component.getParent();
			}
      if (component instanceof MainPanel) {
			  return (MainPanel) component;
      }   
		}
		return null;
	}
  
}
