package simulator.graphs;

import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import editor.GraphIO;
import java.io.IOException;
import java.nio.file.Paths;
import simulator.graphs.bellmanford.BellmanFordGraphVertex;
import simulator.graphs.bellmanford.BellmanFordGraphEdge;
import simulator.graphs.bellmanford.BellmanFordStylesheet;
import simulator.graphs.bfs.BfsGraphCell;
import simulator.graphs.bfs.BfsStylesheet;
import simulator.graphs.dfs.DfsGraphVertex;
import simulator.graphs.dfs.DfsGraphEdge;
import simulator.graphs.dfs.DfsStylesheet;
import simulator.graphs.dijkstra.DijkstraStyleSheet;
import simulator.graphs.scc.SccStyleSheet;
import simulator.graphs.toposort.TopologicalSortStylesheet;

/**
 * Trida pro vygenerovani zakladnich grafu.
 * @author Jakub Varadinek <jvaradinek at gmail.com>
 */
public class BasicGraphs {

  public static AlgorithmGraph getOneNodeGraphDijkstra() {
    final AlgorithmGraph graph = new AlgorithmGraph(DijkstraStyleSheet.getStylesheet());
    graph.setOriented(true);
    graph.getModel().beginUpdate();
    try {
      graph.insertVertex(new BellmanFordGraphVertex("A", true), 250, 250, 80, 60);
    } finally {
      graph.getModel().endUpdate();
    }
    return graph;
  }

  public static AlgorithmGraph getOneNodeGraphScc() {
    final AlgorithmGraph graph = new AlgorithmGraph(SccStyleSheet.getStylesheet());
    graph.setOriented(true);
    graph.getModel().beginUpdate();
    try {
      graph.insertVertex(new DfsGraphVertex("A", true), 250, 250, 80, 60);
    } finally {
      graph.getModel().endUpdate();
    }
    return graph;
  }

  public static AlgorithmGraph getOneNodeGraphTopoSort() {
    final AlgorithmGraph graph = new AlgorithmGraph(TopologicalSortStylesheet.getStylesheet());
    graph.setOriented(true);
    graph.getModel().beginUpdate();
    try {
      graph.insertVertex(new DfsGraphVertex("A", true), 250, 250, 80, 60);
    } finally {
      graph.getModel().endUpdate();
    }
    return graph;
  }

  public static AlgorithmGraph getOneNodeGraphBfs() {
    final AlgorithmGraph graph = new AlgorithmGraph(BfsStylesheet.getStylesheet());
    graph.setOriented(true);
    graph.getModel().beginUpdate();
    try {
      graph.insertVertex(new BfsGraphCell("A", true), 250, 250, 80, 60);
    } finally {
      graph.getModel().endUpdate();
    }
    return graph;
  }

  public static AlgorithmGraph getOneNodeGraphDfs() {
    final AlgorithmGraph graph = new AlgorithmGraph(DfsStylesheet.getStylesheet());
    graph.setOriented(true);
    Object parent = graph.getDefaultParent();
    graph.getModel().beginUpdate();
    try {
      graph.insertVertex(parent, null, new DfsGraphVertex("A", true), 250, 250, 80, 60, DfsStylesheet.VERTEX_STYLE);
    } finally {
      graph.getModel().endUpdate();
    }
    return graph;
  }

  public static AlgorithmGraph getOneNodeGraphBellmanFord() {
    final AlgorithmGraph graph = new AlgorithmGraph(BellmanFordStylesheet.getStylesheet());
    graph.setOriented(true);
    graph.getModel().beginUpdate();
    try {
      graph.insertVertex(new BellmanFordGraphVertex("A", true), 250, 250, 80, 60);
    } finally {
      graph.getModel().endUpdate();
    }
    return graph;
  }

