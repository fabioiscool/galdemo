package simulator.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import simulator.algorithms.AlgorithmBase.AlgorithmContent;
import simulator.algorithms.AlgorithmBase.AlgorithmElement;

/**
 * Abstraktni trida pro algoritmus, poskytuje zakladni metody pro prevod do podoby 
 * pro upraveny panel pseudokódu.Dochazi, zde k prevodu do podoby dokumentu a 
 * k predpocitani cisel radku.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public abstract class AbstractAlgorithm extends DefaultStyledDocument{
  protected AlgorithmBase algorithmBase = new AlgorithmBase();
  protected String algorithmName;
  protected List<AlgorithmElement[]> algorithmParts = new ArrayList<>();
  protected ArrayList<ElementSpec> batch;
  protected int lineNumberCount = 0;
  
  /**
   * Vztvori element pro zacatek
   * @param attr
   * @param direction
   * @return 
   */
  protected ElementSpec createSpecStart(AttributeSet attr, short direction){
    ElementSpec spec = new ElementSpec(attr, ElementSpec.StartTagType);
    spec.setDirection(direction);
    return spec;
  }

  /**
   * Vytvori element pro konec
   * @return 
   */
  protected ElementSpec createSpecEnd(){
    ElementSpec spec = new ElementSpec(null, ElementSpec.EndTagType);
    return spec;
  }

  /**
   * Vytvori element s obsahem
   * @param attr
   * @param str
   * @param direction
   * @return 
   */
  protected ElementSpec createContent(AttributeSet attr, String str, short direction){
    ElementSpec spec = new ElementSpec(attr,ElementSpec.ContentType,str.toCharArray(), 0, str.length());
    spec.setDirection(direction);
    return spec;
  }
  
  /**
   * Prida prazny radek
   */
  protected void addEmtyLine(){
    SimpleAttributeSet as = new SimpleAttributeSet();
    as.addAttribute(AlgorithmBase.algorithmNumLineAttribut, 0);
    as.addAttribute(AlgorithmBase.algorithmRealLineAttribut, 0);
    as.addAttributes(algorithmBase.getAlgorithmsStyle(AlgorithmBase.algorithmHeadingName));
    batch.add(createSpecStart(as, ElementSpec.OriginateDirection));
    batch.add(createContent(SimpleAttributeSet.EMPTY, "\n", ElementSpec.OriginateDirection));
    batch.add(createSpecEnd());
  }
  
  /**
   * Prida cely radek se zadanym obsahem
   * @param lineContent Obsah radku
   */
  protected void addLine(AlgorithmContent[] lineContent){
    for (AlgorithmContent ac : lineContent){
      AttributeSet algStyle = ac.getStyleName() == null ? 
                              SimpleAttributeSet.EMPTY : 
                              algorithmBase.getAlgorithmsStyle(ac.getStyleName()); 
      batch.add(createContent(algStyle, ac.getContent(), ElementSpec.OriginateDirection));
    }
    batch.add(createContent(null, "\n", ElementSpec.OriginateDirection));
  }
  
  /**
   * Prida dalsi podalgoritmus celeho algoritmu.
   * @param algorithmPart Pole predstavujici zapis podalgoritmu
   * @param realLine oznacuje zacatek podle cisla radku.
   * @return 
   */
  protected int addAlgorithmPart(AlgorithmElement[] algorithmPart, int realLine){
    int line = 0;
    for (AlgorithmElement element : algorithmPart){
      realLine++;
      SimpleAttributeSet as = new SimpleAttributeSet();
      as.addAttribute(AlgorithmBase.algorithmNumLineAttribut, line);
      as.addAttribute(AlgorithmBase.algorithmRealLineAttribut, realLine);
      if (element.getStyleName() != null){
        as.addAttributes(algorithmBase.getAlgorithmsStyle(element.getStyleName()));
      }
      batch.add(createSpecStart(as, ElementSpec.OriginateDirection));
      addLine(element.getContentArray());
      batch.add(createSpecEnd());
      line++;
    }
    return realLine;
  }
  
  /**
   * Provedeni pridani vsech casti algoritmu, predpoklada se naplneni pole casti
   * prisalusnym potomkem.
   */
  protected void addAlgorithm(){
    batch = new ArrayList<>();
    batch.add(createSpecEnd());  
    int realLine = 0;
    for (AlgorithmElement[] algorithmPart : algorithmParts){
      realLine = addAlgorithmPart(algorithmPart, realLine);
      addEmtyLine();
    }
    ElementSpec[] inserts = new ElementSpec[batch.size()];
    batch.toArray(inserts);
    try { 
      this.insert(0, inserts);
    } catch (BadLocationException ex) {
      Logger.getLogger(AbstractAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
    }
    lineNumberCount = realLine;
  }
  
  /**
   * @param realLine
   * @return Vraci predchozi cislo radku
   */
  public int getBeforeRealLine(int realLine){
    if (realLine - 1 < 0){
      return lineNumberCount;
    }
    return ((realLine - 1) % (lineNumberCount + 1));
  }
  
  /**
   * 
   * @param realLine
   * @return Vraci dalsi cislo radku
   */
  public int getNextRealLine(int realLine){
    return ((realLine + 1) % (lineNumberCount + 1));
  }
  
  /**
   * Z relativniho cisla radku a vybraneho useku, vraci realne cislo radku pro algoritmus jako celek.
   * @param algorithmPart
   * @param line
   * @return 
   */
  public int getRealLineNumber(int algorithmPart, int line){
    algorithmPart = algorithmPart % algorithmParts.size();
    line = line % algorithmParts.get(algorithmPart).length;
    int realLine = 0;
    for (int i = 0; i < algorithmPart; i++){
      realLine += algorithmParts.get(i).length;
    }
    return realLine + line + 1;
  }
  
  /**
   * Vraci pocet algoritmu, jedna se o pocet samostatnych useku.
   * @return Vraci pocet algoritmu
   */
  public int getNumberOfAlgorithms(){
    return algorithmParts.size();
  }
  
  /**
   * Podle indexu konkretniho useku/algoritmu vraci jeho pocet radku.
   * @param algorithmPart index useku neboli index na jeden z algoritmu
   * @return Vraci pocet radku konkretniho useku
   */
  public int getNumberOfAlgorithmLine(int algorithmPart){
    return algorithmParts.get(algorithmPart).length;
  }
}
