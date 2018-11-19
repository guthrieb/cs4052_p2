package modelChecker;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import model.Model;
import modelChecker.tracing.InvalidStateFormula;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ModelCheckerTestAsCTLForAll {
    /*

    TESTING FORALL UNTIL

     */

    @Test
    public void trueForallUntilActions1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/forall/until/until_actions_query1.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    @Test
    public void falseForallUntilActions1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/forall/until/until_actions_query2.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    @Test
    public void falseForallUntilActions2() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/forall/until/until_actions_query3.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    @Test
    public void trueForallUntilActions2() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/forall/until/until_actions_query4.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }
}
