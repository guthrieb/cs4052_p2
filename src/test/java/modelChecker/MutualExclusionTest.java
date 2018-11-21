package modelChecker;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import model.Model;
import modelChecker.tracing.InvalidStateFormula;
import modelChecker.tracing.InvalidTracingException;
import modelChecker.validitychecking.SimpleModelChecker;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class MutualExclusionTest {

    @Test
    public void mutualExclusionCriticalSectionTest() throws IOException, InvalidStateFormula, InvalidTracingException {
        StateFormula aTrue = FormulaParser.parseRawFormulaString("TRUE");

        FormulaParser parser = new FormulaParser("src/test/resources/mutual-exclusion-test-1.json");

        SimpleModelChecker simpleModelChecker = new SimpleModelChecker();

        boolean check = simpleModelChecker.check(Model.parseModel("src/test/resources/mutual-exclusion.json"), aTrue, parser.parse());

        if (!check) {
            String[] trace = simpleModelChecker.getTrace();

            System.out.println("MODEL DOES NOT HOLD, EXAMPLE TRACE: " + Arrays.toString(trace));
        } else {
            System.out.println("MODEL HOLDS: " + check);
        }

        Assert.assertTrue(check);
    }

    @Test
    public void mutualExclusionReachabilityTest() throws IOException, InvalidStateFormula, InvalidTracingException {
        StateFormula aTrue = FormulaParser.parseRawFormulaString("TRUE");

        FormulaParser parser = new FormulaParser("src/test/resources/mutual-exclusion-test-2.json");

        SimpleModelChecker simpleModelChecker = new SimpleModelChecker();

        boolean check = simpleModelChecker.check(Model.parseModel("src/test/resources/mutual-exclusion.json"), aTrue, parser.parse());

        if (!check) {
            String[] trace = simpleModelChecker.getTrace();

            System.out.println("MODEL DOES NOT HOLD, EXAMPLE TRACE: " + Arrays.toString(trace));
        } else {
            System.out.println("MODEL HOLDS: " + check);
        }

        Assert.assertTrue(check);
    }
}
