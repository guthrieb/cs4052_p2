package modelChecker;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import model.Model;
import modelChecker.graphbuilding.GraphDrawer;
import modelChecker.tracing.EnfConverter;
import modelChecker.tracing.InvalidStateFormula;

import java.io.IOException;

public class Main {

    private static final String QUERY_FILEPATH = "src/test/resources/ctl2.json";
    private static final String MODEL_FILEPATH = "src/test/resources/model1.json";
    private static final String CONSTRAINT_FILE_PATH = "src/test/resources/constraint2.json";

    public static void main(String[] args) {
        try {
            FormulaParser constraintParser = new FormulaParser(CONSTRAINT_FILE_PATH);
            StateFormula stateFormula1 = FormulaParser.parseRawFormulaString("(E(p U q))");
            Model model = Model.parseModel(MODEL_FILEPATH);
            StateFormula constraint = constraintParser.parse();

            EnfConverter converter = new EnfConverter();
            StateFormula stateFormula = converter.convertToEnf(stateFormula1);
            
            ModelChecker modelChecker = new SimpleModelChecker();
            modelChecker.check(model, constraint, stateFormula);

            GraphDrawer.draw(stateFormula);
        } catch (IOException | InvalidStateFormula e) {
            e.printStackTrace();
        }
    }
}
