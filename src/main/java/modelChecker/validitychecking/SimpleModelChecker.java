package modelChecker.validitychecking;

import formula.stateFormula.StateFormula;
import formula.stateFormula.StateFormulaHandler;
import model.Model;
import model.State;
import modelChecker.graphbuilding.PathTree;
import modelChecker.tracing.InvalidStateFormula;
import modelChecker.tracing.InvalidTracingException;
import modelChecker.tracing.Tracer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SimpleModelChecker implements ModelChecker {
    private Model model;
    private StateFormula constraint;
    private StateFormula query;
    private Set<State> constraintRestrictedStates = new HashSet<>();
    private Set<State> queriedStates = new HashSet<>();
    private boolean modelHolds = true;

    @Override
    public boolean check(Model model, StateFormula constraint, StateFormula query) throws InvalidStateFormula {
        this.model = model;
        this.constraint = constraint;
        this.query = query;

        model.getStates();
        model.getTransitions();

        EnfConverter converter = new EnfConverter(model);
        StateFormula enfQuery = converter.convertToEnf(query);
        StateFormula enfConstraint = converter.convertToEnf(constraint);

        System.out.println("ORIGINAL QUERY: " + query);
        System.out.println("ENF QUERY: " + enfQuery);

        this.constraintRestrictedStates = StateFormulaHandler.getStates(model,
                new HashSet<>(Arrays.asList(model.getStates())), enfConstraint, new PathTree(enfConstraint, 0), new HashMap<StateFormula, PathTree>());

        PathTree pathTree = new PathTree(null, 0);
        this.queriedStates = StateFormulaHandler.getStates(model, constraintRestrictedStates, enfQuery, pathTree, new HashMap<StateFormula, PathTree>());


        Set<State> states = insersectInitialStates(constraintRestrictedStates, queriedStates);
        this.modelHolds = states.size() > 0;

        System.out.println("PathTree: " + pathTree);
        return states.size() > 0;
    }

    private Set<State> getInitialStates(Set<State> stateSet) {
        Set<State> initialStates = new HashSet<>();
        for (State state : stateSet) {
            if (state.isInit()) {
                initialStates.add(state);
            }
        }
        return initialStates;
    }

    private Set<State> insersectInitialStates(Set<State> stateSet, Set<State> acceptingStates) {
        Set<State> initialStates = getInitialStates(stateSet);
        initialStates.retainAll(acceptingStates);

        return initialStates;
    }

    @Override
    public String[] getTrace() throws InvalidStateFormula, InvalidTracingException {
        Tracer tracer = new Tracer();

        return tracer.getTrace(model, constraintRestrictedStates, queriedStates, query);
    }
}