  public static AlgorithmGraph getDijkstraGraph2() {
    final AlgorithmGraph graph = new AlgorithmGraph(DijkstraStyleSheet.getStylesheet());
    graph.setOriented(true);
    graph.getModel().beginUpdate();
    try {
      Object s = graph.insertVertex(new BellmanFordGraphVertex("S", true), 40, 175, 80, 60);
      Object t = graph.insertVertex(new BellmanFordGraphVertex("T"), 180, 50, 80, 60);
      Object z = graph.insertVertex(new BellmanFordGraphVertex("Z"), 520, 300, 80, 60);
      Object x = graph.insertVertex(new BellmanFordGraphVertex("X"), 520, 50, 80, 60);
      Object y = graph.insertVertex(new BellmanFordGraphVertex("Y"), 180, 300, 80, 60);

      graph.insertEdge(new BellmanFordGraphEdge(10.0), s, t);
      graph.insertEdge(new BellmanFordGraphEdge(5.0), s, y);
      graph.insertEdge(new BellmanFordGraphEdge(2.0), t, y);
      graph.insertEdge(new BellmanFordGraphEdge(1.0), t, x);
      graph.insertEdge(new BellmanFordGraphEdge(3.0), y, t);
      graph.insertEdge(new BellmanFordGraphEdge(2.0), y, z);
      graph.insertEdge(new BellmanFordGraphEdge(9.0), y, x);
      graph.insertEdge(new BellmanFordGraphEdge(-4.0), x, z);
      graph.insertEdge(new BellmanFordGraphEdge(7.0), z, s);
      graph.insertEdge(new BellmanFordGraphEdge(-6.0), z, x);
    } finally {
      graph.getModel().endUpdate();
    }
    new mxParallelEdgeLayout(graph).execute(graph.getDefaultParent());
    new mxEdgeLabelLayout(graph).execute(graph.getDefaultParent());
    return graph;
  }

  public static AlgorithmGraph getDijkstraGraph() {
    final AlgorithmGraph graph = new AlgorithmGraph(DijkstraStyleSheet.getStylesheet());
    graph.setOriented(true);
    graph.getModel().beginUpdate();
    try {
      Object s = graph.insertVertex(new BellmanFordGraphVertex("S", true), 40, 175, 80, 60);
      Object t = graph.insertVertex(new BellmanFordGraphVertex("T"), 180, 50, 80, 60);
      Object z = graph.insertVertex(new BellmanFordGraphVertex("Z"), 520, 300, 80, 60);
      Object x = graph.insertVertex(new BellmanFordGraphVertex("X"), 520, 50, 80, 60);
      Object y = graph.insertVertex(new BellmanFordGraphVertex("Y"), 180, 300, 80, 60);

      graph.insertEdge(new BellmanFordGraphEdge(10.0), s, t);
      graph.insertEdge(new BellmanFordGraphEdge(5.0), s, y);
      graph.insertEdge(new BellmanFordGraphEdge(2.0), t, y);
      graph.insertEdge(new BellmanFordGraphEdge(1.0), t, x);
      graph.insertEdge(new BellmanFordGraphEdge(3.0), y, t);
      graph.insertEdge(new BellmanFordGraphEdge(2.0), y, z);
      graph.insertEdge(new BellmanFordGraphEdge(9.0), y, x);
      graph.insertEdge(new BellmanFordGraphEdge(4.0), x, z);
      graph.insertEdge(new BellmanFordGraphEdge(7.0), z, s);
      graph.insertEdge(new BellmanFordGraphEdge(6.0), z, x);
    } finally {
      graph.getModel().endUpdate();
    }
    new mxParallelEdgeLayout(graph).execute(graph.getDefaultParent());
    new mxEdgeLabelLayout(graph).execute(graph.getDefaultParent());
    return graph;
  }

  public static AlgorithmGraph getBellmanFordGraph2() {
    final AlgorithmGraph graph = new AlgorithmGraph(BellmanFordStylesheet.getStylesheet());
    graph.setOriented(true);
    graph.getModel().beginUpdate();
    try {
      Object s = graph.insertVertex(new BellmanFordGraphVertex("S", true), 80, 200, 80, 60);
      Object t = graph.insertVertex(new BellmanFordGraphVertex("T"), 220, 50, 80, 60);
      Object z = graph.insertVertex(new BellmanFordGraphVertex("Z"), 460, 350, 80, 60);
      Object x = graph.insertVertex(new BellmanFordGraphVertex("X"), 460, 50, 80, 60);
      Object y = graph.insertVertex(new BellmanFordGraphVertex("Y"), 220, 430, 80, 60);

      graph.insertEdge(new BellmanFordGraphEdge(6.0), s, t);
      graph.insertEdge(new BellmanFordGraphEdge(7.0), s, y);
      graph.insertEdge(new BellmanFordGraphEdge(8.0), t, y);
      graph.insertEdge(new BellmanFordGraphEdge(5.0), t, x);
      graph.insertEdge(new BellmanFordGraphEdge(-4.0), t, z);
      graph.insertEdge(new BellmanFordGraphEdge(9.0), y, z);
      graph.insertEdge(new BellmanFordGraphEdge(-3.0), y, x);
      graph.insertEdge(new BellmanFordGraphEdge(-2.0), x, t);
      graph.insertEdge(new BellmanFordGraphEdge(2.0), z, s);
      graph.insertEdge(new BellmanFordGraphEdge(-7.0), z, x);
    } finally {
      graph.getModel().endUpdate();
    }
    new mxParallelEdgeLayout(graph).execute(graph.getDefaultParent());
    new mxEdgeLabelLayout(graph).execute(graph.getDefaultParent());
    return graph;
  }

