package formula.stateFormula;

import formula.pathFormula.PathFormula;
import formula.pathFormula.PathFormulaHandler;
import model.Model;
import model.State;
import modelChecker.graphbuilding.PathTree;
import modelChecker.tracing.InvalidStateFormula;

import java.util.HashSet;
import java.util.Set;

public class StateFormulaHandler {
    public static Set<State> getStates(Model model, Set<State> statesToCheck, StateFormula formula, PathTree pathTree) throws InvalidStateFormula {
        PathTree nextPathTree = new PathTree(formula, pathTree.depth + 1);

        Set<State> acceptingStates;
        if (formula instanceof AtomicProp) {
            acceptingStates = getAtomicPropStates(model, statesToCheck, (AtomicProp) formula, nextPathTree);
//            return getAtomicPropStates(model, statesToCheck, (AtomicProp)formula);
        } else if (formula instanceof BoolProp) {
            acceptingStates = getBoolPropStates(model, statesToCheck, (BoolProp) formula, nextPathTree);
//            return getBoolPropStates(model, statesToCheck, (BoolProp) formula);
        } else if (formula instanceof Not) {
            acceptingStates = getNotStates(model, statesToCheck, (Not) formula, nextPathTree);
            System.out.println("NOT ACCEPTING STATES: " + acceptingStates);
//            return getNotStates(model, statesToCheck, (Not) formula);
        } else if (formula instanceof Or) {
            acceptingStates = getOrStates(model, statesToCheck, (Or) formula, nextPathTree);
//            return getOrStates(model, statesToCheck, (Or) formula);
        } else if (formula instanceof And) {
            acceptingStates = getAndStates(model, statesToCheck, (And) formula, nextPathTree);
//            return getAndStates(model, statesToCheck, (And) formula);
        } else if (formula instanceof ThereExists) {
            acceptingStates = getThereExistsStates(model, statesToCheck, (ThereExists) formula, nextPathTree);
//            return getThereExistsStates(model, statesToCheck, (ThereExists) formula);
        } else {
            throw new InvalidStateFormula("Property must be in ENF form");
        }

        nextPathTree.setAcceptingStates(acceptingStates);
        pathTree.children.add(nextPathTree);

        return acceptingStates;
    }

    private static Set<State> getAndStates(Model model, Set<State> statesToCheck, And formula, PathTree pathTree) throws InvalidStateFormula {
        Set<State> leftAcceptingStates = getStates(model, statesToCheck, formula.left, pathTree);
        Set<State> rightAcceptingStates = getStates(model, statesToCheck, formula.right, pathTree);

        leftAcceptingStates.retainAll(rightAcceptingStates);

        System.out.println("\nEvaluating And: " + formula);
        System.out.println("Accepting States: " + leftAcceptingStates);
        return leftAcceptingStates;
    }

    private static Set<State> getOrStates(Model model, Set<State> stateSet, Or formula, PathTree pathTree) throws InvalidStateFormula {
        //TODO introduce better ENF to make this method redundant
        Set<State> leftAcceptingStates = getStates(model, stateSet, formula.left, pathTree);
        Set<State> rightAcceptingStates = getStates(model, stateSet, formula.right, pathTree);
        leftAcceptingStates.addAll(rightAcceptingStates);

        System.out.println("\nEvaluating Or: " + formula);
        System.out.println("Accepting States: " + leftAcceptingStates);
        return leftAcceptingStates;
    }

    private static Set<State> getNotStates(Model model, Set<State> stateSet, Not formula, PathTree pathTree) throws InvalidStateFormula {
        Set<State> acceptingStates = new HashSet<>(stateSet);
        Set<State> subformulaStates = getStates(model, stateSet, formula.stateFormula, pathTree);

        acceptingStates.removeAll(subformulaStates);

        System.out.println("\nEvaluating Not: " + formula);
        System.out.println("Accepting States: " + acceptingStates);
        return acceptingStates;
    }

    private static Set<State> getBoolPropStates(Model model, Set<State> stateSet, BoolProp formula, PathTree pathTree) {
        Set<State> acceptingStates = new HashSet<>();
        if (formula.value) {
            acceptingStates.addAll(stateSet);
        }

        System.out.println("\nEvaluating BoolProp: " + formula);
        System.out.println("Accepting States: " + acceptingStates);
        return acceptingStates;
    }

    private static Set<State> getAtomicPropStates(Model model, Set<State> stateSet, AtomicProp prop, PathTree pathTree) {

        Set<State> acceptingStates = new HashSet<>();
        for (State state : stateSet) {
            for (String label : state.getLabels()) {


                if (prop.label.equals(label)) {
                    acceptingStates.add(state);
                }
            }
        }

        System.out.println("\nEvaluating Atomic Prop: " + prop);
        System.out.println("Accepting States: " + acceptingStates);
        return acceptingStates;
    }

    private static Set<State> getThereExistsStates(Model model, Set<State> stateSet, ThereExists exists, PathTree pathTree) throws InvalidStateFormula {
        PathFormula pathFormula = exists.pathFormula;

        Set<State> acceptingStates = PathFormulaHandler.getStates(model, stateSet, pathFormula, pathTree);


        System.out.println("\nEvaluating Exists: " + exists);
        System.out.println("Accepting States: " + acceptingStates);
        return acceptingStates;
    }


}
