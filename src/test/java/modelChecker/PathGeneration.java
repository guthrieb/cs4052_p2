package modelChecker;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import model.Model;
import modelChecker.tracing.InvalidStateFormula;
import modelChecker.tracing.InvalidTracingException;
import modelChecker.validitychecking.ModelChecker;
import modelChecker.validitychecking.SimpleModelChecker;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class PathGeneration {

    @Test
    public void basicNextPath() throws IOException, InvalidStateFormula, InvalidTracingException {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("AX(p)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
        String[] trace = modelChecker.getTrace();
        Assert.assertEquals(trace[0], "s0");
        Assert.assertEquals(trace[1], "s1");
    }

    @Test
    public void basicAlwaysPath2() throws IOException, InvalidStateFormula, InvalidTracingException {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("AG(p)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
        String[] trace = modelChecker.getTrace();
        Assert.assertEquals(trace[0], "s0");
    }

    @Test
    public void basicAlwaysPath3() throws IOException, InvalidStateFormula, InvalidTracingException {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("AG(q)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);

        String[] trace = modelChecker.getTrace();
        Assert.assertEquals(trace[0], "s0");
        Assert.assertEquals(trace[1], "s1");
        Assert.assertEquals(trace[2], "s2");
    }

    @Test(expected = InvalidTracingException.class)
    public void basicFailureTracingAttempt() throws IOException, InvalidStateFormula, InvalidTracingException {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("AG(q || p)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);

        modelChecker.getTrace();
    }

    @Test
    public void basicUntilTrace() throws IOException, InvalidStateFormula, InvalidTracingException {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("A(q U r)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);

        String[] trace = modelChecker.getTrace();

        Assert.assertEquals("s0", trace[0]);
        Assert.assertEquals("s1", trace[1]);
        Assert.assertEquals("s3", trace[2]);
        Assert.assertEquals("s3", trace[3]);
    }

    @Test
    public void basicEventuallyTrace() throws IOException, InvalidStateFormula, InvalidTracingException {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("TRUE");
        StateFormula query = FormulaParser.parseRawFormulaString("AF(r)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);

        String[] trace = modelChecker.getTrace();

        Assert.assertEquals("s0", trace[0]);
        Assert.assertEquals("s1", trace[1]);
        Assert.assertEquals("s3", trace[2]);
    }
}
