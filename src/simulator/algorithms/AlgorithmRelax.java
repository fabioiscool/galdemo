package simulator.algorithms;

import simulator.algorithms.AlgorithmBase.AlgorithmContent;
import simulator.algorithms.AlgorithmBase.AlgorithmElement;

/**
 * Trida predstavujici zapis pseudokodu algoritmu pro relaxaci hran.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class AlgorithmRelax extends AbstractAlgorithm{
  
  public static AlgorithmElement[] content = new AlgorithmElement[] {
    new AlgorithmElement(AlgorithmBase.algorithmHeadingName, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmName, "Relax"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "("),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ","),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ","),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "w"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ")"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "if "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "d"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " > "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "d"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " + "),
      new AlgorithmContent(AlgorithmBase.algorithmMethodsName, "w"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "("),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ","),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ") "),
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "then"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "d"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "d"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " + "),
      new AlgorithmContent(AlgorithmBase.algorithmMethodsName, "w"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "("),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ","),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ") "),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "π"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "end if"),  
    }),
  };

  public AlgorithmRelax() {
    this.algorithmParts.add(content);
    this.addAlgorithm();
  }
}
