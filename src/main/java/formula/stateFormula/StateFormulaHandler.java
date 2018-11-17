package formula.stateFormula;

import formula.pathFormula.PathFormula;
import formula.pathFormula.PathFormulaHandler;
import model.Model;
import model.State;
import modelChecker.tracing.InvalidStateFormula;

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
            throw new InvalidStateFormula("Property must be in ENF form");
        }
    }

    private static  Set<State> getAndStates(Model model, And formula) throws InvalidStateFormula {
        Set<State> leftAcceptingStates = getStates(model, formula.left);
        Set<State> rightAcceptingStates = getStates(model, formula.right);

        leftAcceptingStates.retainAll(rightAcceptingStates);

        System.out.println("\nEvaluating And: " + formula);
        System.out.println("Accepting States: " + leftAcceptingStates);
        return leftAcceptingStates;
    }

    private static  Set<State> getOrStates(Model model, Or formula) throws InvalidStateFormula {
        //TODO introduce better ENF to make this method redundant
        Set<State> leftAcceptingStates = getStates(model, formula.left);
        Set<State> rightAcceptingStates = getStates(model, formula.right);
        leftAcceptingStates.addAll(rightAcceptingStates);

        System.out.println("\nEvaluating Or: " + formula);
        System.out.println("Accepting States: " + leftAcceptingStates);
        return leftAcceptingStates;
    }

    private static  Set<State> getNotStates(Model model, Not formula) throws InvalidStateFormula {
        Set<State> acceptingStates = new HashSet<>(Arrays.asList(model.getStates()));
        Set<State> subformulaStates = getStates(model, formula.stateFormula);

        acceptingStates.removeAll(subformulaStates);

        System.out.println("\nEvaluating Not: " + formula);
        System.out.println("Accepting States: " + acceptingStates);
        return acceptingStates;
    }

    private static  Set<State> getBoolPropStates(Model model, BoolProp formula) {
        Set<State> acceptingStates = new HashSet<>();
        if(formula.value) {
            acceptingStates.addAll(Arrays.asList(model.getStates()));
        }

        System.out.println("\nEvaluating BoolProp: " + formula);
        System.out.println("Accepting States: " + acceptingStates);
        return acceptingStates;
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

        System.out.println("\nEvaluating Atomic Prop: " + prop);
        System.out.println("Accepting States: " + acceptingStates);
        return acceptingStates;
    }

    private static Set<State> getThereExistsStates(Model model, ThereExists exists) throws InvalidStateFormula {
        PathFormula pathFormula = exists.pathFormula;

        Set<State> states = PathFormulaHandler.getStates(model, pathFormula);


        Set<State> initialStatesSatisfyingThereExists = combineAcceptingAndInitialStates(model, states);

        System.out.println("\nEvaluating Exists: " + exists);
        System.out.println("Accepting States: " + initialStatesSatisfyingThereExists);
        return initialStatesSatisfyingThereExists;
    }

    private static Set<State> getInitialStates(Model model) {
        State[] states = model.getStates();
        Set<State> initialStates = new HashSet<>();
        for(State state : states) {
            if(state.isInit()) {
                initialStates.add(state);
            }
        }
        return initialStates;
    }

    private static Set<State> combineAcceptingAndInitialStates(Model model, Set<State> acceptingStates) {
        Set<State> initialStates = getInitialStates(model);
        initialStates.retainAll(acceptingStates);

        return initialStates;
    }

}
