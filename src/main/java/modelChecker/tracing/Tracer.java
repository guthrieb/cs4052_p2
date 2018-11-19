package modelChecker.tracing;

import formula.stateFormula.Not;
import formula.stateFormula.StateFormula;
import formula.stateFormula.StateFormulaHandler;
import model.Model;
import model.State;
import modelChecker.graphbuilding.InvalidTracingException;
import modelChecker.graphbuilding.PathTree;

import java.util.HashSet;
import java.util.Set;

public class Tracer {
    public String[] getTrace(Model model, Set<State> stateSet, Set<State> acceptingStates, StateFormula formula) throws InvalidTracingException, InvalidStateFormula {

        Set<State> failedStates = getFailedStates(acceptingStates, stateSet);

        State initialState = getFailedInitialState(failedStates);
        if (initialState == null) {
            throw new InvalidTracingException();
        }


        //Assume the initial entry in formula will be one of Forall or Exists
        PathTree tree = buildPathTree(model, stateSet, new Not(formula));

        String[] path = generatePath(initialState, failedStates, new Not(formula), tree);

        return new String[0];
    }

    private String[] generatePath(State initialState, Set<State> failedStates, StateFormula formula, PathTree tree) {


        return new String[0];
    }

    private State getFailedInitialState(Set<State> failedStates) {
        State initialState = null;
        for (State state : failedStates) {
            if (state.isInit()) {
                initialState = state;
                break;
            }
        }
        return initialState;
    }

    private PathTree buildPathTree(Model model, Set<State> stateSet, StateFormula formula) throws InvalidStateFormula {
        PathTree pathTree = new PathTree(formula, 0);
        StateFormulaHandler.getStates(model, stateSet, formula, pathTree);

        return pathTree;
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
