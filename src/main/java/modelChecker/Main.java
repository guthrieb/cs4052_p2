package modelChecker;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import model.Model;
import model.State;
import model.Transition;
import modelChecker.graphbuilding.FormulaTree;
import modelChecker.graphbuilding.GraphDrawer;
import modelChecker.tracing.EnfConverter;
import modelChecker.tracing.InvalidStateFormula;

import java.io.IOException;
import java.util.Arrays;

public class Main {

    private static final String QUERY_FILEPATH = "src/test/resources/ctl1.json";
    private static final String MODEL_FILEPATH = "src/test/resources/model1.json";
    private static final String CONSTRAINT_FILE_PATH = "src/test/resources/constraint1.json";

    public static void main(String[] args) {
        try {
            FormulaParser constraintParser = new FormulaParser(CONSTRAINT_FILE_PATH);
            FormulaParser queryParser = new FormulaParser(QUERY_FILEPATH);
            StateFormula stateFormula1 = FormulaParser.parseRawFormulaString("EF(p)");
            Model model = Model.parseModel(MODEL_FILEPATH);
            StateFormula query = queryParser.parse();
            StateFormula constraint = constraintParser.parse();

            EnfConverter converter = new EnfConverter();
            StateFormula stateFormula = converter.convertToEnf(stateFormula1);
            
            ModelChecker modelChecker = new SimpleModelChecker();
            modelChecker.check(model, constraint, stateFormula);
        } catch (IOException | InvalidStateFormula e) {
            e.printStackTrace();
        }
    }
}
