package modelChecker.graphbuilding;

import guru.nidi.graphviz.attribute.Color;
import model.Model;
import model.Transition;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static guru.nidi.graphviz.model.Factory.node;

public class PathDrawing {

    public static void drawGraph(String[] trace, Model parsedModel) throws IOException {
        TraceGraphDrawer drawer = new TraceGraphDrawer();

        for (String nodeInTrace : trace) {
            drawer.addNode(node(nodeInTrace), Color.RED);
        }

        for (int i = 0; i < trace.length - 1; i++) {
            drawer.addLink(node(trace[i]), node(trace[i + 1]), Color.RED);
        }

        Transition[] transitions = parsedModel.getTransitions();

        for (Transition transition : transitions) {

            if (!transitionAlreadyDrawn(trace, transition)) {
                drawer.addLink(node(transition.getSource()), node(transition.getTarget()), Color.BLACK);
            }
        }

        drawer.toPNG();
        File file = new File("drawings/failed_path.png");

        Desktop desktop = Desktop.getDesktop();
        if (file.exists()) desktop.open(file);
    }

    public static boolean transitionAlreadyDrawn(String[] trace, Transition transition) {
        for (int i = 0; i < trace.length - 1; i++) {
            if ((trace[i].equals(transition.getSource()) && trace[i + 1].equals(transition.getTarget()))) {
                return true;
            }
        }

        return false;
    }
}
