package modelChecker;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import model.Model;
import modelChecker.graphbuilding.InvalidTracingException;
import modelChecker.tracing.InvalidStateFormula;

import java.io.IOException;
import java.util.Arrays;

import static modelChecker.graphbuilding.PathDrawing.drawGraph;

public class Main {

    private static String QUERY_FILEPATH;
    private static String MODEL_FILEPATH;
    private static String CONSTRAINT_FILE_PATH;

    public static void main(String[] args) {
        try {

            if (args.length != 4) {
                System.out.println("usage: Main query_filepath model_filepath constraint_filepath");
                System.exit(1);
            }

            QUERY_FILEPATH = args[1];
            MODEL_FILEPATH = args[2];
            CONSTRAINT_FILE_PATH = args[3];

//            StateFormula stateFormula = FormulaParser.parseRawFormulaString("((A(q U r)))");
            StateFormula aTrue = FormulaParser.parseRawFormulaString("TRUE");

            FormulaParser parser = new FormulaParser(QUERY_FILEPATH);

            SimpleModelChecker simpleModelChecker = new SimpleModelChecker();

            Model parsedModel = Model.parseModel(MODEL_FILEPATH);

            boolean check = simpleModelChecker.check(parsedModel, aTrue, parser.parse());

            if (!check) {
                String[] trace = simpleModelChecker.getTrace();

                System.out.println("MODEL DOES NOT HOLD, EXAMPLE TRACE: " + Arrays.toString(trace));
                drawGraph(trace, parsedModel);

            } else {
                System.out.println("MODEL HOLDS: " + check);
            }


        } catch (IOException | InvalidStateFormula | InvalidTracingException e) {
            e.printStackTrace();
        }
    }


}
