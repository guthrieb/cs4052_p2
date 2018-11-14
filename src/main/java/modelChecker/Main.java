package modelChecker;

import formula.FormulaParser;
import formula.stateFormula.AtomicProp;
import formula.stateFormula.StateFormula;
import model.Model;
import model.State;
import model.Transition;

import java.io.IOException;
import java.util.Arrays;

public class Main {

    private static final String QUERY_FILEPATH = "src/test/resources/ctl1";
    private static final String MODEL_FILEPATH = "src/test/resources/model1.json";
    private static final String CONSTRAINT_FILE_PATH = "src/test/resources/constraint1";

    public static void main(String[] args) {
        try {
            FormulaParser constraintParser = new FormulaParser(CONSTRAINT_FILE_PATH);
            FormulaParser queryParser = new FormulaParser(QUERY_FILEPATH);
            Model model = Model.parseModel(MODEL_FILEPATH);
            StateFormula query = queryParser.parse();
            StateFormula constraint = constraintParser.parse();


            SimpleModelChecker simpleModelChecker = new SimpleModelChecker();
            simpleModelChecker.check(model, query, constraint);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
