package simulator.algorithms;

import simulator.algorithms.AlgorithmBase.AlgorithmContent;
import simulator.algorithms.AlgorithmBase.AlgorithmElement;

/**
 * Trida predstavujici zapis pseudokodu algoritmu BFS.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class AlgorithmBfs extends AbstractAlgorithm{
  
  public static AlgorithmElement[] content = new AlgorithmElement[] {
    new AlgorithmElement(AlgorithmBase.algorithmHeadingName, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmName, "BFS"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "("),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "G"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ")"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "for"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " každý uzel "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ∈ "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "V"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " - {"),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "s"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "}"),
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, " do")
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "color"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "WHITE"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "d"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "∞"),   
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "π"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "NIL"),  
    }), 
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "end for"),  
    }), 
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "color"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "s"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "GRAY"),
    }), 
   new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "d"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "s"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "0"),   
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "π"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "s"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "NIL"),  
    }), 
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "Q"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "∅"),     
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmMethodsName, "enqueue"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "("),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "Q"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ","),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "s"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ")"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "while "), 
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "Q"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ≠ "), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "∅"), 
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, " do"),     
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "), 
      new AlgorithmContent(AlgorithmBase.algorithmMethodsName, "dequeue"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "("),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "Q"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ")"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "for"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " každý "), 
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ∈ "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "Adj"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"),
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, " do"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName2, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "if"), 
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, " color"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " = "),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "WHITE "),
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "then"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName3, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "color"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "GRAY"),
    }), 
   new AlgorithmElement(AlgorithmBase.algorithmLineName3, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "d"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "), 
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "d"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " + 1"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName3, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "π"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "), 
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),  
    }), 
    new AlgorithmElement(AlgorithmBase.algorithmLineName3, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmMethodsName, "enqueue"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "("),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "Q"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ","),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ")"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName2, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "end if"), 
    }), 
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "end for"), 
    }), 
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "end while"), 
    }), 
  };
  
  public AlgorithmBfs() {
    this.algorithmParts.add(content);
    this.addAlgorithm();
  }
 
}
