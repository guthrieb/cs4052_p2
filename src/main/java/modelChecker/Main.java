package modelChecker;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import model.Model;
import modelChecker.graphbuilding.InvalidTracingException;
import modelChecker.tracing.InvalidStateFormula;

import java.io.IOException;

public class Main {

    private static final String QUERY_FILEPATH = "src/test/resources/ctl6.json";
    private static final String MODEL_FILEPATH = "src/test/resources/mutual-exclusion.json";
    private static final String CONSTRAINT_FILE_PATH = "src/test/resources/constraint2.json";

    public static void main(String[] args) {
        try {

//            StateFormula stateFormula = FormulaParser.parseRawFormulaString("((A(q U r)))");
            StateFormula aTrue = FormulaParser.parseRawFormulaString("TRUE");

            FormulaParser parser = new FormulaParser(QUERY_FILEPATH);

            SimpleModelChecker simpleModelChecker = new SimpleModelChecker();

            boolean check = simpleModelChecker.check(Model.parseModel(MODEL_FILEPATH), aTrue, parser.parse());

            if (!check) {
                simpleModelChecker.getTrace();
            } else {
                System.out.println("MODEL HOLDS: " + check);
            }


        } catch (IOException | InvalidStateFormula | InvalidTracingException e) {
            e.printStackTrace();
        }
    }
}