  public static AlgorithmGraph getBellmanFordGraph() {
    final AlgorithmGraph graph = new AlgorithmGraph(BellmanFordStylesheet.getStylesheet());
    graph.setOriented(true);
    graph.getModel().beginUpdate();
    try {
      Object s = graph.insertVertex(new BellmanFordGraphVertex("S", true), 80, 200, 80, 60);
      Object t = graph.insertVertex(new BellmanFordGraphVertex("T"), 220, 50, 80, 60);
      Object z = graph.insertVertex(new BellmanFordGraphVertex("Z"), 460, 350, 80, 60);
      Object x = graph.insertVertex(new BellmanFordGraphVertex("X"), 460, 50, 80, 60);
      Object y = graph.insertVertex(new BellmanFordGraphVertex("Y"), 220, 350, 80, 60);

      graph.insertEdge(new BellmanFordGraphEdge(6.0), s, t);
      graph.insertEdge(new BellmanFordGraphEdge(7.0), s, y);
      graph.insertEdge(new BellmanFordGraphEdge(8.0), t, y);
      graph.insertEdge(new BellmanFordGraphEdge(5.0), t, x);
      graph.insertEdge(new BellmanFordGraphEdge(-4.0), t, z);
      graph.insertEdge(new BellmanFordGraphEdge(9.0), y, z);
      graph.insertEdge(new BellmanFordGraphEdge(-3.0), y, x);
      graph.insertEdge(new BellmanFordGraphEdge(-2.0), x, t);
      graph.insertEdge(new BellmanFordGraphEdge(2.0), z, s);
      graph.insertEdge(new BellmanFordGraphEdge(7.0), z, x);
    } finally {
      graph.getModel().endUpdate();
    }
    new mxParallelEdgeLayout(graph).execute(graph.getDefaultParent());
    new mxEdgeLabelLayout(graph).execute(graph.getDefaultParent());
    //new EdgeLabelLayout(graph).execute(graph.getDefaultParent());
    return graph;
  }

  public static AlgorithmGraph getSccGraph() {
    final AlgorithmGraph graph = new AlgorithmGraph(SccStyleSheet.getStylesheet());
    graph.setOriented(true);
    graph.getModel().beginUpdate();
    try {
      Object a = graph.insertVertex(new DfsGraphVertex("A"), 40, 60, 80, 60);
      Object b = graph.insertVertex(new DfsGraphVertex("B"), 180, 60, 80, 60);
      Object c = graph.insertVertex(new DfsGraphVertex("C"), 320, 60, 80, 60);
      Object d = graph.insertVertex(new DfsGraphVertex("D"), 460, 60, 80, 60);
      Object e = graph.insertVertex(new DfsGraphVertex("E"), 40, 200, 80, 60);
      Object f = graph.insertVertex(new DfsGraphVertex("F"), 180, 200, 80, 60);
      Object g = graph.insertVertex(new DfsGraphVertex("G"), 320, 200, 80, 60);
      Object h = graph.insertVertex(new DfsGraphVertex("H", true), 460, 200, 80, 60);

      graph.insertEdge(new DfsGraphEdge(), a, b);
      graph.insertEdge(new DfsGraphEdge(), b, c);
      graph.insertEdge(new DfsGraphEdge(), b, e);
      graph.insertEdge(new DfsGraphEdge(), b, f);
      graph.insertEdge(new DfsGraphEdge(), c, d);
      graph.insertEdge(new DfsGraphEdge(), c, g);
      graph.insertEdge(new DfsGraphEdge(), d, c);
      graph.insertEdge(new DfsGraphEdge(), d, h);
      graph.insertEdge(new DfsGraphEdge(), e, a);
      graph.insertEdge(new DfsGraphEdge(), e, f);
      graph.insertEdge(new DfsGraphEdge(), f, g);
      graph.insertEdge(new DfsGraphEdge(), g, f);
      graph.insertEdge(new DfsGraphEdge(), g, h);
      graph.insertEdge(new DfsGraphEdge(), h, h);
    } finally {
      graph.getModel().endUpdate();
    }
    new mxParallelEdgeLayout(graph).execute(graph.getDefaultParent());
    return graph;
  }

