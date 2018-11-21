package modelChecker.graphbuilding;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static guru.nidi.graphviz.model.Factory.*;

public class TraceGraphDrawer {

    Graph g;

    public TraceGraphDrawer() {
        this.g = graph().directed();
    }

    public void addNode(Node node, Color color) {
        g = g.with(node);
    }

    public void addLink(Node from, Node to, Color color) {
        g = g.with(from.link(to(to).with(color)));

    }

    public void toPNG() throws IOException {
        Graphviz.fromGraph(g).width(900).render(Format.PNG).toFile(new File("drawings/failed_path.png"));
        System.out.println("Printed graph.");
    }

    /**
     * Draws a directed graph of the model showing the path to the identified failure
     *
     * @param nodesInGraph   nodes in the graph
     * @param nodesToFailure nodes visited on a path to a failure
     * @throws IOException
     */
    public void Draw(Set<Node> nodesInGraph, Set<Node> nodesToFailure, List<Node[]> links, List<Node[]> linksToFailure) throws IOException {

        Node main = node("main").with(Label.html("<b>main</b><br/>start"), Color.rgb("1020d0").font());
        Node init = node(Label.markdown("**_init_**"));
        Node execute = node("execute");
        Node compare = node("compare").with(Shape.RECTANGLE, Style.FILLED, Color.hsv(.7, .3, 1.0));
        Node mkString = node("mkString").with(Label.of("make a\nstring"));
        Node printf = node("printf");

        Graph g = graph().directed();

//        for (Node node : nodesInGraph) {
//            addNodeToGraph(node, g);
//        }
//
//        for (Node[] nodePair : links) {
//            addLinkToGraph(nodePair[0], nodePair[1], Color.BLACK, g);
//        }
////
//        g = g.with(main.link(to(compare)).with(Color.RED));
////        g = g.link(main, node("compare").with(Color.PALEGREEN1));

        Graphviz.fromGraph(g).width(900).render(Format.PNG).toFile(new File("drawings/ex2.png"));

    }



}
