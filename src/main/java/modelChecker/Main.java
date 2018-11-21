package modelChecker;

import modelChecker.graphbuilding.ModelGraphDrawer;

import java.io.IOException;
import java.util.Collections;

public class Main {

    private static final String QUERY_FILEPATH = "src/test/resources/ctl6.json";
    private static final String MODEL_FILEPATH = "src/test/resources/mutual-exclusion.json";
    private static final String CONSTRAINT_FILE_PATH = "src/test/resources/constraint2.json";

    public static void main(String[] args) {
        try {
//
////            StateFormula stateFormula = FormulaParser.parseRawFormulaString("((A(q U r)))");
//            StateFormula aTrue = FormulaParser.parseRawFormulaString("TRUE");
//
//            FormulaParser parser = new FormulaParser(QUERY_FILEPATH);
//
//            SimpleModelChecker simpleModelChecker = new SimpleModelChecker();
//
//            boolean check = simpleModelChecker.check(Model.parseModel(MODEL_FILEPATH), aTrue, parser.parse());

            ModelGraphDrawer drawer = new ModelGraphDrawer();
            drawer.Draw(Collections.EMPTY_SET, Collections.EMPTY_SET, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
//
//            if (!check) {
//                simpleModelChecker.getTrace();
//            } else {
//                System.out.println("MODEL HOLDS: " + check);
//
        } catch (IOException e) {
//    | InvalidStateFormula | InvalidTracingException e) {
            e.printStackTrace();
        }
    }
}

