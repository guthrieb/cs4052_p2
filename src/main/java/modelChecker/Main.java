package modelChecker;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import model.Model;

import java.io.IOException;

public class Main {

    private static final String QUERY_FILEPATH = "src/test/resources/testing/custom-ctl1.json";
    private static final String MODEL_FILEPATH = "src/test/resources/mutual-exclusion.json";
    private static final String CONSTRAINT_FILE_PATH = "src/test/resources/constraint2.json";

    public static void main(String[] args) {
        try {

//            StateFormula stateFormula = FormulaParser.parseRawFormulaString("((AG( p )))");
            StateFormula aTrue = FormulaParser.parseRawFormulaString("TRUE");

            SimpleModelChecker simpleModelChecker = new SimpleModelChecker();

            Model model = Model.parseModel(MODEL_FILEPATH);
//            simpleModelChecker.check(Model.parseModel(MODEL_FILEPATH), aTrue, stateFormula);
//            GraphDrawer.draw(query);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
