package program.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Trida provadi logovani akci v editoru
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class ActionLogger extends AbstractAction{
  private static Logger logger = LogManager.getLogger(ActionLogger.class.getSimpleName());
  protected Action action;
  
  public ActionLogger(Action a) {
    if (a == null){
      throw new NullPointerException();
    }
    
    action = a;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    assert e != null;
    
    logger.entry(action, e);
    action.actionPerformed(e);
    logger.exit();
  }
  
}
