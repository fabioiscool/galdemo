package simulator.algorithms;

import simulator.algorithms.AlgorithmBase.AlgorithmContent;
import simulator.algorithms.AlgorithmBase.AlgorithmElement;

/**
 * Trida predstavujici zapis pseudokodu algoritmu pro inicializaci pri hledani nejkratsich cest.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class AlgorithmInitSingleSource extends AbstractAlgorithm{
  
  public static AlgorithmElement[] content = new AlgorithmElement[] {
    new AlgorithmElement(AlgorithmBase.algorithmHeadingName, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmName, "Initialize-Single-Source"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "("),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "G"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ","),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "s"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ")"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "for"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " každý "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ∈ "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "V"),
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, " do")
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "d"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← ∞"),  
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "π"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← NIL"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "end for"),  
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "d"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "s"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← 0"),   
    }),
  };

  public AlgorithmInitSingleSource() {
    this.algorithmParts.add(content);
    this.addAlgorithm();
  }
}