  public static AlgorithmGraph getTopologicalSortGraph() {
    final AlgorithmGraph graph = new AlgorithmGraph(TopologicalSortStylesheet.getStylesheet());
    graph.setOriented(true);
    graph.getModel().beginUpdate();
    try {
      Object kosile = graph.insertVertex(new DfsGraphVertex("košile", true), 180, 280, 80, 60);
      Object vazanka = graph.insertVertex(new DfsGraphVertex("vázanka"), 180, 400, 80, 60);
      Object bunda = graph.insertVertex(new DfsGraphVertex("bunda"), 180, 520, 80, 60);
      Object opasek = graph.insertVertex(new DfsGraphVertex("opasek"), 60, 360, 80, 60);
      Object hodinky = graph.insertVertex(new DfsGraphVertex("hodinky"), 360, 130, 80, 60);
      Object spodky = graph.insertVertex(new DfsGraphVertex("spodky"), 60, 60, 80, 60);
      Object kalhoty = graph.insertVertex(new DfsGraphVertex("kalhoty"), 60, 200, 80, 60);
      Object boty = graph.insertVertex(new DfsGraphVertex("boty"), 260, 200, 80, 60);
      Object ponozky = graph.insertVertex(new DfsGraphVertex("ponožky"), 260, 60, 80, 60);

      graph.insertEdge(new DfsGraphEdge(), spodky, boty);
      graph.insertEdge(new DfsGraphEdge(), spodky, kalhoty);
      graph.insertEdge(new DfsGraphEdge(), ponozky, boty);
      graph.insertEdge(new DfsGraphEdge(), kalhoty, boty);
      graph.insertEdge(new DfsGraphEdge(), kalhoty, opasek);
      graph.insertEdge(new DfsGraphEdge(), kosile, opasek);
      graph.insertEdge(new DfsGraphEdge(), kosile, vazanka);
      graph.insertEdge(new DfsGraphEdge(), vazanka, bunda);
      graph.insertEdge(new DfsGraphEdge(), opasek, bunda);
    } finally {
      graph.getModel().endUpdate();
    }
    return graph;
  }

  public static AlgorithmGraph getDfsGraph2() {
    final AlgorithmGraph graph = new AlgorithmGraph(DfsStylesheet.getStylesheet());
    graph.setOriented(true);
    graph.getModel().beginUpdate();
    try {
      Object a = graph.insertVertex(new DfsGraphVertex("A"), 40, 60, 60, 60);
      Object b = graph.insertVertex(new DfsGraphVertex("B"), 180, 60, 60, 60);
      Object c = graph.insertVertex(new DfsGraphVertex("C"), 320, 60, 60, 60);
      Object d = graph.insertVertex(new DfsGraphVertex("D"), 460, 60, 60, 60);
      Object e = graph.insertVertex(new DfsGraphVertex("E"), 40, 200, 60, 60);
      Object f = graph.insertVertex(new DfsGraphVertex("F"), 180, 200, 60, 60);
      Object g = graph.insertVertex(new DfsGraphVertex("G"), 320, 200, 60, 60);
      Object h = graph.insertVertex(new DfsGraphVertex("H"), 460, 200, 60, 60);

      graph.insertEdge(new DfsGraphEdge(), a, e);
      graph.insertEdge(new DfsGraphEdge(), b, a);
      graph.insertEdge(new DfsGraphEdge(), b, f);
      graph.insertEdge(new DfsGraphEdge(), c, b);
      graph.insertEdge(new DfsGraphEdge(), c, f);
      graph.insertEdge(new DfsGraphEdge(), d, g);
      graph.insertEdge(new DfsGraphEdge(), d, h);
      graph.insertEdge(new DfsGraphEdge(), e, b);
      graph.insertEdge(new DfsGraphEdge(), f, e);
      graph.insertEdge(new DfsGraphEdge(), g, c);
      graph.insertEdge(new DfsGraphEdge(), g, f);
      graph.insertEdge(new DfsGraphEdge(), h, g);
      graph.insertEdge(new DfsGraphEdge(), h, d);
    } finally {
      graph.getModel().endUpdate();
    }
    //new mxHierarchicalLayout(graph).execute(graph.getDefaultParent());
    new mxParallelEdgeLayout(graph).execute(graph.getDefaultParent());
    return graph;
  }

