package modelChecker.tracing;

import formula.stateFormula.Not;
import formula.stateFormula.StateFormula;
import formula.stateFormula.StateFormulaHandler;
import model.Model;
import model.State;
import modelChecker.graphbuilding.InvalidTracingException;
import modelChecker.graphbuilding.PathTree;

import java.util.*;

public class Tracer {
    public String[] getTrace(Model model, Set<State> stateSet, Set<State> acceptingStates, StateFormula formula) throws InvalidTracingException, InvalidStateFormula {

        Set<State> failedStates = getFailedStates(acceptingStates, stateSet);

        State initialState = getFailedInitialState(failedStates);
        if (initialState == null) {
            throw new InvalidTracingException("Not initial state fails");
        }


        //Assume the initial entry in formula will be one of Forall or Exists
        HashMap<StateFormula, PathTree> pathData = new HashMap<>();
        EnfConverter enfConverter = new EnfConverter(model);


        StateFormula stateFormula = enfConverter.convertToEnf(formula);
        if (stateFormula instanceof Not) {
            stateFormula = ((Not) stateFormula).stateFormula;
        } else {
            throw new InvalidTracingException("Passed Invalid Formula: " + formula);
        }

        PathTree tree = buildPathTree(model, stateSet, stateFormula, pathData);


        System.out.println(stateFormula);
        System.out.println("\n\n\n");
        for (Map.Entry<StateFormula, PathTree> entry : pathData.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().getAcceptingStates());
        }


        List<State> path = PathTracer.generatePath(model, initialState, stateSet, failedStates, stateFormula, tree, pathData);

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

    private PathTree buildPathTree(Model model, Set<State> stateSet, StateFormula formula, Map<StateFormula, PathTree> pathData) throws InvalidStateFormula {
        PathTree pathTree = new PathTree(formula, 0);

        StateFormulaHandler.getStates(model, stateSet, formula, pathTree, pathData);

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
