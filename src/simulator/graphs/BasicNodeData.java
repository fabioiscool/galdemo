package simulator.graphs;

/**
 * Data ulozena v objektu, ktery je ulozeny v grafu.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class BasicNodeData implements NodeData {
  protected boolean enabled = false;
  protected boolean toolTipsEnabled = false;
  protected boolean active = false;
  
  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean isActive() {
    return active;
  }

  @Override
  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public void setToolTipsEnabled(boolean toolTipsEnabled) {
    this.toolTipsEnabled = toolTipsEnabled;
  }

  @Override
  public boolean isToolTipsEnabled() {
    return toolTipsEnabled;
  }

  @Override
  public void resetState() {
    enabled = false;
    toolTipsEnabled = false;
    active = false;
  }
  
}
