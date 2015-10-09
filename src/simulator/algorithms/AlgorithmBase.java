package simulator.algorithms;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * Trida pro zakladni tridy a styly k algoritmum. 
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class AlgorithmBase {
  public static final String algorithmNumLineAttribut = "lineNum";
  public static final String algorithmRealLineAttribut = "line";
  
  public static final String algorithmHeadingName = "AlgorithmHeading";
  public static final String algorithmLineName0 = "AlgorithmLine0";
  public static final String algorithmLineName1 = "AlgorithmLine1";
  public static final String algorithmLineName2 = "AlgorithmLine2";
  public static final String algorithmLineName3 = "AlgorithmLine3";
  public static final String algorithmLineName4 = "AlgorithmLine4";
  public static final String algorithmLineName5 = "AlgorithmLine5";
  
  public static final String algorithmName = "AlgorithmName";
  public static final String algorithmKeywordsName = "AlgorithmKeywords";
  public static final String algorithmVariableName = "AlgorithmVariable";
  public static final String algorithmMethodsName = "AlgorithmMethods";
  public static final String algorithmOthersName = "AlgorithmOthers";
  public static final String algorithmSuperscriptName = "AlgorithmSuperscript";
  
  protected StyleContext algorithmsStyleContext = new StyleContext();

  public AlgorithmBase() {
    makeStyle();
  }
  
  /**
   * Vytvori predvolene styly
   */
  private void makeStyle(){
    Style defaultStyle = algorithmsStyleContext.getStyle(StyleContext.DEFAULT_STYLE);
    Style mainStyle = algorithmsStyleContext.addStyle(algorithmHeadingName, defaultStyle);
    StyleConstants.setFontFamily(mainStyle, "serif");
    StyleConstants.setFontSize(mainStyle, 22);
    
    Style lineStyle0 = algorithmsStyleContext.addStyle(algorithmLineName0, defaultStyle);
    StyleConstants.setFontFamily(lineStyle0, "serif");
    StyleConstants.setFontSize(lineStyle0, 18);
    StyleConstants.setLeftIndent(lineStyle0, 15);
    
    Style lineStyle1 = algorithmsStyleContext.addStyle(algorithmLineName1, lineStyle0);
    StyleConstants.setLeftIndent(lineStyle1, 30);
    Style lineStyle2 = algorithmsStyleContext.addStyle(algorithmLineName2, lineStyle0);
    StyleConstants.setLeftIndent(lineStyle2, 45);
    Style lineStyle3 = algorithmsStyleContext.addStyle(algorithmLineName3, lineStyle0);
    StyleConstants.setLeftIndent(lineStyle3, 60);
    
    Style mainNameStyle = algorithmsStyleContext.addStyle(algorithmName, defaultStyle);
    StyleConstants.setBold(mainNameStyle, true);
    
    Style keywordsStyle = algorithmsStyleContext.addStyle(algorithmKeywordsName, defaultStyle);
    StyleConstants.setBold(keywordsStyle, true);
    
    Style variablesStyle = algorithmsStyleContext.addStyle(algorithmVariableName, defaultStyle);
    StyleConstants.setItalic(variablesStyle, true);
    
    Style methodsStyle = algorithmsStyleContext.addStyle(algorithmMethodsName, defaultStyle);

    
    Style othersStyle = algorithmsStyleContext.addStyle(algorithmOthersName, defaultStyle);
    
    Style superscriptStyle = algorithmsStyleContext.addStyle(algorithmSuperscriptName, defaultStyle);
    StyleConstants.setSuperscript(superscriptStyle, true);
    
  }
  
  /**
   * Vraci styl podle jeho jmena
   * @param styleName
   * @return 
   */
  public Style getAlgorithmsStyle(String styleName) {
    return algorithmsStyleContext.getStyle(styleName);
  }

  /**
   * Trida pro obsah radku algoritmu.
   */
  public static class AlgorithmContent {
    protected String styleName;
    protected String content;
    
    public AlgorithmContent(String styleName, String content) {
      this.styleName = styleName;
      this.content = content;
    }

    public String getStyleName() {
      return styleName;
    }

    public String getContent() {
      return content;
    }
    
  }
  
  /**
   * Trida predstavujici jeden element algoritmu - jeden radek
   */
  public static class AlgorithmElement {
    protected String styleName;
    protected AlgorithmContent[] contentArray;
    
    public AlgorithmElement(String styleName, AlgorithmContent[] contentArray) {
      this.styleName = styleName;
      this.contentArray = contentArray;
    }

    public String getStyleName() {
      return styleName;
    }

    public AlgorithmContent[] getContentArray() {
      return contentArray;
    }
  }
}
