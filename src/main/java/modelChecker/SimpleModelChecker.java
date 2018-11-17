package modelChecker;

import formula.stateFormula.StateFormula;
import model.Model;
import model.State;
import modelChecker.tracing.InvalidStateFormula;
import modelChecker.tracing.StateFormulaHandler;

import java.util.Set;

public class SimpleModelChecker implements ModelChecker {

    @Override
    public boolean check(Model model, StateFormula constraint, StateFormula query) throws InvalidStateFormula {
        model.getStates();
        model.getTransitions();
        System.out.println("model = [" + model + "], constraint = [" + constraint + "], query = [" + query + "]");

        Set<State> states = StateFormulaHandler.getStates(model, query);
        System.out.println(states);

        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String[] getTrace() {
        // TODO Auto-generated method stub
        return null;
    }
}
