package modelChecker;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import model.Model;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        try {
            StateFormula formula = FormulaParser.parseRawFormulaString("AG p");
            Model model = Model.parseModel("src/test/resources/model1.json");
            System.out.println(model);
            System.out.println(Arrays.toString(model.getTransitions()));
            System.out.println(Arrays.toString(model.getStates()));


//            System.out.println(formula);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