  public static AlgorithmGraph getDfsGraph() {
    final AlgorithmGraph graph = new AlgorithmGraph(DfsStylesheet.getStylesheet());
    graph.setOriented(true);
    graph.getModel().beginUpdate();
    try {
      DfsGraphVertex aCell = new DfsGraphVertex("A");
      //aCell.setSelected(true);
      Object a = graph.insertVertex(aCell, 60, 60, 60, 60);
      Object b = graph.insertVertex(new DfsGraphVertex("B"), 220, 60, 60, 60);
      Object c = graph.insertVertex(new DfsGraphVertex("C"), 220, 220, 60, 60);
      Object d = graph.insertVertex(new DfsGraphVertex("D"), 60, 220, 60, 60);
      Object e = graph.insertVertex(new DfsGraphVertex("E"), 380, 60, 60, 60);
      Object f = graph.insertVertex(new DfsGraphVertex("F"), 380, 220, 60, 60);
      graph.insertEdge(new DfsGraphEdge(), a, b);
      graph.insertEdge(new DfsGraphEdge(), a, d);
      graph.insertEdge(new DfsGraphEdge(), c, d);
      graph.insertEdge(new DfsGraphEdge(), d, b);
      graph.insertEdge(new DfsGraphEdge(), b, c);
      graph.insertEdge(new DfsGraphEdge(), e, c);
      graph.insertEdge(new DfsGraphEdge(), e, f);
      graph.insertEdge(new DfsGraphEdge(), f, f);

    } finally {
      graph.getModel().endUpdate();
    }
    return graph;
  }

  public static AlgorithmGraph getBfsGraph2() {
    final AlgorithmGraph graph = new AlgorithmGraph(BfsStylesheet.getStylesheet());
    graph.setOriented(true);
    graph.getModel().beginUpdate();
    try {
      Object first = graph.insertVertex(new BfsGraphCell("1"), 130, 130, 60, 60);
      Object a = graph.insertVertex(new BfsGraphCell("a"), 20, 20, 40, 40);
      Object b = graph.insertVertex(new BfsGraphCell("b"), 200, 60, 40, 40);
      Object c = graph.insertVertex(new BfsGraphCell("c"), 240, 240, 40, 40);
      Object d = graph.insertVertex(new BfsGraphCell("d"), 10, 200, 40, 40);
      Object e = graph.insertVertex(new BfsGraphCell("e"), 120, 260, 40, 40);
      graph.insertEdge(null, a, first);
      graph.insertEdge(null, b, first);
      graph.insertEdge(null, c, first);
      graph.insertEdge(null, d, first);
      graph.insertEdge(null, e, first);

      Object second = graph.insertVertex(new BfsGraphCell("2"), 320, 130, 60, 60);
      Object q = graph.insertVertex(new BfsGraphCell("q"), 320, 10, 40, 40);
      Object r = graph.insertVertex(new BfsGraphCell("r"), 440, 80, 40, 40);
      Object h = graph.insertVertex(new BfsGraphCell("h"), 400, 240, 40, 40);
      graph.insertEdge(null, c, second);
      graph.insertEdge(null, h, second);
      graph.insertEdge(null, q, second);
      graph.insertEdge(null, r, second);

      Object third = graph.insertVertex(new BfsGraphCell("3"), 510, 130, 60, 60);
      Object p = graph.insertVertex(new BfsGraphCell("p"), 600, 90, 40, 40);
      Object o = graph.insertVertex(new BfsGraphCell("o"), 590, 240, 40, 40);
      graph.insertEdge(null, h, third);
      graph.insertEdge(null, p, third);
      graph.insertEdge(null, o, third);

      Object fourth = graph.insertVertex(new BfsGraphCell("4"), 130, 360, 60, 60);
      Object f = graph.insertVertex(new BfsGraphCell("f"), 80, 440, 40, 40);
      Object g = graph.insertVertex(new BfsGraphCell("g"), 260, 450, 40, 40);
      graph.insertEdge(null, e, fourth);
      graph.insertEdge(null, f, fourth);
      graph.insertEdge(null, g, fourth);
      BfsGraphCell vCell = new BfsGraphCell("5");
      vCell.setSelected(true);
      Object fifth = graph.insertVertex(vCell, 320, 360, 60, 60);
      Object i = graph.insertVertex(new BfsGraphCell("i"), 380, 480, 40, 40);
      graph.insertEdge(null, c, fifth);
      graph.insertEdge(null, g, fifth);
      graph.insertEdge(null, i, fifth);

      Object sixth = graph.insertVertex(new BfsGraphCell("6"), 510, 360, 60, 60);
      Object n = graph.insertVertex(new BfsGraphCell("n"), 450, 420, 40, 40);
      Object m = graph.insertVertex(new BfsGraphCell("m"), 620, 400, 40, 40);
      Object k = graph.insertVertex(new BfsGraphCell("k"), 550, 490, 40, 40);
      graph.insertEdge(null, o, sixth);
      graph.insertEdge(null, n, sixth);
      graph.insertEdge(null, m, sixth);
      graph.insertEdge(null, k, sixth);
    } finally {
      graph.getModel().endUpdate();
    }
    return graph;
  }

