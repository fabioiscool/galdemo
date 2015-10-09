package simulator.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;

/**
 * Panel ovladani simulatoru
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class ControlPanel extends JPanel{

  public static final int MAX_TIME = 6;
  public static final int MIN_TIME = 0;
  public static final int STANDARD_TIME = 4;
   
  public final static int MAX_DELAY = 10000;
  public final static int MIN_DELAY = 0;
  
  protected JButton backButton = new JButton();
  protected JButton playButton = new JButton();
  protected JButton resetButton = new JButton();
  protected JButton forwardButton = new JButton();
  protected JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, MIN_TIME, MAX_TIME, STANDARD_TIME); 
  
  public ControlPanel() { 
    super(new BorderLayout());

    speedSlider.setMajorTickSpacing(1);
    speedSlider.setPaintTicks(true);
    speedSlider.setPaintLabels(true);
    speedSlider.setSnapToTicks(true);
    speedSlider.setInverted(true);
    Enumeration e = speedSlider.getLabelTable().keys();
    int speed = 1;
    while (e.hasMoreElements()) {
      Integer i = (Integer) e.nextElement();
      JLabel label = (JLabel) speedSlider.getLabelTable().get(i);
      if (i.intValue() == MIN_TIME){
        label.setText("Max");
      } else {
        label.setText(speed + "x");
        speed = speed * 2;
      }
    }
    speedSlider.setPaintLabels(true);
    JToolBar toolBar = new JToolBar();
    toolBar.setName("Ovládání Simulace Algoritmu");
    toolBar.setLayout(new GridBagLayout());
    speedSlider.setFocusable(false); 
    backButton.setFocusable(false);
    backButton.setHideActionText(true);
    backButton.setBorderPainted(false);
    playButton.setFocusable(false);
    playButton.setHideActionText(true);
    playButton.setBorderPainted(false);
    //iconButton(playButton);
    resetButton.setFocusable(false);
    resetButton.setHideActionText(true);
    resetButton.setBorderPainted(false);
    forwardButton.setFocusable(false);
    forwardButton.setHideActionText(true);
    forwardButton.setBorderPainted(false);
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;
    toolBar.add(backButton, c);
    c.gridx = 1;
    toolBar.add(resetButton, c);
    c.gridx = 2;
    toolBar.add(playButton, c);
    c.gridx = 3;
    toolBar.add(forwardButton, c);
    c.gridx = 0;
    c.gridy = 1;
    c.gridwidth = 4;
    toolBar.add(speedSlider, c);
    toolBar.setRollover(false);
    this.add(toolBar, BorderLayout.LINE_START);
  }
  
  public void setButtonsEnabled(boolean enabled){
    backButton.setEnabled(enabled);
    playButton.setEnabled(enabled);
    resetButton.setEnabled(enabled);
    forwardButton.setEnabled(enabled);
  }
  
  public void setAllEnabled(boolean enabled){
    speedSlider.setEnabled(enabled);
    setButtonsEnabled(enabled);
  }
  
  public AbstractButton getBackButton() {
    return backButton;
  }

  public AbstractButton getPlayButton() {
    return playButton;
  }

  public AbstractButton getResetButton() {
    return resetButton;
  }

  public AbstractButton getForwardButton() {
    return forwardButton;
  }

  public JSlider getSpeedSlider() {
    return speedSlider;
  }
  
  public static int getDelayBySliderValue(int sliderValue, int max, int min) {
    if (sliderValue == 0){
      return 0;
    }
    sliderValue = (int) Math.pow(2, max - sliderValue);
    int delay = ((MAX_DELAY - MIN_DELAY) / sliderValue);
    return delay;
  }
  
  public static int getStandardTime(){
    return getDelayBySliderValue(STANDARD_TIME, MAX_TIME, MIN_TIME);
  }
}
