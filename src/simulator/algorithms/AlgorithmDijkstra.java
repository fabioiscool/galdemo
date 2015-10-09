package simulator.algorithms;

import simulator.algorithms.AlgorithmBase.AlgorithmContent;
import simulator.algorithms.AlgorithmBase.AlgorithmElement;

/**
 * Trida predstavujici zapis pseudokodu Dijkstrova algoritmu.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class AlgorithmDijkstra extends AbstractAlgorithm{
  
  public static AlgorithmElement[] content = new AlgorithmElement[] {
    new AlgorithmElement(AlgorithmBase.algorithmHeadingName, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmName, "Dijkstra"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "("),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "G"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ","),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "w"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ","),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "s"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ")"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {
      new AlgorithmContent(AlgorithmBase.algorithmMethodsName, "Initialize-Single-Source"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "("),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "G"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ","),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "s"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ")"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "S"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← ∅"),    
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "Q"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "), 
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "V"),     
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "while "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "Q"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ≠ ∅ "),
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "do"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "), 
      new AlgorithmContent(AlgorithmBase.algorithmMethodsName, "Extract-Min"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "("),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "Q"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ")"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "S"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "S"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ∪ {"),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "}"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "for"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " každý uzel "), 
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ∈ "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "Adj"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"),
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, " do"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName2, new AlgorithmContent[] {
      new AlgorithmContent(AlgorithmBase.algorithmMethodsName, "Relax"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "("),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ","),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ","),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "w"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ")"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "end for"), 
    }), 
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "end while"),  
    }),
  };
  
  public AlgorithmDijkstra() {
    this.algorithmParts.add(content);
    this.algorithmParts.add(AlgorithmRelax.content);
    this.algorithmParts.add(AlgorithmInitSingleSource.content);
    this.addAlgorithm();
  }
}
