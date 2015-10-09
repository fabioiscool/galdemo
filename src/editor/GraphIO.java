package editor;

import com.mxgraph.io.mxCodec;
import com.mxgraph.io.mxStylesheetCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import simulator.graphs.AlgorithmGraph;
import simulator.graphs.AlgorithmGraphStylesheet;
import simulator.graphs.BasicStylesheet;
import simulator.graphs.GraphEdgeBasicData;
import simulator.graphs.GraphEdgeData;
import simulator.graphs.GraphVertexBasicData;
import simulator.graphs.GraphVertexData;

/**
 * Trida poskytujici metody pro rozsirene ulozeni a nacteni grafu.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class GraphIO {
  
  public static class GraphIOBadXmlException extends Exception {
    public GraphIOBadXmlException() {
      super(mxResources.get("BadXmlException"));
    }
  }
  
  private GraphIO() {}
  
  /**
   * Metoda vztvori klon vstupniho grafu.
   * @param graph vstupni graf
   * @return Klon grafu
   */
  public static mxGraph getClonedGraph(mxGraph graph){
    mxGraph clonedGraph = new mxGraph();
    mxStylesheet stylesheet = graph.getStylesheet();
    clonedGraph.addCells(graph.cloneCells(graph.getChildCells(graph.getDefaultParent())));
    for (Object o : mxGraphModel.getChildren(clonedGraph.getModel(), clonedGraph.getDefaultParent())){
      if (o instanceof mxCell){
        mxCell cell = (mxCell) o;
        if (cell.getValue() != null && (cell.getValue() instanceof GraphVertexBasicData || 
                                        cell.getValue() instanceof GraphEdgeBasicData)){
          if (cell.isVertex()){
            cell.setValue(((GraphVertexBasicData)cell.getValue()).getBasicVertexData());
          } else {
            cell.setValue(((GraphEdgeBasicData)cell.getValue()).getBasicEdgeData());
          }
        } else {
          if (cell.isVertex()){
            cell.setValue(new GraphVertexData());
          } else {
            cell.setValue((new GraphEdgeData()));
          }
        }
        if (stylesheet instanceof AlgorithmGraphStylesheet){
          AlgorithmGraphStylesheet ags = (AlgorithmGraphStylesheet) stylesheet;
          cell.setStyle(cell.isVertex() ? ags.getVertexStyle() : ags.getEdgeStyle());
        }
      }
    }
    return clonedGraph;
  }
  
  /**
   * Metoda ulozi graf do xml souboru spolecne s jeho stylesheetem.
   * @param graph Graf k ulozeni
   * @param graphPath Cesta kam se ma ulozit
   * @throws IOException 
   */
  public static void writeGraph(mxGraph graph, Path graphPath) throws IOException {
    mxGraph clonedGraph = getClonedGraph(graph);
    try {
      mxStylesheetCodec styleCodec = new mxStylesheetCodec();
      mxCodec codec = new mxCodec();
    
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder;
      docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element rootElement = doc.createElement("graph");
      doc.appendChild(rootElement);
      rootElement.appendChild(doc.importNode(codec.encode(clonedGraph.getModel()),true)); 
      rootElement.appendChild(doc.importNode(styleCodec.encode(codec, graph.getStylesheet()), true));   
      
      //String xml = mxXmlUtils.getXml(doc.getDocumentElement());
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      StreamResult result = new StreamResult(new StringWriter());
      DOMSource source = new DOMSource(doc);
      transformer.transform(source, result);
      String xmlString = result.getWriter().toString();
      try (FileWriter fw = new FileWriter(graphPath.toFile())) {
        fw.write(xmlString);
        fw.flush();
      }
    } catch (TransformerException | ParserConfigurationException ex) {
      Logger.getLogger(GraphIO.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  /**
   * Metoda nacte graf z xml souboru, kde je ulozen model grafu a stylesheet grafu
   * @param graphPath Cesta k souboru s xml
   * @return nacteny graf, nebo null pokud xml soubor neobsahuje vsechna data.
   * 
   * @throws IOException
   * @throws SAXException
   * @throws ParserConfigurationException 
   */
  public static AlgorithmGraph readGraph(Path graphPath) throws IOException, SAXException, ParserConfigurationException, GraphIOBadXmlException{
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
    Document docGraph = docBuilder.parse(graphPath.toFile());
    if (docGraph == null){
      throw new GraphIOBadXmlException();
    }
    Node model = docGraph.getDocumentElement().getElementsByTagName("mxGraphModel").item(0);
    Node sheet = docGraph.getDocumentElement().getElementsByTagName("mxStylesheet").item(0);
    if (model == null || sheet == null){
      throw new GraphIOBadXmlException();
    }
    mxStylesheetCodec styleCodec = new mxStylesheetCodec();
    mxCodec codec = new mxCodec();
    AlgorithmGraph graph = new AlgorithmGraph();
    mxStylesheet stylesheet = (mxStylesheet) styleCodec.decode(codec, sheet);
    BasicStylesheet basicStylesheet = BasicStylesheet.getFromMxStylesheet(stylesheet);
    graph.setOriented(basicStylesheet.isDirectedEgeStyle());
    graph.setModel((mxIGraphModel) codec.decode(model));
    graph.setStylesheet(basicStylesheet);
    return graph;
  }
}
