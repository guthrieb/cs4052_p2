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

public class ModelCheckerTestasCTLThereExists {
    /*

    TESTING EXISTS UNTIL

     */

    @Test
    public void trueExistsUntilActions1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/thereexists/until/test_query1.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void falseExistsUntilActions1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/thereexists/until/test_query2.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    @Test
    public void trueExistsUntilActions2() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/thereexists/until/test_query3.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void falseExistsUntilActions2() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/thereexists/until/test_query4.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void trueExistsUntilActions3() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/thereexists/until/test_query5.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void falseExistsUntilActions3() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/thereexists/until/test_query6.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    @Test
    public void trueExistsUntilActions4() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/thereexists/until/test_query7.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    /*

    TESTING EXISTS NEXT

     */

    @Test
    public void trueExistsNextActions1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/thereexists/next/next_actions_query1.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void falseExistsNextActions1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/thereexists/next/next_actions_query2.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    /*

    TESTING EXISTS ALWAYS

     */

    @Test
    public void trueExistsAlwaysActions1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/thereexists/always/always_actions_query1.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void falseExistsAlwaysActions1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/thereexists/always/always_actions_query2.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    /*

    TESTING EXISTS EVENTUALLY

     */

    @Test
    public void trueExistsEventuallyActions1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/thereexists/eventually/eventually_actions_query1.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void falseExistsEventuallyActions1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/thereexists/eventually/eventually_actions_query2.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    @Test
    public void trueExistsEventuallyActions2() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/thereexists/eventually/eventually_actions_query3.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void falseExistsEventuallyActions2() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/thereexists/eventually/eventually_actions_query4.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    @Test
    public void falseExistsEventuallyActions3() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/thereexists/eventually/eventually_actions_query5.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    @Test
    public void trueExistsEventuallyActions3() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/thereexists/eventually/eventually_actions_query6.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void trueExistsEventuallyActions4() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        FormulaParser parser = new FormulaParser("src/test/resources/other-examples/thereexists/eventually/eventually_actions_query7.json");
        StateFormula query = parser.parse();
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

}
