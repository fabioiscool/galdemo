package simulator.algorithms;

import simulator.algorithms.AlgorithmBase.AlgorithmContent;
import simulator.algorithms.AlgorithmBase.AlgorithmElement;

/**
 * Trida predstavujici zapis pseudokodu algoritmu SCC. 
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class AlgorithmScc extends AbstractAlgorithm{

  protected AlgorithmElement[] content = new AlgorithmElement[] {
    new AlgorithmElement(AlgorithmBase.algorithmHeadingName, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmName, "SCC"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "("),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "G"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ")"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "zavolej "), 
      new AlgorithmContent(AlgorithmBase.algorithmMethodsName, "DFS"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "("),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "G"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ")"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " pro výpočet hodnot "), 
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "f"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "vypočítej "), 
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "G"),
      new AlgorithmContent(AlgorithmBase.algorithmSuperscriptName, "T"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "zavolej "), 
      new AlgorithmContent(AlgorithmBase.algorithmMethodsName, "DFS"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "("),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "G"),
      new AlgorithmContent(AlgorithmBase.algorithmSuperscriptName, "T"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ")"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ", ale v hlavním cyklu "
          + "uvažuj uzly v klesajícím pořadí podle hodnoty "), 
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "f"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "na výstup dej uzly "
          + "každého stromu z DFS lesa, určeného na řádku 3, jako samostatnou silně souvislou komponentu."), 
    }),
  };
  
  public AlgorithmScc() {
    this.algorithmParts.add(content);
    this.algorithmParts.add(AlgorithmDfs.content1);
    this.algorithmParts.add(AlgorithmDfs.content2);
    this.addAlgorithm();
  }
  
}
