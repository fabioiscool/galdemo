package simulator.algorithms;

import simulator.algorithms.AlgorithmBase.AlgorithmContent;
import simulator.algorithms.AlgorithmBase.AlgorithmElement;

/**
 * Trida predstavujici zapis pseudokodu algoritmu DFS.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class AlgorithmDfs extends AbstractAlgorithm{
    static public AlgorithmBase.AlgorithmElement[] content1 = new AlgorithmBase.AlgorithmElement[] {
    new AlgorithmBase.AlgorithmElement(AlgorithmBase.algorithmHeadingName, new AlgorithmBase.AlgorithmContent[] {      
      new AlgorithmBase.AlgorithmContent(AlgorithmBase.algorithmName, "DFS"), 
      new AlgorithmBase.AlgorithmContent(AlgorithmBase.algorithmOthersName, "("),
      new AlgorithmBase.AlgorithmContent(AlgorithmBase.algorithmVariableName, "G"),
      new AlgorithmBase.AlgorithmContent(AlgorithmBase.algorithmOthersName, ")"),     
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "for"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " každý uzel "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ∈ "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "V"),
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
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "time"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "0"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "for"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " každý uzel "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ∈ "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "V"),
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, " do")
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "if"), 
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, " color"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " = "),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "WHITE"),
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, " then")
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName2, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmMethodsName, "DFS-VISIT"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "("),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ")"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "end if"),  
    }), 
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "end for"),  
    }), 
  };
  static public AlgorithmBase.AlgorithmElement[] content2 = new AlgorithmBase.AlgorithmElement[] {
    new AlgorithmBase.AlgorithmElement(AlgorithmBase.algorithmHeadingName, new AlgorithmBase.AlgorithmContent[] {      
      new AlgorithmBase.AlgorithmContent(AlgorithmBase.algorithmName, "DFS-VISIT"), 
      new AlgorithmBase.AlgorithmContent(AlgorithmBase.algorithmOthersName, "("),
      new AlgorithmBase.AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmBase.AlgorithmContent(AlgorithmBase.algorithmOthersName, ")"),     
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "color"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "GRAY"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {  
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "time"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "), 
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "time"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " + 1"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "d"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "), 
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "time"),  
    }), 
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "for"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " každý uzel "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ∈ "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "Adj"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"),
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, " do")
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "if"), 
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, " color"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " = "),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "WHITE"),
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, " then")
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName2, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "π"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "), 
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),  
    }), 
    new AlgorithmElement(AlgorithmBase.algorithmLineName2, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmMethodsName, "DFS-VISIT"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "("),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "v"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, ")"),
    }),    
    new AlgorithmElement(AlgorithmBase.algorithmLineName1, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "end if"),  
    }), 
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmKeywordsName, "end for"),  
    }), 
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "color"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "BLACK"),
    }),
    new AlgorithmElement(AlgorithmBase.algorithmLineName0, new AlgorithmContent[] {      
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "f"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ["),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "u"),
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, "]"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "), 
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "time"),  
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " ← "),
      new AlgorithmContent(AlgorithmBase.algorithmVariableName, "time"), 
      new AlgorithmContent(AlgorithmBase.algorithmOthersName, " + 1"),
    }), 
  };

  public AlgorithmDfs() {
    this.algorithmParts.add(content1);
    this.algorithmParts.add(content2);
    this.addAlgorithm();
  }
}
