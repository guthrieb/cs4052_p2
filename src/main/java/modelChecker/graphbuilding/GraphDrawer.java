package modelChecker.graphbuilding;

import formula.stateFormula.StateFormula;

import java.io.IOException;
import java.io.PrintWriter;

public class GraphDrawer {
    public static void draw(StateFormula toDraw) throws IOException {
        FormulaTree formulaTree = new FormulaTree(toDraw);
        String dotFormat = formulaTree.inDotFormat();


        PrintWriter writer = new PrintWriter("out.dot");
        writer.write(dotFormat);
        writer.flush();
        writer.close();
    }

    public static void main(String[] args) {

    }

}
