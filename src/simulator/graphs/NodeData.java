package simulator.graphs;

import java.io.Serializable;

/**
 * Rozhrani pro data umistené v objektu pro grafy
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public interface NodeData extends Serializable{
  public boolean isEnabled();
  public void setEnabled(boolean enabled);
  public boolean isActive();
  public void setActive(boolean active);
  public void resetState();
  public void setToolTipsEnabled(boolean toolTipsEnabled);
  public boolean isToolTipsEnabled();
}
