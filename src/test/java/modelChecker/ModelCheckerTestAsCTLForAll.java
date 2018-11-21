package modelChecker;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import model.Model;
import modelChecker.tracing.InvalidStateFormula;
import modelChecker.validitychecking.ModelChecker;
import modelChecker.validitychecking.SimpleModelChecker;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ModelCheckerTestAsCTLForAll {
    /*

    TESTING FORALL UNTIL

     */

    @Test
    public void falseForallUntilActions4() throws IOException, InvalidStateFormula {
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

    @Test
    public void trueForallUntilActions3() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/forall/until/until_actions_query6.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }



        /*

    TESTING FORALL NEXT

     */

    @Test
    public void trueForallNextActions1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/forall/next/next_actions_query1.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void trueForallNextActions2() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model3.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/forall/next/next_actions_query1.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void falseForallNextActions1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model3.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/forall/next/next_actions_query2.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    /*

    TESTING FORALL ALWAYS

     */

    @Test
    public void trueForallAlwaysActions1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model4.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/forall/always/always_actions_query1.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void trueForallAlwaysActions2() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/forall/always/always_actions_query2.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void falseForallAlwaysActions1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/forall/always/always_actions_query3.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    @Test
    public void falseForallAlwaysActions2() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/forall/always/always_actions_query4.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    /*

    TESTING FORALL EVENTUALLY

     */

    @Test
    public void trueForallEventuallyActions1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/forall/eventually/eventually_actions_query1.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }


    @Test
    public void trueForallEventuallyActions2() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/forall/eventually/eventually_actions_query2.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void trueForallEventuallyActions3() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/forall/eventually/eventually_actions_query3.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }


}
