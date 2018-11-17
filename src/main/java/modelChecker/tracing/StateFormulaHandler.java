package modelChecker.tracing;

import formula.pathFormula.PathFormula;
import formula.stateFormula.*;
import model.Model;
import model.State;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StateFormulaHandler {
    public static Set<State> getStates(Model model, StateFormula formula) throws InvalidStateFormula {
        if(formula instanceof AtomicProp) {
            return getAtomicPropStates(model, (AtomicProp)formula);
        } else if (formula instanceof BoolProp) {
            return getBoolPropStates(model, (BoolProp) formula);
        } else if (formula instanceof Not) {
            return getNotStates(model, (Not) formula);
        } else if (formula instanceof Or) {
            return getOrStates(model, (Or) formula);
        } else if (formula instanceof And) {
            return getAndStates(model, (And) formula);
        } else if (formula instanceof ThereExists) {
            return getThereExistsStates(model, (ThereExists) formula);
        } else {
            System.out.println(formula);
            throw new InvalidStateFormula("Property must be in ENF form");
        }
    }

    private static  Set<State> getAndStates(Model model, And formula) throws InvalidStateFormula {
        Set<State> leftAcceptingStates = getStates(model, formula.left);
        Set<State> rightAcceptingStates = getStates(model, formula.right);

        leftAcceptingStates.retainAll(rightAcceptingStates);
        return leftAcceptingStates;
    }

    private static  Set<State> getOrStates(Model model, Or formula) throws InvalidStateFormula {
        Set<State> leftAcceptingStates = getStates(model, formula.left);
        Set<State> rightAcceptingStates = getStates(model, formula.right);
        leftAcceptingStates.addAll(rightAcceptingStates);

        return leftAcceptingStates;
    }

    private static  Set<State> getNotStates(Model model, Not formula) throws InvalidStateFormula {
        Set<State> acceptingStates = new HashSet<>(Arrays.asList(model.getStates()));
        Set<State> subformulaStates = getStates(model, formula.stateFormula);

        acceptingStates.removeAll(subformulaStates);
        return acceptingStates;
    }

    private static  Set<State> getBoolPropStates(Model model, BoolProp formula) {
        Set<State> states = new HashSet<>();
        if(formula.value) {
            states.addAll(Arrays.asList(model.getStates()));
        }
        return states;
    }

    private static  Set<State> getAtomicPropStates(Model model, AtomicProp prop) {
        Set<State> acceptingStates = new HashSet<>();
        for(State state : model.getStates()) {
            for(String label : state.getLabels()) {
                if(prop.label.equals(label)) {
                    acceptingStates.add(state);
                }
            }
        }
        return acceptingStates;
    }

    private static Set<State> getThereExistsStates(Model model, ThereExists exists) throws InvalidStateFormula {
        PathFormula pathFormula = exists.pathFormula;
        Set<State> states = PathFormulaHandler.getStates(model, pathFormula);
        return combineAcceptingAndInitialStates(model, states);
    }

    public static Set<State> getInitialStates(Model model) {
        State[] states = model.getStates();
        Set<State> initialStates = new HashSet<>();
        for(State state : states) {
            if(state.isInit()) {
                initialStates.add(state);
            }
        }
        return initialStates;
    }

    public static Set<State> combineAcceptingAndInitialStates(Model model, Set<State> acceptingStates) {
        Set<State> initialStates = getInitialStates(model);
        initialStates.retainAll(acceptingStates);
        return initialStates;
    }

}
