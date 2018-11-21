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

public class FairnessConstraintTesting {

    @Test
    public void basicFairnessConstraintTest() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("!r");
        StateFormula query = FormulaParser.parseRawFormulaString("AG(q)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertTrue(check);
    }

    @Test
    public void basicFairnessConstraintTest2() throws IOException, InvalidStateFormula {
        Model model = Model.parseModel("src/test/resources/model2.json");

        StateFormula fairnessConstraint = FormulaParser.parseRawFormulaString("!((p) && (q))");
        StateFormula query = FormulaParser.parseRawFormulaString("AG(q)");
        ModelChecker modelChecker = new SimpleModelChecker();
        boolean check = modelChecker.check(model, fairnessConstraint, query);

        Assert.assertFalse(check);
    }
}
