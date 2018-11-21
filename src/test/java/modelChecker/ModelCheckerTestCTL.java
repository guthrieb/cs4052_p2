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

import static org.junit.Assert.fail;

public class ModelCheckerTestCTL {

    /*
     * An example of how to set up and call the model building methods and make
     * a call to the model checker itself. The contents of model.json,
     * constraint1.json and ctl.json are just examples, you need to add new
     * models and formulas for the mutual exclusion task.
     */
    @Test
    public void buildAndCheckModel() {
        try {
            Model model = Model.parseModel("src/test/resources/model1.json");

            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/ctl1.json").parse();

            ModelChecker mc = new SimpleModelChecker();

//            mc.check(model, )
            // TO IMPLEMENT
            // assertTrue(mc.check(model, fairnessConstraint, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    @Test
    public void atomicPropTest() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model1.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("p");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void falseAtomicPropTest() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model1.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("q");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    @Test
    public void falseAndTest() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model1.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("(p && r)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    @Test
    public void trueOrTest() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model1.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("(p || r)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void trueOrTest2() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model1.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("(p || z)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void falseOrTest() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model1.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("(q || z)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    @Test
    public void trueNotTest() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model1.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("!(p)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void falseNotOrTest() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model1.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("!(p || r)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    @Test
    public void trueExistsNextTest1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model1.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("EX(p)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void trueExistsNextTest2() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model1.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("EX(p && q)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void falseExistsNextTest2() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model1.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("EX(z)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    @Test
    public void trueExistsEventuallyTest() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model1.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("EF(q)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void trueExistsEventuallyTest2() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("EF(p)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void trueExistsEventuallyTest3() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("EF(p && r)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void falseExistsEventuallyTest3() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("EF(q && r)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    @Test
    public void trueExistsAlwaysTest1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("EG(q || p)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void trueExistsAlwaysTest2() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("EG(q)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void trueExistsUntilTest1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("E((q)U(p))");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void trueExistsUntilTest2() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("E((q)U(r || q))");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void falseExistsUntilTest1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("E((q)U(z))");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    @Test
    public void trueForallUntilTest1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("A((q)U(p))");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void trueForallEventuallyTest1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("AF(p)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void falseForallEventuallyTest2() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("AF(r)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    @Test
    public void trueForallNextTest1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model1.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("AF(p)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void testFalseForAllUntil1() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("A((z) U (r))");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }

    @Test
    public void falseEventuallyUntil2() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("!E((q) U (r))");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }


}
