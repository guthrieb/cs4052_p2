package modelChecker.graphbuilding;

import formula.pathFormula.PathFormula;
import formula.stateFormula.StateFormula;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Path;

public class GraphDrawer {
    public static void draw(StateFormula toDraw) throws FileNotFoundException {
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
