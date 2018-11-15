package modelChecker;

import formula.stateFormula.StateFormula;
import model.Model;

public class SimpleModelChecker implements ModelChecker {

    @Override
    public boolean check(Model model, StateFormula constraint, StateFormula query) {
        model.getStates();
        model.getTransitions();
        System.out.println("model = [" + model + "], constraint = [" + constraint + "], query = [" + query + "]");



        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String[] getTrace() {
        // TODO Auto-generated method stub
        return null;
    }
}
