package modelChecker.tracing;

import formula.stateFormula.Not;
import formula.stateFormula.StateFormula;
import model.Model;
import model.State;
import modelChecker.SimpleModelChecker;
import modelChecker.graphbuilding.InvalidTracingException;
import modelChecker.graphbuilding.PathTree;

import java.util.HashSet;
import java.util.Set;

public class Tracer {
    public String[] getTrace(Model model, Set<State> stateSet, Set<State> acceptingStates, StateFormula formula) throws InvalidTracingException {
        Set<State> failedStates = getFailedStates(acceptingStates, stateSet);
        State initialState = null;
        for (State state : failedStates) {
            if (state.isInit()) {
                initialState = state;
                break;
            }
        }

        if (initialState == null) {
            throw new InvalidTracingException();
        }


        PathTree tree = buildPathTree(model, stateSet, new Not(formula));


        return new String[0];
    }

    private PathTree buildPathTree(Model model, Set<State> stateSet, StateFormula formula) {
        SimpleModelChecker simpleModelChecker = new SimpleModelChecker();


        return null;
    }

    private Set<State> getFailedStates(Set<State> acceptingStates, Set<State> stateSet) {
        Set<State> failedStates = new HashSet<>();
        for (State state : stateSet) {
            if (!acceptingStates.contains(state)) {
                failedStates.add(state);
            }
        }
        return failedStates;
    }
}
