package simulator.algorithms;

import simulator.algorithms.AlgorithmBase.AlgorithmContent;
import simulator.algorithms.AlgorithmBase.AlgorithmElement;

/**
 * Trida predstavujici zapis pseudokodu BellmanFordova algoritmu.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class AlgorithmBellmanFord extends AbstractAlgorithm{
  
  public static AlgorithmElement[] content = new AlgorithmElement[] {
    new AlgorithmElement(AlgorithmBase.algorithmHeadingName, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmName, "Bellman-Ford"), 
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
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "for "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "i"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← 1"),
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, " to "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "n"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " - 1 "),
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "do"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "for "),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "každou hranu ("),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ","),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ")"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ∈ "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "E"),
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
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "end for"),  
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "for "),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "každou hranu ("),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ","),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ")"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ∈ "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "E"),
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, " do"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {
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
    new AlgorithmElement(AlgorithmBase.algorithmLineName2, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "return "), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "FALSE"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "end if"),  
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "end for"),  
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "return "), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "TRUE"),
    }),
  };

  public AlgorithmBellmanFord() {
    this.algorithmParts.add(content);
    this.algorithmParts.add(AlgorithmRelax.content);
    this.algorithmParts.add(AlgorithmInitSingleSource.content);
    this.addAlgorithm();
  }
  
  
}
