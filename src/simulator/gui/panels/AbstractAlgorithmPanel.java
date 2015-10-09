package simulator.gui.panels;

import javax.swing.JPanel;

/**
 * Rozhrani simulatoru s metodou pro zastaveni simulatoru
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public abstract class AbstractAlgorithmPanel extends JPanel{
  public abstract void stop();
}