  public static AlgorithmGraph getBfsGraph() {
    final AlgorithmGraph graph = new AlgorithmGraph(BfsStylesheet.getStylesheet());
    graph.setOriented(false);
    graph.getModel().beginUpdate();
    try {
      BfsGraphCell sCell = new BfsGraphCell("s");
      sCell.setSelected(true);
      Object v1 = graph.insertVertex(sCell, 200, 120, 60, 60);
      Object v2 = graph.insertVertex(new BfsGraphCell("r"), 40, 60, 60, 60);
      Object v3 = graph.insertVertex(new BfsGraphCell("v"), 40, 300, 60, 60);
      Object v4 = graph.insertVertex(new BfsGraphCell("t"), 350, 40, 60, 60);
      Object v5 = graph.insertVertex(new BfsGraphCell("x"), 350, 300, 60, 60);
      Object v6 = graph.insertVertex(new BfsGraphCell("w"), 200, 400, 60, 60);
      Object v7 = graph.insertVertex(new BfsGraphCell("u"), 460, 160, 60, 60);
      Object v8 = graph.insertVertex(new BfsGraphCell("z"), 460, 380, 60, 60);
      graph.insertEdge(null, v1, v2);
      graph.insertEdge(null, v2, v3);
      graph.insertEdge(null, v1, v6);
      graph.insertEdge(null, v6, v4);
      graph.insertEdge(null, v6, v5);
      graph.insertEdge(null, v5, v4);
      graph.insertEdge(null, v5, v7);
      graph.insertEdge(null, v5, v8);
      graph.insertEdge(null, v7, v4);
      graph.insertEdge(null, v7, v8);
      //graph.setKeepEdgesInBackground(true);
    } finally {
      graph.getModel().endUpdate();
    }
    return graph;
  }

  public static void main(String[] args) throws IOException {
    GraphIO.writeGraph(getBfsGraph(), Paths.get(System.getProperty("user.dir"), "BfsGraph1.xml"));
    GraphIO.writeGraph(getBfsGraph2(), Paths.get(System.getProperty("user.dir"), "BfsGraph2.xml"));
    GraphIO.writeGraph(getDfsGraph(), Paths.get(System.getProperty("user.dir"), "DfsGraph1.xml"));
    GraphIO.writeGraph(getDfsGraph2(), Paths.get(System.getProperty("user.dir"), "DfsGraph2.xml"));
    GraphIO.writeGraph(getTopologicalSortGraph(), Paths.get(System.getProperty("user.dir"), "TopologicalSortGraph1.xml"));
    GraphIO.writeGraph(getSccGraph(), Paths.get(System.getProperty("user.dir"), "SccGraph1.xml"));
    GraphIO.writeGraph(getBellmanFordGraph(), Paths.get(System.getProperty("user.dir"), "BellmanFordGraph1.xml"));
    GraphIO.writeGraph(getDijkstraGraph(), Paths.get(System.getProperty("user.dir"), "DijkstraGraph1.xml"));
    GraphIO.writeGraph(getDijkstraGraph2(), Paths.get(System.getProperty("user.dir"), "DijkstraGraph2.xml"));
  }
}
