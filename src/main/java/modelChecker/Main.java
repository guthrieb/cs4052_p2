package modelChecker;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import guru.nidi.graphviz.attribute.Color;
import model.Model;
import model.Transition;
import modelChecker.graphbuilding.InvalidTracingException;
import modelChecker.graphbuilding.TraceGraphDrawer;
import modelChecker.tracing.InvalidStateFormula;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static guru.nidi.graphviz.model.Factory.node;

public class Main {

    private static final String QUERY_FILEPATH = "src/test/resources/ctl1.json";
    private static final String MODEL_FILEPATH = "src/test/resources/mutual-exclusion.json";
    private static final String CONSTRAINT_FILE_PATH = "src/test/resources/constraint2.json";

    public static void main(String[] args) {
        try {

//            StateFormula stateFormula = FormulaParser.parseRawFormulaString("((A(q U r)))");
            StateFormula aTrue = FormulaParser.parseRawFormulaString("TRUE");

            FormulaParser parser = new FormulaParser(QUERY_FILEPATH);

            SimpleModelChecker simpleModelChecker = new SimpleModelChecker();

            Model parsedModel = Model.parseModel(MODEL_FILEPATH);

            boolean check = simpleModelChecker.check(parsedModel, aTrue, parser.parse());

            if (!check) {
                String[] trace = simpleModelChecker.getTrace();

                System.out.println("MODEL DOES NOT HOLD, EXAMPLE TRACE: " + Arrays.toString(trace));

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


            } else {
                System.out.println("MODEL HOLDS: " + check);
            }


        } catch (IOException | InvalidStateFormula | InvalidTracingException e) {
            e.printStackTrace();
        }
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
